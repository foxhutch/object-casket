package org.fuchss.objectcasket.sqlconnector.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TestCreateAndAssignTables {

	private SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
	private SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

	@Test
	void createDBandTableTest() throws IOException, CasketException {
		this.createDBandTable1(DB.SQLITE);
		this.createDBandTable1(DB.H2);
		this.createDBandTable(DB.SQLITE);
		this.createDBandTable(DB.H2);
	}

	private void createDBandTable1(DB driver) throws IOException, CasketException {

		File dbFile = Utility.createFile(this);
		SqlDatabase db = null;
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, driver);

			db = this.dbFac.openDatabase(config);
			SqlColumnSignature proto = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, null);
			proto.setFlag(SqlColumnSignature.Flag.PRIMARY_KEY);
			proto.setFlag(SqlColumnSignature.Flag.AUTOINCREMENT);

			SqlColumnSignature proto1 = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, null);

			Map<String, SqlColumnSignature> columns = new HashMap<>();
			columns.put(Utility.PK_NAME, proto);
			columns.put("TEST", proto1);

			db.createTable(Utility.TABLE_NAME, columns);
			this.dbFac.closeDatabase(db);

			db = this.dbFac.openDatabase(config);
			proto = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, null);
			proto.setFlag(SqlColumnSignature.Flag.PRIMARY_KEY);
			proto.setFlag(SqlColumnSignature.Flag.AUTOINCREMENT);

			proto1 = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, null);

			columns = new HashMap<>();
			columns.put(Utility.PK_NAME, proto);
			columns.put("TEST", proto1);

			TableAssignment view = db.createView(Utility.TABLE_NAME, columns);

			Assertions.assertTrue(view.columnNames().contains(Utility.PK_NAME));
			StorageClass sc = view.storageClass(Utility.PK_NAME);
			Assertions.assertEquals(proto.getType(), sc);

			Assertions.assertTrue(view.columnNames().contains("TEST"));
			sc = view.storageClass("TEST");
			Assertions.assertEquals(proto1.getType(), sc);

		} finally {
			this.dbFac.closeDatabase(db);
			Utility.deleteFile(dbFile);
		}
	}

	private void createDBandTable(DB driver) throws IOException, CasketException {

		File dbFile = Utility.createFile(this);
		SqlDatabase db = null;

		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, driver);

			db = this.dbFac.openDatabase(config);
			db.createTable(Utility.TABLE_NAME, Utility.createColumns(Utility.PK_NAME, (byte) 0));
			this.dbFac.closeDatabase(db);

			db = this.dbFac.openDatabase(config);

			Map<String, SqlColumnSignature> columns = new HashMap<>();
			Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);

			for (String col : allColumns.keySet()) {
				columns.put(col, allColumns.get(col));
				columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));

				TableAssignment view = db.createView(Utility.TABLE_NAME, columns);

				Assertions.assertTrue(view.columnNames().contains(col));
				StorageClass sc = view.storageClass(col);
				Assertions.assertEquals(allColumns.get(col).getType(), sc);

				columns.clear();
			}
		} finally {
			this.dbFac.closeDatabase(db);
			Utility.deleteFile(dbFile);
		}
	}

}
