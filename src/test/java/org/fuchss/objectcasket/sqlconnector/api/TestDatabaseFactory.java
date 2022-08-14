package org.fuchss.objectcasket.sqlconnector.api;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TestDatabaseFactory {

	SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
	SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

	@Test
	void createConfig() throws CasketException {

		DBConfiguration config = this.dbFac.createConfiguration();
		Assertions.assertNotNull(config);

	}

	@Test
	void createDatabase() throws CasketException, IOException {
		this.createDatabase(DB.SQLITE);
		this.createDatabase(DB.H2);
	}

	private void createDatabase(DB dialect) throws CasketException, IOException {

		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, dialect);
			SqlDatabase db = this.dbFac.openDatabase(config);
			Assertions.assertNotNull(db);
			this.dbFac.closeDatabase(db);
		} finally {
			Utility.deleteFile(dbFile);
		}
	}

}
