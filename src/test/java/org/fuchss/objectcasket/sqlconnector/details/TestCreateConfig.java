package org.fuchss.objectcasket.sqlconnector.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateConfig {

	@Test
	void editConfig() throws CasketException {
		this.editConfig(DB.SQLITE);
		this.editConfig(DB.H2);
	}

	void editConfig(DB dialect) throws CasketException {
		DBConfiguration config = SqlPort.SQL_PORT.sqlDatabaseFactory().createConfiguration();
		Assertions.assertNotNull(config);

		boolean complete = config.isComplete();
		Assertions.assertFalse(complete);

		boolean driverResult = config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		Assertions.assertTrue(driverResult);

		complete = config.isComplete();
		Assertions.assertFalse(complete);

		this.editUri(config);

		complete = config.isComplete();
		Assertions.assertFalse(complete);

		this.editUser(config);
		complete = config.isComplete();
		Assertions.assertFalse(complete);

		this.editPWD(config);
		complete = config.isComplete();
		Assertions.assertTrue(complete);

		this.editFlags(config);
		complete = config.isComplete();
		Assertions.assertTrue(complete);

		boolean inUse = config.isInUse();
		Assertions.assertFalse(inUse);

	}

	private void editUri(DBConfiguration config) throws CasketException {
		boolean uriResult = config.setUri("xyz.db");
		Assertions.assertTrue(uriResult);

		uriResult = config.setUri("xyz.db");
		Assertions.assertFalse(uriResult);

		uriResult = config.setUri("xyz2.db");
		Assertions.assertTrue(uriResult);
	}

	private void editUser(DBConfiguration config) throws CasketException {
		boolean userResult = config.setUser("user");
		Assertions.assertTrue(userResult);

		userResult = config.setUser("user");
		Assertions.assertFalse(userResult);

		userResult = config.setUser("user2");
		Assertions.assertTrue(userResult);
	}

	private void editPWD(DBConfiguration config) throws CasketException {
		boolean pwdResult = config.setPassword("pwd");
		Assertions.assertTrue(pwdResult);

		pwdResult = config.setPassword("pwd");
		Assertions.assertFalse(pwdResult);

		pwdResult = config.setPassword("pwd2");
		Assertions.assertTrue(pwdResult);
	}

	private void editFlags(DBConfiguration config) throws CasketException {

		boolean flagResult = config.setFlag(DBConfiguration.Flag.CREATE);
		Assertions.assertTrue(flagResult);

		flagResult = config.setFlag(DBConfiguration.Flag.CREATE);
		Assertions.assertFalse(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.MODIFY);
		Assertions.assertFalse(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.CREATE, DBConfiguration.Flag.MODIFY);
		Assertions.assertFalse(flagResult);

		flagResult = config.setFlag(DBConfiguration.Flag.MODIFY);
		Assertions.assertTrue(flagResult);

		flagResult = config.setFlag(DBConfiguration.Flag.MODIFY);
		Assertions.assertFalse(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.MODIFY);
		Assertions.assertTrue(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.CREATE);
		Assertions.assertTrue(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.CREATE, DBConfiguration.Flag.MODIFY);
		Assertions.assertTrue(flagResult);

		flagResult = config.containsAll(DBConfiguration.Flag.MODIFY, DBConfiguration.Flag.CREATE);
		Assertions.assertTrue(flagResult);

		flagResult = config.containsAll();
		Assertions.assertTrue(flagResult);

		flagResult = config.removeFlag(DBConfiguration.Flag.CREATE);
		Assertions.assertTrue(flagResult);

		flagResult = config.removeFlag(DBConfiguration.Flag.CREATE);
		Assertions.assertFalse(flagResult);

		flagResult = config.removeFlag(DBConfiguration.Flag.CREATE, DBConfiguration.Flag.MODIFY);
		Assertions.assertTrue(flagResult);

		flagResult = config.removeFlag(DBConfiguration.Flag.CREATE, DBConfiguration.Flag.MODIFY);
		Assertions.assertFalse(flagResult);

		flagResult = config.setFlag(DBConfiguration.Flag.MODIFY);
		Assertions.assertTrue(flagResult);

		flagResult = config.setFlag(DBConfiguration.Flag.CREATE);
		Assertions.assertTrue(flagResult);
	}

}
