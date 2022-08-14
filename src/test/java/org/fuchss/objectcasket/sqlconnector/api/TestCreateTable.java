package org.fuchss.objectcasket.sqlconnector.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateTable {

	SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
	SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

	File dbFile = null;

	@Test
	void createTableTest() throws CasketException, IOException {
		this.createTable(DB.H2);
		this.createTable(DB.SQLITE);
	}

	private void createTable(DB driver) throws CasketException, IOException {

		this.dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(this.dbFile, driver);

			SqlDatabase db = this.dbFac.openDatabase(config);

			Map<String, SqlColumnSignature> columns = new HashMap<>();

			SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
			pkCol.setFlag(Flag.PRIMARY_KEY);
			pkCol.setFlag(Flag.AUTOINCREMENT);
			// pkCol.setFlag(Flag.NOT_NULL);
			columns.put("PK", pkCol);

			SqlColumnSignature textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, "abcdefghijklmnopqrstuvwxyz");
			columns.put("text", textCol);

			Assertions.assertFalse(db.tableExists("Table"));
			TableAssignment table = db.createTable("Table", columns);
			Assertions.assertNotNull(table);
			Assertions.assertTrue(db.tableExists("Table"));

			this.dbFac.closeDatabase(db);
		} finally {
			Utility.deleteFile(this.dbFile);
		}
	}
}
