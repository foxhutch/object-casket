package org.fuchss.objectcasket;

import java.io.File;
import java.sql.Driver;

import org.fuchss.objectcasket.justDoNotCrash.types.A;
import org.fuchss.objectcasket.justDoNotCrash.types.AB;
import org.fuchss.objectcasket.justDoNotCrash.types.ABC;
import org.fuchss.objectcasket.justDoNotCrash.types.XY;
import org.fuchss.objectcasket.justDoNotCrash.types.XY1;
import org.fuchss.objectcasket.justDoNotCrash.types.XY2;
import org.fuchss.objectcasket.justDoNotCrash.types.XY3;
import org.fuchss.objectcasket.justDoNotCrash.types.XY4;
import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ObjectCasketPort;
import org.fuchss.objectcasket.port.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPersist {
	private static final Class<? extends Driver> DRIVER = org.sqlite.JDBC.class;
	private static final String DRIVERNAME = "jdbc:sqlite:";

	private ObjectCasketPort storePort;

	private File file;
	private Session session;

	@Before
	public void setUpConnection() throws Exception {
		this.storePort = ObjectCasketFactory.FACTORY.ObjectCasketPort();

		Configuration config = this.storePort.configurationBuilder().createConfiguration();
		this.file = File.createTempFile("TestPersist", "db");
		config.setDriver(TestPersist.DRIVER, TestPersist.DRIVERNAME);
		config.setUri(this.file.toURI().getPath());
		config.setUser("");
		config.setPasswd("");
		config.setFlag(Configuration.Flag.MODIFY, Configuration.Flag.CREATE);
		this.session = this.storePort.sessionManager().session(config);

	}

	@After
	public void cleanup() throws Exception {
		this.storePort.sessionManager().terminate(this.session);
		this.file.delete();
	}

	@Test
	public void checkPersistNotFail() throws Exception {

		this.session.declareClass(XY.class, AB.class, ABC.class, XY1.class, XY2.class, XY3.class, XY4.class, A.class);
		this.session.open();

		XY xy = new XY();
		xy.column = 2;
		this.session.persist(xy);

		AB ab = new AB();
		ab.column = xy;
		this.session.persist(ab);

		AB ab2 = new AB();
		ab2.column = new XY();
		ab2.column.column = 3;
		this.session.persist(ab2);

		ABC abc = new ABC();
		this.session.persist(abc);

		xy.column = 11;
		this.session.persist(xy);

		A a = new A();

		a.columnA = 10;
		a.columnB = 20;
		a.setA(30);
		this.session.persist(a);

		XY2 objXY2a = new XY2();
		objXY2a.column = 10;
		objXY2a.column2 = "objXY2a";

		XY2 objXY2b = new XY2();
		objXY2b.column = 20;
		objXY2b.column2 = "objXY2b";
		objXY2b.column3 = objXY2a;

		XY1 objXY1a = new XY1();
		objXY1a.column = 100;
		objXY1a.column2 = "objXY1a";
		objXY1a.column3 = objXY2a;
		objXY1a.column4 = objXY2b;

		XY1 objXY1b = new XY1();
		objXY1b.column = 200;
		objXY1b.column2 = "objXY1b";
		objXY1b.column3 = objXY2b;
		objXY1b.column4 = objXY2b;

		XY1 objXY1c = new XY1();
		objXY1c.column = 300;
		objXY1c.column2 = "objXY1c";
		objXY1c.column3 = objXY2b;
		objXY1c.column4 = objXY2b;

		this.session.persist(objXY1c);
		this.session.persist(objXY1b);
		this.session.persist(objXY1a);
		this.session.persist(objXY2b);
		this.session.persist(objXY2a);

	}

}