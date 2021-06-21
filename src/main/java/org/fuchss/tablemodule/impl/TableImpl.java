package org.fuchss.tablemodule.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlArg;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.RowPrototype;
import org.fuchss.tablemodule.port.Table;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.Transaction;

public class TableImpl implements Table {

	private SqlDatabase database;
	private SqlObjectFactory sqlObjectFactory;

	private String name;
	private Map<String, SqlPrototype> protoTypes;

	private Map<Row, RowImpl> rowMap = new HashMap<>();
	private Map<SqlObject, RowImpl> primaryKeyToRowMap = new HashMap<>();
	private Map<RowImpl, SqlObject> rowToPrimaryKeyMap = new HashMap<>();

	private String pkName;
	private int sequenzNumber;
	private boolean autoincrement = false;
	List<Integer> unusedSequenzNumbers = null;

	TableImpl(SqlDatabase database, SqlObjectFactory sqlObjectFactory) {
		this.database = database;
		this.sqlObjectFactory = sqlObjectFactory;
	}

	void setName(String name) {
		this.name = name;
	}

	void setSequenzNumber(int sequenzNumber) {
		this.sequenzNumber = sequenzNumber;
	}

	void setProtoTypes(Map<String, SqlPrototype> protoTypes, String pkName) throws TableModuleException {
		if ((pkName == null) || !protoTypes.containsKey(pkName)) {
			TableException.Error.NoPrimaryKey.build((this.name == null) ? this.toString() : this.name);
		}
		this.protoTypes = protoTypes;
		this.pkName = pkName;
		this.autoincrement = this.protoTypes.get(this.pkName).isAutoIncrementedPrimaryKey();
	}

	@Override
	public Row mkRow(Transaction transaction) throws TableModuleException {
		if (!((TransactionImpl) transaction).lockIfExists()) {
			TableException.Error.WrongTransaction.build();
		}
		this.calcSequenzNumber();
		try {
			RowImpl row = new RowImpl(this, this.pkName);
			if (this.autoincrement) {
				row.set(this.pkName, ++this.sequenzNumber);
			}
			this.rowMap.put(row, row);
			((TransactionImpl) transaction).addToCreateSet(row, this);
			return row;
		} finally {
			((TransactionImpl) transaction).unlock();
		}
	}

	private void calcSequenzNumber() {
		if (this.unusedSequenzNumbers == null) {
			return;
		}
		Collections.sort(this.unusedSequenzNumbers, Collections.reverseOrder());
		for (Integer number : this.unusedSequenzNumbers) {
			if (this.sequenzNumber == number) {
				this.sequenzNumber--;
			} else {
				break;
			}
		}
		this.unusedSequenzNumbers = null;
	}

	void undoMkRow(RowImpl row, SqlObject pk) {
		this.rowMap.remove(row);
		row.setDeleted(true);
		if (this.protoTypes.get(this.pkName).isAutoIncrementedPrimaryKey()) {
			if (this.unusedSequenzNumbers == null) {
				this.unusedSequenzNumbers = new ArrayList<>();
			}
			this.unusedSequenzNumbers.add(pk.get(Integer.class));
		}
	}

	@Override
	public RowPrototype mkRowPrototype() {
		return new RowPrototypeImpl(this);
	}

	@Override
	public void delete(Transaction transaction, Row row) throws TableModuleException {
		RowImpl rowImpl = this.checkTransactionAndRow_LockIfPossible(transaction, row);
		try {
			rowImpl.prepareDelet();
			((TransactionImpl) transaction).addToDeleteSet(rowImpl, this);
		} finally {
			((TransactionImpl) transaction).unlock();
		}
	}

	private RowImpl checkTransactionAndRow_LockIfPossible(Transaction transaction, Row row) throws TableModuleException {
		RowImpl rowImpl = this.rowMap.get(row);
		if (rowImpl == null) {
			TableException.Error.UnknownRow.build(row.toString(), this.name);
		}
		if (!((TransactionImpl) transaction).lockIfExists()) {
			TableException.Error.WrongTransaction.build();
		}
		return rowImpl;
	}

	void saveOrUpdate(RowImpl row) throws ConnectorException, SQLException {
		SqlObject pk = this.rowToPrimaryKeyMap.get(row);
		if (pk != null) {
			this.updateRow(row, pk);
		} else {
			this.saveRow(row);
		}
	}

	private void updateRow(RowImpl row, SqlObject pk) throws ConnectorException {
		List<SqlArg> args = new ArrayList<>();
		row.newValues().forEach((column, sqlObj) -> args.add(new SqlArg(column, SqlArg.CMP.EQUAL, sqlObj)));
		this.database.updateRow(this.name, args, this.pkName, pk);
	}

	private void saveRow(RowImpl row) throws ConnectorException, SQLException {
		this.database.newRow(this.name, row.newValues());
		ResultSet resultSet = this.database.query(row.getUnsave(this.pkName), this.name, this.pkName);
		this.addValues(row, resultSet);
		this.database.closeStatement(resultSet);
	}

	private void addValues(RowImpl row, ResultSet resultSet) throws SQLException {
		for (String name : this.protoTypes.keySet()) {
			Object obj = resultSet.getObject(name);
			if (obj != null) {
				row.setUnsave(name, obj, true);
			}
		}
	}

	void remove(RowImpl row) throws ConnectorException {

		if (this.rowToPrimaryKeyMap.containsKey(row)) {
			SqlObject pkObj = this.rowToPrimaryKeyMap.get(row);
			this.database.deleteRow(this.name, this.pkName, pkObj);
		}
	}

	void clearRow(RowImpl row, SqlObject pk) {
		this.rowMap.remove(row);
		row.setDeleted(true);
		this.primaryKeyToRowMap.remove(pk);
		this.rowToPrimaryKeyMap.remove(row);
	}

	void createRow(RowImpl row, SqlObject pk) {
		if (this.primaryKeyToRowMap.containsKey(pk)) {
			return;
		}
		this.rowToPrimaryKeyMap.put(row, pk);
		this.primaryKeyToRowMap.put(pk, row);
		if (this.autoincrement) {
			int pkNo = pk.get(Integer.TYPE);
			this.sequenzNumber = (this.sequenzNumber < pkNo) ? pkNo : this.sequenzNumber;
		}
	}

	SqlObject createSqlObject(String column, Object obj, boolean fromSQL) throws TableModuleException {
		SqlPrototype prototype = this.protoTypes.get(column);
		if (prototype == null) {
			TableException.Error.UnknownColulmnName.build(column, this.name);
		}
		SqlObject sqlObj = null;
		try {
			if (fromSQL)
				sqlObj = this.sqlObjectFactory.mkSqlObjectFromSQL(prototype.getType(), obj);
			else
				sqlObj = this.sqlObjectFactory.mkSqlObject(prototype.getType(), obj); //////////// from java
		} catch (ConnectorException e) {
			TableModuleException.build(e);
		}
		return sqlObj;
	}

	@Override
	public List<Row> allRows() throws TableModuleException {
		List<RowImpl> rows = this.allRows(null);
		return new ArrayList<>(rows);
	}

	@Override
	public List<Row> allRowsByPrototype(RowPrototype prototype) throws TableModuleException {
		try {
			RowPrototypeImpl protoImpl = (RowPrototypeImpl) prototype;
			List<SqlArg> args = protoImpl.mkArgs();
			List<RowImpl> rows = this.allRows(args);
			return new ArrayList<>(rows);
		} catch (Exception e) {
			TableModuleException.build(e);
		}
		return null;
	}

	private List<RowImpl> allRows(List<SqlArg> args) throws TableModuleException {
		List<RowImpl> rows = this.newRows(args); // oops
		for (RowImpl row : rows) {
			if (this.rowToPrimaryKeyMap.containsKey(row)) {
				continue;
			}
			SqlObject pkObj = row.getObject(this.pkName);
			this.primaryKeyToRowMap.put(pkObj, row);
			this.rowToPrimaryKeyMap.put(row, pkObj);
			this.rowMap.put(row, row);
		}
		return rows;
	}

	private List<RowImpl> newRows(List<SqlArg> args) throws TableModuleException {
		try {
			List<RowImpl> rows = new ArrayList<>();
			ResultSet resultSet = this.database.query(this.name, args);
			while (resultSet.next()) {
				RowImpl row = this.rowExists(resultSet);
				if (row == null) {
					row = new RowImpl(this, this.pkName);
					this.addValues(row, resultSet);
				}
				rows.add(row);
			}
			this.database.closeStatement(resultSet);
			return rows;
		} catch (ConnectorException | SQLException e) {
			TableModuleException.build(e);
		}
		return null;
	}

	private RowImpl rowExists(ResultSet resultSet) throws SQLException, TableModuleException {
		Object obj = resultSet.getObject(this.pkName);
		SqlObject pkObj = this.createSqlObject(this.pkName, obj, true);
		return this.primaryKeyToRowMap.get(pkObj);
	}

	@Override
	public String getPkName() {
		return this.pkName;
	}

	private static class TableException extends TableModuleException {

		private static final long serialVersionUID = 1L;

		private TableException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			NoPrimaryKey("To install a table or making a row one needs a primary key."), //
			UnknownRow("OOPS - unknown Row %s in table %s."), //
			WrongTransaction("Unknown or already finished transaction, use an othert one."), //
			UnknownColulmnName("Unknown column %s in table %s."); //

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws TableModuleException {
				TableModuleException.build(new TableException(this, arg));
			}

		}

	}

}
