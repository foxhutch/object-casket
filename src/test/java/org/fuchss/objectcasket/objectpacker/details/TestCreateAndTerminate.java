package org.fuchss.objectcasket.objectpacker.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.PackerPort;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TestCreateAndTerminate {

	@Test
	void createConfigTest() throws CasketException, IOException, InterruptedException {
		this.createConfigTest(DB.SQLITE);
		this.createConfigTest(DB.H2);
	}

	@Test
	void modifyConfigTest() throws CasketException, IOException, InterruptedException {
		this.modifyConfigTest(DB.SQLITE);
		this.modifyConfigTest(DB.H2);

	}

	private void createConfigTest(DB dialect) throws CasketException, IOException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		boolean ok = config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);
		Assertions.assertTrue(ok);

		Configuration config2 = manager.createConfiguration();
		config2.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config2.setUri(dbFile.toURI().getPath());
		config2.setUser("");
		config2.setPassword("");
		ok = config2.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.WRITE);
		Assertions.assertTrue(ok);

		Domain dom = manager.mkDomain(config);
		manager.finalizeDomain(dom);

		boolean error = false;
		try {
			dom = manager.mkDomain(config2);
		} catch (Exception e) {
			error = true;
		}
		Assertions.assertTrue(error);

		dom = manager.editDomain(config2);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		Assertions.assertNotNull(session);
		Session session1 = manager.session(config);
		Assertions.assertNotNull(session1);
		Session session2 = manager.session(config2);
		Assertions.assertNotNull(session2);

		Session session3 = manager.session(config2);
		Assertions.assertEquals(session2, session3);

		manager.terminate(session2);

		manager.terminateAll(config);

		error = false;
		try {
			manager.terminate(session);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		session = manager.session(config);
		session1 = manager.session(config);

		config2.setFlag(Configuration.Flag.SESSIONS);
		session2 = manager.session(config2);
		session3 = manager.session(config2);

		manager.terminate(session);

		manager.terminateAll();

		error = false;
		try {
			manager.terminate(session1);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		error = false;
		try {
			manager.terminate(session2);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		Utility.deleteFile(dbFile);
	}

	private void modifyConfigTest(DB dialect) throws CasketException, IOException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		boolean ok = config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);
		Assertions.assertTrue(ok);

		ok = config.setFlag(Configuration.Flag.CREATE);
		Assertions.assertTrue(ok);

		ok = config.removeFlag(Configuration.Flag.ALTER);
		Assertions.assertTrue(ok);

		ok = config.removeFlag(Configuration.Flag.ALTER);
		Assertions.assertFalse(ok);

		Domain dom = manager.mkDomain(config);
		manager.finalizeDomain(dom);
		Session session = manager.session(config);
		Assertions.assertNotNull(session);

		boolean error = false;
		try {
			ok = config.removeFlag(Configuration.Flag.ALTER);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		manager.terminate(session);

		Utility.deleteFile(dbFile);
	}

}
