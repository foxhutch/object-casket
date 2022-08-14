package org.fuchss.objectcasket.tablemodule.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.DatabaseObserver;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableObserver;

class TableImpl implements Table, DatabaseObserver {

	private SqlDatabase db;
	private SqlObjectFactory objFac;
	private TableAssignment dbTab;
	private TableAssignment pkTab;

	private boolean closed;
	private SqlArg pkArg;
	private PreCompiledStatement newRowStmt;
	private PreCompiledStatement deleteRowStmt;
	private PreCompiledStatement selectAllPKsStmt;
	private PreCompiledStatement selectRowByPkStmt;
	private CasketException exc;

	private Map<Object, RowImpl> pkRowMap = new HashMap<>();
	private Map<Row, RowImpl> myRows = new HashMap<>();
	private Set<TableObserver> observers = new HashSet<>();

	private Map<String, Class<? extends Serializable>> rowSignature = new HashMap<>();
	private String pkName;
	private Class<? extends Serializable> pkType;
	private boolean isAutoIncrementedPK;

	private TableModuleImpl myModule;

	protected TableImpl(TableModuleImpl module, TableAssignment dbTab, TableAssignment pkTab, SqlDatabase db, SqlObjectFactory objFac) {
		this.myModule = module;
		this.dbTab = dbTab;
		this.pkTab = pkTab;
		this.db = db;
		this.objFac = objFac;
	}

	protected void initSignature(Map<String, Class<? extends Serializable>> signature, String pkName, boolean autoIncrement) throws CasketException {
		signature.forEach(this.rowSignature::put);
		this.pkName = pkName;
		this.pkType = signature.get(pkName);
		this.isAutoIncrementedPK = autoIncrement;
		this.init();
		this.db.attach(this, this.pkTab);
	}

	private void init() throws CasketException {
		HashSet<SqlArg> pkArgSet = new HashSet<>();
		this.pkArg = this.db.mkSqlArg(this.dbTab, this.pkName, SqlArg.CMP.EQUAL);
		pkArgSet.add(this.pkArg);
		this.newRowStmt = this.db.mkNewRowStmt(this.dbTab);
		this.deleteRowStmt = this.db.mkDeleteStmt(this.dbTab, pkArgSet, SqlArg.OP.AND);
		this.selectAllPKsStmt = this.db.mkSelectStmt(this.pkTab, new HashSet<>(), SqlArg.OP.AND);
		this.selectRowByPkStmt = this.db.mkSelectStmt(this.dbTab, pkArgSet, SqlArg.OP.AND);
	}

	@Override
	public synchronized boolean register(TableObserver observer) {
		return this.observers.add(observer);
	}

	@Override
	public synchronized boolean deregister(TableObserver observer) {
		return this.observers.remove(observer);
	}

	@Override
	public synchronized void update(TableAssignment tabOrView, List<SqlObject> changed, List<SqlObject> deleted, List<SqlObject> added) {
		if (tabOrView != this.pkTab)
			return;

		Set<Row> changedRows = this.updateChanged(changed);
		Set<Row> deletedRows = this.updateDeleted(deleted);
		Set<Row> addedRows = this.updateAdded(added);
		changedRows.removeAll(deletedRows);
		if (changedRows.isEmpty() && deletedRows.isEmpty() && addedRows.isEmpty())
			return;
		this.observers.forEach(obs -> obs.update(changedRows, deletedRows, addedRows));
	}

	private Set<Row> updateChanged(List<SqlObject> changed) {
		Set<Row> changedRows = new HashSet<>();
		for (SqlObject obj : changed) {
			RowImpl row = this.pkRowMap.get(obj.get(this.pkType));
			if (row != null) {
				row.hasChangd();
				changedRows.add(row);
			}
		}
		return changedRows;
	}

	private Set<Row> updateDeleted(List<SqlObject> deleted) {
		Set<Row> deletedRows = new HashSet<>();
		for (SqlObject obj : deleted) {
			Object pk = obj.get(this.pkType);
			RowImpl row = this.pkRowMap.get(pk);
			if (row != null) {
				row.delete();
				deletedRows.add(row);
			}
			this.pkRowMap.remove(pk);
			this.myRows.remove(row);
		}
		return deletedRows;
	}

	private Set<Row> updateAdded(List<SqlObject> added) {
		Set<Row> addedRows = new HashSet<>();
		for (SqlObject obj : added) {
			Serializable pk = obj.get(this.pkType);
			Map<SqlArg, SqlObject> args = new HashMap<>();
			try {
				args.put(this.pkArg, this.objFac.mkSqlObject(this.dbTab.storageClass(this.pkName), pk));
				Map<String, SqlObject> rowSqlValues = this.db.select(this.selectRowByPkStmt, args, null).get(0);
				addedRows.add(this.updateMaps(this.mkRow(rowSqlValues, pk), pk, null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return addedRows;
	}

	protected void close() throws CasketException {
		if (this.closed)
			throw CasketError.ALREADY_CLOSED.build();
		this.closed = true;
		this.db.detach(this, this.pkTab);
		this.newRowStmt.close();
		this.deleteRowStmt.close();
		this.selectAllPKsStmt.close();
		this.selectRowByPkStmt.close();
	}

	@Override
	public synchronized Row createRow(Map<String, ? extends Serializable> values, Object voucher) throws CasketException {
		this.checkArgs(values, voucher);
		this.myModule.checkVoucher(voucher);
		try {
			Map<String, SqlObject> sqlValues = this.javaToSql(values, false);
			Serializable pk = this.db.newRow(this.newRowStmt, sqlValues, voucher).get(this.pkName).get(this.pkType);
			RowImpl newRow = new RowImpl(this, values, pk);
			this.pkRowMap.put(pk, newRow);
			this.myRows.put(newRow, newRow);
			this.myModule.add2created(this, newRow);
			return newRow;
		} catch (CasketException e) {
			this.myModule.rollback(e, voucher);
			return null; // never reached!
		}
	}

	@Override
	public synchronized void updateRow(Row row, Map<String, ? extends Serializable> values, Object voucher) throws CasketException {
		RowImpl theRow = this.myRows.get(row);
		this.checkArgs(theRow, values);
		this.myModule.checkVoucher(voucher);
		try {
			Map<String, SqlObject> sqlValues = this.javaToSql(values, true);
			PreCompiledStatement updateRowStmt = this.db.mkUpdateRowStmt(this.dbTab, values.keySet());
			this.db.updateRow(updateRowStmt, this.mkSqlObject(this.pkName, theRow.getPk(this.pkType)), sqlValues, voucher);
			for (Entry<String, ? extends Serializable> entry : values.entrySet())
				theRow.setValue(entry.getKey(), entry.getValue());
			this.myModule.add2changed(this, theRow);
		} catch (CasketException e) {
			this.myModule.rollback(e, voucher);
		}
	}

	@Override
	public synchronized void deleteRow(Row row, Object voucher) throws CasketException {
		RowImpl theRow = this.myRows.get(row);
		this.checkArgs(theRow);
		this.myModule.checkVoucher(voucher);
		try {
			Map<SqlArg, SqlObject> args = new HashMap<>();
			args.put(this.pkArg, this.objFac.mkSqlObject(this.dbTab.storageClass(this.pkName), theRow.getPk(this.pkType)));
			List<SqlObject> keys = this.db.delete(this.deleteRowStmt, args, voucher);
			if ((keys == null) || (keys.size() != 1) || (this.pkRowMap.get(keys.get(0).get(this.pkType)) != theRow))
				throw CasketError.UNEXPECTED_DELETE.build();
			theRow.delete();
			this.myModule.add2deleted(this, theRow);
		} catch (CasketException e) {
			this.myModule.rollback(e, voucher);

		}
	}

	private List<Row> searchRows(HashSet<SqlArg> argSet, Map<SqlArg, SqlObject> args, Object voucher) throws CasketException {
		PreCompiledStatement selectRowStmt = this.db.mkSelectStmt(this.dbTab, argSet, SqlArg.OP.AND);
		List<Row> selectedRows = new ArrayList<>();
		for (Map<String, SqlObject> RowSqlValues : this.db.select(selectRowStmt, args, voucher)) {
			RowImpl row = this.mkRow(RowSqlValues, null);
			Object pk = row.getPk(this.pkType);
			selectedRows.add(this.updateMaps(row, pk, voucher));
		}
		return selectedRows;

	}

	@Override
	public synchronized List<Row> allRows(Object voucher) throws CasketException {
		List<Map<String, SqlObject>> allPks = this.db.select(this.selectAllPKsStmt, new HashMap<>(), voucher);
		Set<Serializable> pkObjects = new HashSet<>();
		allPks.forEach(pkPair -> pkObjects.add(pkPair.get(this.pkName).get(this.pkType)));
		for (Serializable pk : pkObjects) {
			Row row = this.pkRowMap.get(pk);
			if ((row != null) && !row.isDirty())
				continue;
			Map<SqlArg, SqlObject> args = new HashMap<>();
			args.put(this.pkArg, this.objFac.mkSqlObject(this.dbTab.storageClass(this.pkName), pk));
			Map<String, SqlObject> rowSqlValues = this.db.select(this.selectRowByPkStmt, args, voucher).get(0);
			this.updateMaps(this.mkRow(rowSqlValues, pk), pk, voucher);
		}
		return new ArrayList<>(this.pkRowMap.values());

	}

	private RowImpl updateMaps(RowImpl row, Object pk, Object voucher) throws CasketException {
		RowImpl oldRow = this.pkRowMap.get(pk);
		if (oldRow == null) {
			this.pkRowMap.put(pk, row);
			this.myRows.put(row, row);
			return row;
		}
		if ((voucher != null) && oldRow.isDirty())
			this.reloadRow(oldRow, voucher);
		return oldRow;
	}

	@Override
	public synchronized List<Row> searchRows(Set<Exp> cmpDef, Object voucher) throws CasketException {
		Objects.requireNonNull(cmpDef);
		HashSet<SqlArg> argSet = new HashSet<>();
		Map<SqlArg, SqlObject> args = new HashMap<>();
		for (Exp exp : cmpDef) {
			CompareObjectImpl cmpObj = this.mkCmpObject(exp.columnName(), exp.value(), exp.op());
			argSet.add(cmpObj.sqlArg);
			args.put(cmpObj.sqlArg, cmpObj.sqlObj);
		}
		return this.searchRows(argSet, args, voucher);
	}

	private <T extends Serializable> CompareObjectImpl mkCmpObject(String column, T value, TabCMP op) throws CasketException {
		Util.objectsNotNull(column, value, op);
		SqlObject sqlObj = this.objFac.mkSqlObject(this.dbTab.storageClass(column), value);
		SqlArg sqlArg = this.db.mkSqlArg(this.dbTab, column, CompareObjectImpl.map.get(op));
		return new CompareObjectImpl(this, column, sqlArg, sqlObj);

	}

	private <T extends Serializable> RowImpl mkRow(Map<String, SqlObject> rowSqlValues, T pk) {
		Map<String, Serializable> rowValues = new HashMap<>();
		rowSqlValues.forEach((col, sqlObj) -> rowValues.put(col, sqlObj.get(this.getColumnType(col))));
		return new RowImpl(this, rowValues, pk == null ? rowValues.get(this.pkName) : pk);
	}

	@Override
	public void reloadRow(Row row, Object voucher) throws CasketException {
		RowImpl theRow = this.myRows.get(row);
		Objects.requireNonNull(theRow);
		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(this.pkArg, this.objFac.mkSqlObject(this.dbTab.storageClass(this.pkName), row.getPk(this.pkType)));
		Map<String, SqlObject> rowSqlValues = this.db.select(this.selectRowByPkStmt, args, voucher).get(0);
		for (Entry<String, SqlObject> entry : rowSqlValues.entrySet()) {
			String col = entry.getKey();
			if (col.equals(this.pkName))
				continue;
			theRow.setValue(col, entry.getValue().get(this.getColumnType(col)));
		}

	}

	protected Class<? extends Serializable> getColumnType(String column) {
		return this.rowSignature.get(column);
	}

	protected Set<String> allColumns() {
		return this.rowSignature.keySet();
	}

	protected String pkName() {
		return this.pkName;
	}

	private <T extends Serializable> SqlObject mkSqlObject(String column, T value) {
		try {
			return this.objFac.mkSqlObject(this.dbTab.storageClass(column), value);
		} catch (CasketException e) {
			this.exc = e;
		}
		return null;
	}

	private void throwIfError() throws CasketException {
		if (this.exc != null) {
			CasketException e = this.exc;
			this.exc = null;
			throw e;
		}
	}

	private void checkArgs(Object... args) throws CasketException {
		if (this.closed)
			throw CasketError.TABLE_CLOSED.build();
		Util.objectsNotNull(args);
	}

	private Map<String, SqlObject> javaToSql(Map<String, ? extends Serializable> values, boolean valuesOnly) throws CasketException {
		Map<String, SqlObject> sqlValues = new HashMap<>();
		if (valuesOnly)
			values.keySet().forEach(column -> sqlValues.put(column, this.mkSqlObject(column, values.get(column))));
		else
			this.dbTab.columnNames().forEach(column -> sqlValues.put(column, this.mkSqlObject(column, values.get(column))));
		this.throwIfError();
		return sqlValues;
	}

	protected void rollback(Set<RowImpl> rows) {
		if (rows == null)
			return;
		try {
			for (Row row : rows) {
				RowImpl theRow = this.myRows.remove(row);
				Object pk = theRow == null ? null : theRow.getPk(this.pkType);
				if (pk == null)
					continue;
				this.pkRowMap.remove(pk);
				if (this.isAutoIncrementedPK)
					theRow.resetPK();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
