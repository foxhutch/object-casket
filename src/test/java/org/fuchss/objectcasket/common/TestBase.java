package org.fuchss.objectcasket.common;

import java.io.File;
import java.nio.file.Files;
import java.sql.Driver;

import org.fuchss.objectcasket.ObjectCasketFactory;
import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.ObjectCasketPort;
import org.junit.After;
import org.junit.Before;

public abstract class TestBase {
	private static final Class<? extends Driver> DRIVER = org.sqlite.JDBC.class;
	private static final String DRIVERNAME = "jdbc:sqlite:";

	protected ObjectCasketPort storePort;
	protected File dbFile;

	@Before
	public void setUpConnection() throws Exception {
		this.storePort = ObjectCasketFactory.FACTORY.ObjectCasketPort();
		this.dbFile = File.createTempFile("TestsDB" + this.getClass().getSimpleName(), ".db");
		Files.deleteIfExists(this.dbFile.toPath());
		System.out.println("DB is " + this.dbFile.getPath());
	}

	protected Configuration config() throws ObjectCasketException {
		Configuration config = this.storePort.configurationBuilder().createConfiguration();
		config.setDriver(TestBase.DRIVER, TestBase.DRIVERNAME);
		config.setUri(this.dbFile.toURI().getPath());
		config.setUser("");
		config.setPasswd("");
		config.setFlag(Configuration.Flag.MODIFY, Configuration.Flag.CREATE);
		return config;
	}

	@After
	public void cleanup() throws Exception {
		this.dbFile.delete();
		System.out.println("DB " + this.dbFile.getPath() + " deleted");
	}

}
