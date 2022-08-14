package org.fuchss.objectcasket.sqlconnector.details;

import java.io.File;
import java.io.IOException;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestSecurity {

	@Test
	void userAndPassword() throws IOException, CasketException {

		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, DB.H2);
			Assertions.assertTrue(config.setUser("User"));
			Assertions.assertTrue(config.setPassword("secure"));
			SqlDatabase db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			Assertions.assertNotNull(db);
			db.createTable(Utility.TABLE_NAME, Utility.createColumns(Utility.PK_NAME, (byte) 0));
			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);

			Assertions.assertTrue(config.setUser("OtherUser"));
			CasketException exc = null;
			try {
				db = null;
				db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			} catch (CasketException e) {
				exc = e;
			}
			Assertions.assertNotNull(exc);
			Assertions.assertNull(db);

			Assertions.assertTrue(config.setUser("User"));
			Assertions.assertTrue(config.setPassword("very_secure"));

			exc = null;
			try {
				db = null;
				db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			} catch (CasketException e) {
				exc = e;
			}
			Assertions.assertNotNull(exc);
			Assertions.assertNull(db);

			Assertions.assertTrue(config.setPassword("secure"));

			exc = null;
			try {
				db = null;
				db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			} catch (CasketException e) {
				exc = e;
			}
			Assertions.assertNull(exc);
			Assertions.assertNotNull(db);
			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);

		} finally {
			Utility.deleteFile(dbFile);
		}
	}
}
