package org.fuchss.objectcasket.sqlconnector.examples;

import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

class TestSqlPort {

	@Test
	void usingTheSqlPort() throws IOException, InterruptedException {
		this.usingTheSqlPort(DB.SQLITE);
		this.usingTheSqlPort(DB.H2);
	}

	void usingTheSqlPort(DB dbVariant) throws IOException, InterruptedException {

		File dbFile = Utility.createFile(this);

		try {

			// 1 Get the database factory
			SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

			// 2 Create a configuration
			DBConfiguration config = dbFac.createConfiguration();
			config.setDriver(Utility.dialectDriverMap.get(dbVariant), Utility.dialectUrlPrefixMap.get(dbVariant), Utility.dialectMap.get(dbVariant));
			config.setUri(dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");
			config.setFlag(DBConfiguration.Flag.MODIFY, DBConfiguration.Flag.CREATE);

			// 3 Open the database
			SqlDatabase db = dbFac.openDatabase(config);

			// 4 Work on the database
			// 4.1 Create a table
			// Use the object factory.
			SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
			// Create a column definitions.
			Map<String, SqlColumnSignature> columns = new HashMap<>();

			// Define the primary key column
			SqlColumnSignature pkCol = factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
			pkCol.setFlag(Flag.PRIMARY_KEY);
			pkCol.setFlag(Flag.AUTOINCREMENT);

			// and a normal column.
			SqlColumnSignature textCol = factory.mkColumnSignature(StorageClass.TEXT, String.class, null);

			columns.put("PK", pkCol);
			columns.put("text", textCol);
			TableAssignment table = db.createTable("Table", columns); // Use db.createView(...) if the table exists.

			// 4.2 Insert some values
			Map<String, SqlObject> values = new HashMap<>();

			// Use the right pre-compiled statement.
			PreCompiledStatement preStat = db.mkNewRowStmt(table);

			// Open a transaction and wait if other transactions are running.
			Object voucher = db.beginTransaction(true);

			values.clear();
			values.put("text", factory.mkSqlObject(StorageClass.TEXT, "some text"));
			db.newRow(preStat, values, voucher);

			values.clear();
			values.put("text", factory.mkSqlObject(StorageClass.TEXT, "some other text"));
			db.newRow(preStat, values, voucher);

			// Maybe we need the primary key.
			values.clear();
			values.put("text", factory.mkSqlObject(StorageClass.TEXT, "some more text"));
			Map<String, SqlObject> pkMap = db.newRow(preStat, values, voucher);
			int pk = pkMap.get("PK").get(Integer.TYPE);

			Assertions.assertEquals(3, pk);

			// Close the transaction and also the pre-compiled statement.
			db.endTransaction(voucher);
			preStat.close();

			// 4.3 Select some values
			// We need a select statement and a set of proper arguments.
			SqlArg arg1 = db.mkSqlArg(table, "PK", CMP.GREATER);
			SqlArg arg2 = db.mkSqlArg(table, "PK", CMP.LESS);
			Set<SqlArg> argSet = new HashSet<>();
			argSet.add(arg1);
			argSet.add(arg2);
			preStat = db.mkSelectStmt(table, argSet, OP.AND);

			Map<SqlArg, SqlObject> argMap = new HashMap<>();
			argMap.put(arg1, factory.mkSqlObject(StorageClass.INTEGER, 1)); // PK should be greater than 1
			argMap.put(arg2, factory.mkSqlObject(StorageClass.INTEGER, 3)); // PK should be less than 3

			// Open a transaction and return null if other transactions are running.

			while ((voucher = db.beginTransaction(false)) == null)
				Assertions.assertTrue(false);

			List<Map<String, SqlObject>> result = db.select(preStat, argMap, voucher);

			// Close the transaction and also the pre-compiled statement.
			db.endTransaction(voucher);
			preStat.close();

			// Now, work with the result.
			Assertions.assertEquals(1, result.size());
			Assertions.assertEquals("some other text", result.get(0).get("text").get(String.class));

			// 5 Finally close the database

			dbFac.closeDatabase(db);

			// Thats all. Try also to delete a row, to update a row, ...

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utility.deleteFile(dbFile);
		}
	}

}
