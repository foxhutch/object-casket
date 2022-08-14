package org.fuchss.objectcasket.sqlconnector.details;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.Arrays;

class TestOpenAndCloseDatabase {

	@Test
	void createAndCloseDatabaseTest() throws IOException, CasketException {
		this.createAndCloseDatabase(DB.SQLITE);
		this.createAndCloseDatabase(DB.H2);
	}

	@Test
	void incompleteCreate() throws IOException, CasketException {
		this.incompleteCreate(DB.SQLITE);
		this.incompleteCreate(DB.H2);
	}

	@Test
	void editConfig() throws IOException, CasketException {
		this.editConfig(DB.SQLITE);
		this.editConfig(DB.H2);

	}

	private void createAndCloseDatabase(DB dialect) throws IOException, CasketException {
		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = this.configWithOut(dbFile, dialect);
			SqlDatabase db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			Assertions.assertNotNull(db);
			SqlDatabase db2 = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			Assertions.assertNotNull(db2);
			Assertions.assertEquals(db, db2);

			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);
			CasketException exception = Assertions.assertThrows(CasketException.class, () -> {
				SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);
			});

			Assertions.assertEquals(exception.getMessage(), CasketError.UNKNOWN_DATABASE.build().getMessage());
		} finally {
			Utility.deleteFile(dbFile);
		}

	}

	private void incompleteCreate(DB dialect) throws IOException, CasketException {
		File dbFile = Utility.createFile(this);
		try {
			for (Param p : Param.values()) {
				DBConfiguration config = this.configWithOut(dbFile, dialect, p);
				CasketException exception = Assertions.assertThrows(CasketException.class, () -> {
					SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
				});
				Assertions.assertEquals(exception.getMessage(), CasketError.INCOMPLETE_CONFIGURATION.build().getMessage());
			}
		} finally {
			Utility.deleteFile(dbFile);
		}

	}

	private void editConfig(DB dialect) throws IOException, CasketException {
		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = this.configWithOut(dbFile, dialect);
			SqlDatabase db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);
			Assertions.assertNotNull(db);

			CasketException exception = Assertions.assertThrows(CasketException.class, () -> {
				config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			});
			Assertions.assertEquals(exception.getMessage(), CasketError.CONFIGURATION_IN_USE.build().getMessage());

			exception = Assertions.assertThrows(CasketException.class, () -> {
				config.setUri("xyz.db");
			});
			Assertions.assertEquals(exception.getMessage(), CasketError.CONFIGURATION_IN_USE.build().getMessage());
			exception = Assertions.assertThrows(CasketException.class, () -> {
				config.setUser("user");
			});
			Assertions.assertEquals(exception.getMessage(), CasketError.CONFIGURATION_IN_USE.build().getMessage());

			exception = Assertions.assertThrows(CasketException.class, () -> {
				config.setPassword("pwd");
			});
			Assertions.assertEquals(exception.getMessage(), CasketError.CONFIGURATION_IN_USE.build().getMessage());
			exception = Assertions.assertThrows(CasketException.class, () -> {
				config.setFlag(DBConfiguration.Flag.CREATE);
			});
			Assertions.assertEquals(exception.getMessage(), CasketError.CONFIGURATION_IN_USE.build().getMessage());

			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);
		} finally {
			Utility.deleteFile(dbFile);
		}

	}

	private DBConfiguration configWithOut(File dbFile, DB db, Param... p) throws IOException, CasketException {

		Class<? extends Driver> driver = Utility.dialectDriverMap.get(db);
		String driverPrefix = Utility.dialectUrlPrefixMap.get(db);
		SqlDialect dialect = Utility.dialectMap.get(db);

		DBConfiguration config = SqlPort.SQL_PORT.sqlDatabaseFactory().createConfiguration();
		if (!Arrays.asList(p).contains(Param.DRIVER))
			config.setDriver(driver, driverPrefix, dialect);
		if (!Arrays.asList(p).contains(Param.URI))
			config.setUri(dbFile.toURI().getPath());
		if (!Arrays.asList(p).contains(Param.USER))
			config.setUser("");
		if (!Arrays.asList(p).contains(Param.PASSWORD))
			config.setPassword("");
		config.setFlag(DBConfiguration.Flag.MODIFY, DBConfiguration.Flag.CREATE);
		return config;
	}

	private enum Param {
		DRIVER, URI, USER, PASSWORD;
	}

}
