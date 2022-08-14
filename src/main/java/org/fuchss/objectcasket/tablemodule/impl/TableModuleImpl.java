package org.fuchss.objectcasket.tablemodule.impl;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

class TableModuleImpl implements TableModule {

	private final SqlObjectFactory objFac;
	private final SqlDatabase database;
	private ModuleConfigurationImpl config;

	private final Map<String, TableImpl> allViews = new HashMap<>();
	private final Map<String, TableImpl> allTables = new HashMap<>();

	private TransactionImpl transaction;

	private boolean close;

	protected TableModuleImpl(ModuleConfigurationImpl config, SqlDatabase db, SqlObjectFactory objFac) {
		this.database = db;
		this.objFac = objFac;
		this.config = config;
	}

	@Override
	public synchronized Table mkView(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException {
		Util.objectsNotNull(tableName, pkName, signature);
		if (this.allViews.containsKey(tableName))
			throw CasketError.TABLE_OR_VIEW_EXISTS.build();
		TableImpl table = this.caa(CMD.ASSIGN, tableName, pkName, signature, autoIncrement);
		this.allViews.put(tableName, table);
		return table;

	}

	@Override
	public synchronized Table createTable(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException {
		Util.objectsNotNull(tableName, pkName, signature);
		if (this.allTables.containsKey(tableName) || this.allViews.containsKey(tableName))
			throw CasketError.TABLE_OR_VIEW_EXISTS.build();
		TableImpl table = this.caa(CMD.CREATE, tableName, pkName, signature, autoIncrement);
		this.allTables.put(tableName, table);
		return table;

	}

	@Override
	public synchronized Table adjustTable(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException {
		Util.objectsNotNull(tableName, pkName, signature);
		if (this.allTables.containsKey(tableName) || this.allViews.containsKey(tableName))
			throw CasketError.TABLE_IN_USE.build();
		TableImpl table = this.caa(CMD.ADJUST, tableName, pkName, signature, autoIncrement);
		this.allTables.put(tableName, table);
		return table;

	}

	@Override
	public synchronized void dropTable(String tableName) throws CasketException {
		Util.objectsNotNull(tableName);

		this.database.dropTable(tableName);

	}

	@Override
	public synchronized boolean tableExists(String tableName) throws CasketException {
		Util.objectsNotNull(tableName);

		return this.database.tableExists(tableName);

	}

	private TableImpl caa(CMD cmd, String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException {
		Map<String, SqlColumnSignature> colSig = this.mkColSig(signature, pkName, autoIncrement);
		TableAssignment dbTab = null;
		dbTab = switch (cmd) {
			case CREATE -> this.database.createTable(tableName, colSig);
			case ASSIGN -> this.database.createView(tableName, colSig);
			case ADJUST -> this.database.adjustTable(tableName, colSig);
		};
		Map<String, Class<? extends Serializable>> pkSignature = new HashMap<>();
		pkSignature.put(pkName, signature.get(pkName));
		Map<String, SqlColumnSignature> pkDef = this.mkColSig(pkSignature, pkName, autoIncrement);
		TableAssignment pkTab = this.database.createView(tableName, pkDef);
		TableImpl tableImpl = new TableImpl(this, dbTab, pkTab, this.database, this.objFac);
		tableImpl.initSignature(signature, pkName, autoIncrement);
		return tableImpl;

	}

	private Map<String, SqlColumnSignature> mkColSig(Map<String, Class<? extends Serializable>> signature, String pkName, boolean autoIncrement) throws CasketException {
		Map<String, SqlColumnSignature> colMap = new HashMap<>();
		for (Entry<String, Class<? extends Serializable>> entry : signature.entrySet()) {
			Class<? extends Serializable> javaType = entry.getValue();
			String columnName = entry.getKey();
			StorageClass sClass = this.getStorageClass(javaType);
			if (sClass == null)
				throw CasketError.NO_SUITABLE_STORAGE_CLASS.build();
			SqlColumnSignature colSig = this.objFac.mkColumnSignature(sClass, javaType, null);
			if (columnName.equals(pkName)) {
				colSig.setFlag(SqlColumnSignature.Flag.PRIMARY_KEY);
				if (autoIncrement)
					colSig.setFlag(SqlColumnSignature.Flag.AUTOINCREMENT);
			}
			colMap.put(columnName, colSig);
		}
		return colMap;
	}

	private StorageClass getStorageClass(Class<? extends Serializable> javaType) {
		StorageClass storageClass = StorageClass.TYPE_MAP.get(javaType);
		if ((storageClass == null) && Serializable.class.isAssignableFrom(javaType))
			storageClass = StorageClass.BLOB;
		return storageClass;
	}

	protected SqlDatabase getDatabase() {
		return this.database;
	}

	protected ModuleConfigurationImpl config() {
		return this.config;
	}

	protected void close() throws CasketException {
		if (this.close)
			throw CasketError.ALREADY_CLOSED.build();
		this.close = true;
		for (TableImpl tab : this.allViews.values())
			tab.close();
		for (TableImpl tab : this.allTables.values())
			tab.close();
		this.config = null;
	}

	@Override
	public boolean isClosed() {
		return this.close;
	}

	@Override
	public synchronized Object beginTransaction() {

		Object obj = this.database.beginTransaction(true);
		this.transaction = new TransactionImpl(obj);
		return obj;

	}

	@Override
	public synchronized void endTransaction(Object voucher) throws CasketException {
		this.checkVoucher(voucher);
		try {
			this.database.endTransaction(voucher);
			this.transaction.done();
			this.transaction = null;
		} catch (Exception exc) {
			this.transaction.rollbackCreated(voucher);
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized void rollback(Object voucher) throws CasketException {
		this.checkVoucher(voucher);
		this.transaction.rollbackCreated(voucher);
		this.transaction = null;

		this.database.rollback(voucher);

	}

	protected void rollback(CasketException exc, Object voucher) throws CasketException {
		Objects.requireNonNull(exc);
		this.rollback(voucher);
		throw exc;
	}

	protected void add2created(TableImpl tableImpl, RowImpl newRow) {
		this.transaction.add2created(tableImpl, newRow);
	}

	protected void add2deleted(TableImpl tableImpl, RowImpl theRow) {
		this.transaction.add2deleted(tableImpl, theRow);
	}

	protected void add2changed(TableImpl tableImpl, RowImpl newRow) {
		this.transaction.add2changed(tableImpl, newRow);
	}

	protected void checkVoucher(Object voucher) throws CasketException {
		if ((this.transaction == null) || (this.transaction.voucher != voucher))
			throw CasketError.UNKNOWN_TRANSACTION.build();
	}

	private enum CMD {
		CREATE, ASSIGN, ADJUST
	}

}
