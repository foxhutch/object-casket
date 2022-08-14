package org.fuchss.objectcasket.objectpacker.api;

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

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

class TestRead {

	@Test
	void readTest() throws IOException, CasketException, NoSuchFieldException, SecurityException, InterruptedException {
		this.readTest(DB.SQLITE);
		this.readTest(DB.H2);
	}

	private void readTest(DB dialect) throws IOException, CasketException, NoSuchFieldException, SecurityException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestClassRead.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);

		session.declareClass(TestClassRead.class);

		TestClassRead[] a = new TestClassRead[] { new TestClassRead(10, "ABC10"), new TestClassRead(20, "ABC20"), new TestClassRead(30, "ABC30"), new TestClassRead(40, "ABC40") };

		session.beginTransaction();
		session.persist(a[0]);
		session.persist(a[1]);
		session.persist(a[2]);
		session.persist(a[3]);
		session.endTransaction();

		session.beginTransaction();
		Set<TestClassRead> objs = session.getAllObjects(TestClassRead.class);
		session.endTransaction();

		Assertions.assertEquals(objs.size(), a.length);
		for (TestClassRead obj : a) {
			Assertions.assertTrue(objs.contains(obj));
		}

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestClassRead.class);

		session.beginTransaction();
		objs = session.getAllObjects(TestClassRead.class);
		session.endTransaction();
		Assertions.assertEquals(objs.size(), a.length);
		Set<Integer> x = new HashSet<>();
		Set<String> y = new HashSet<>();
		Set<Integer> pk = new HashSet<>();
		for (TestClassRead test : objs) {
			x.add(test.getX());
			y.add(test.getY());
			pk.add(test.getId());
		}
		for (TestClassRead obj : a) {
			Assertions.assertFalse(objs.contains(obj));
			Assertions.assertTrue(x.contains(obj.getX()));
			Assertions.assertTrue(y.contains(obj.getY()));
			Assertions.assertTrue(pk.contains(obj.getId()));
		}
		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestClassRead.class);

		Set<Session.Exp> args = new HashSet<>();
		args.add(new Session.Exp("x", ">=", 20));
		args.add(new Session.Exp("x", "<=", 30));

		session.beginTransaction();
		objs = session.getObjects(TestClassRead.class, args);
		session.endTransaction();

		Assertions.assertEquals(2, objs.size());
		for (TestClassRead test : objs) {
			Assertions.assertTrue(test.getX() >= 20);
			Assertions.assertTrue(test.getX() <= 30);
		}

		manager.terminate(session);
		Files.deleteIfExists(dbFile.toPath());
	}

}

@Entity()
@Table(name = "table")
final class TestClassRead {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	public TestClassRead() {
	}

	public TestClassRead(Integer x, String y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return this.x;
	}

	public String getY() {
		return this.y;
	}

	public Integer getId() {
		return this.id;
	}
}
