package org.fuchss.objectcasket.sqlconnector.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;

class PrepareTable {

	SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
	SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

	static final String PKname = "PK";
	static final String TextCol = "text";
	static final String TableName = "Table";

	DBConfiguration config = null;
	File dbFile = null;
	SqlDatabase db = null;
	TableAssignment table = null;

	SqlObject pk = null;

	protected TableAssignment initTable(String tabName, String keyName, DB driver) throws CasketException, IOException {

		this.initTable(driver);

		TableAssignment tab;

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, null);
		pkCol.setFlag(Flag.PRIMARY_KEY);
		columns.put(keyName, pkCol);

		SqlColumnSignature textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, "abc");
		columns.put(TextCol, textCol);

		tab = this.db.createTable(tabName, columns);

		PreCompiledStatement preStat = this.db.mkNewRowStmt(tab);

		Map<String, SqlObject> values = new HashMap<>();

		Object voucher = this.db.beginTransaction(true);
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "123"));
		values.put(keyName, this.factory.mkSqlObject(StorageClass.TEXT, "key1"));
		this.db.newRow(preStat, values, voucher);

		values.clear();
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "456"));
		values.put(keyName, this.factory.mkSqlObject(StorageClass.TEXT, "key2"));
		this.db.newRow(preStat, values, voucher);

		values.clear();
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "789"));
		values.put(keyName, this.factory.mkSqlObject(StorageClass.TEXT, "key3"));
		this.db.newRow(preStat, values, voucher);

		this.db.endTransaction(voucher);

		return tab;
	}

	protected void initTable(DB driver) throws CasketException, IOException {

		this.dbFile = Utility.createFile(this);
		this.config = Utility.createDBConfig(this.dbFile, driver);
		this.db = this.dbFac.openDatabase(this.config);

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
		pkCol.setFlag(Flag.PRIMARY_KEY);
		pkCol.setFlag(Flag.AUTOINCREMENT);
		// pkCol.setFlag(Flag.NOT_NULL);
		columns.put(PKname, pkCol);

		SqlColumnSignature textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, "abc");
		columns.put(TextCol, textCol);

		this.table = this.db.createTable(TableName, columns);

		PreCompiledStatement preStat = this.db.mkNewRowStmt(this.table);

		Map<String, SqlObject> values = new HashMap<>();

		Object voucher = this.db.beginTransaction(true);
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "123"));
		this.pk = this.db.newRow(preStat, values, voucher).get(PKname);
		Integer ipk = this.pk.get(Integer.class);
		Assertions.assertNotNull(ipk);

		values.clear();
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "456"));
		ipk = this.db.newRow(preStat, values, voucher).get(PKname).get(Integer.class);
		Assertions.assertNotNull(ipk);

		values.clear();
		values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "789"));
		ipk = this.db.newRow(preStat, values, voucher).get(PKname).get(Integer.class);
		Assertions.assertNotNull(ipk);

		this.db.endTransaction(voucher);

	}

	protected void delTable() throws CasketException, IOException {

		this.dbFac.closeDatabase(this.db);
		Utility.deleteFile(this.dbFile);
		this.table = null;
	}

	/*
	 * protected void delConfig() throws CasketException, IOException {
	 * Utility.deleteFile(this.dbFile); }
	 */
}
