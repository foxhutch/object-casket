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

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TestMany2One {

	@Test
	void createTablesTest() throws IOException, CasketException, InterruptedException {
		this.createTablesTest(DB.SQLITE);
		this.createTablesTest(DB.H2);
	}

	@Test
	void persistTest() throws IOException, CasketException, InterruptedException {
		this.persistTest(DB.SQLITE);
		this.persistTest(DB.H2);

	}

	@Test
	void createInsertDeleteTest1() throws IOException, CasketException, InterruptedException {
		this.createInsertDeleteTest1(DB.SQLITE);
		this.createInsertDeleteTest1(DB.H2);

	}

	@Test
	void createInsertDeleteTest2() throws IOException, CasketException, InterruptedException {
		this.createInsertDeleteTest2(DB.SQLITE);
		this.createInsertDeleteTest2(DB.H2);

	}

	@Test
	void createInsertDeleteTest3() throws IOException, CasketException, InterruptedException {
		this.createInsertDeleteTest3(DB.SQLITE);
		this.createInsertDeleteTest3(DB.H2);

	}

	@Test
	void createInsertDeleteTest4() throws IOException, CasketException, InterruptedException {
		this.createInsertDeleteTest4(DB.SQLITE);
		this.createInsertDeleteTest4(DB.H2);

	}

	@Test
	void createInsertDeleteTest5() throws IOException, CasketException, InterruptedException {
		this.createInsertDeleteTest5(DB.SQLITE);
		this.createInsertDeleteTest5(DB.H2);

	}

	private void createTablesTest(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class, Client2.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class, Client2.class);

		Session session2 = manager.session(config);
		session2.declareClass(TestTarget2.class, ClientOne.class); // it's possible a view

		boolean error = false;
		try {
			session.declareClass(TestTarget2.class, ClientOne.class);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		manager.terminateAll();

		dom = manager.editDomain(config);

		error = false;
		try {
			manager.addEntity(dom, TestTarget3.class, ClientTwo.class);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		error = false;
		try {
			manager.finalizeDomain(dom);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void persistTest(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class, Client2.class, Client3.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class, Client2.class, Client3.class);

		TestTarget target = new TestTarget();
		Client1 cl1 = new Client1();
		Client2 cl2 = new Client2();
		Client3 cl3 = new Client3();

		target.x = 10;
		cl1.myTarget = target;
		target.cl = cl1;
		cl1.x = 11;
		cl2.myTarget = target;
		cl2.x = 12;
		cl3.myTarget = target;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target);
		session.endTransaction();

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		session.beginTransaction();
		List<Client1> cl1s = new ArrayList<>(session.getAllObjects(Client1.class));
		session.endTransaction();
		Client1 clOne = cl1s.get(0);
		Assertions.assertEquals(clOne.x, cl1.x);
		Assertions.assertEquals(clOne.id, cl1.id);
		Assertions.assertEquals(clOne.myTarget.id, cl1.myTarget.id);
		Assertions.assertEquals(clOne.myTarget.x, cl1.myTarget.x);
		Assertions.assertEquals(clOne.myTarget.cl, clOne);

		TestTarget target0 = clOne.myTarget;
		TestTarget target2 = new TestTarget();
		clOne.myTarget = target2;
		target2.x = 25;

		session.beginTransaction();
		session.persist(clOne);
		session.endTransaction();

		Assertions.assertEquals(target0.cl, clOne);
		Assertions.assertEquals(clOne.myTarget, target2);
		Assertions.assertNotEquals(target2.cl, clOne);

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void createInsertDeleteTest1(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		TestTarget target1 = new TestTarget();
		TestTarget target2 = new TestTarget();
		TestTarget target3 = new TestTarget();
		Client1 cl1 = new Client1();
		Client1 cl2 = new Client1();
		Client1 cl3 = new Client1();

		target1.x = 10;
		cl1.myTarget = target1;
		cl1.x = 5;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target1);
		session.persist(target2);
		session.persist(target3);
		session.endTransaction();

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		List<Client1> cls = new ArrayList<>(session.getAllObjects(Client1.class));
		Assertions.assertEquals(3, cls.size());

		List<TestTarget> tgs = new ArrayList<>(session.getAllObjects(TestTarget.class));
		Assertions.assertEquals(3, tgs.size());

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void createInsertDeleteTest2(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		TestTarget target1 = new TestTarget();
		TestTarget target2 = new TestTarget();
		TestTarget target3 = new TestTarget();
		Client1 cl1 = new Client1();
		Client1 cl2 = new Client1();
		Client1 cl3 = new Client1();

		target1.x = 10;
		cl1.myTarget = target1;
		cl1.x = 5;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target1);
		session.persist(target2);
		session.persist(target3);
		session.endTransaction();

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		Session.Exp exp = new Session.Exp("x", "==", 5);
		Set<Session.Exp> filter = new HashSet<>();
		filter.add(exp);
		List<Client1> cls = new ArrayList<>(session.getObjects(Client1.class, filter));
		Assertions.assertEquals(1, cls.size());

		exp = new Session.Exp("x", "==", 10);
		filter.clear();
		filter.add(exp);
		List<TestTarget> tgs = new ArrayList<>(session.getObjects(TestTarget.class, filter));
		Assertions.assertEquals(1, tgs.size());

		Assertions.assertEquals(cls.get(0).myTarget, tgs.get(0));

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void createInsertDeleteTest3(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		TestTarget target1 = new TestTarget();
		TestTarget target2 = new TestTarget();
		TestTarget target3 = new TestTarget();
		Client1 cl1 = new Client1();
		Client1 cl2 = new Client1();
		Client1 cl3 = new Client1();

		target1.x = 10;
		cl1.myTarget = target1;
		cl1.x = 5;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target1);
		session.persist(target2);
		session.persist(target3);
		session.endTransaction();

		session.delete(cl2);
		session.delete(target2);

		boolean error = false;
		try {
			session.delete(target1);
		} catch (CasketException e) {
			error = true;
		}

		Assertions.assertTrue(error);
		error = false;
		try {
			session.delete(cl1);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);
		error = false;

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		List<Client1> cls = new ArrayList<>(session.getAllObjects(Client1.class));
		Assertions.assertEquals(2, cls.size());

		List<TestTarget> tgs = new ArrayList<>(session.getAllObjects(TestTarget.class));
		Assertions.assertEquals(2, tgs.size());
		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void createInsertDeleteTest4(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		TestTarget target1 = new TestTarget();
		TestTarget target2 = new TestTarget();
		TestTarget target3 = new TestTarget();
		Client1 cl1 = new Client1();
		Client1 cl2 = new Client1();
		Client1 cl3 = new Client1();

		target1.x = 10;
		cl1.myTarget = target1;
		cl1.x = 5;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target1);
		session.persist(target2);
		session.persist(target3);
		session.endTransaction();

		Session.Exp exp = new Session.Exp("x", "==", 5);
		Set<Session.Exp> filter = new HashSet<>();
		filter.add(exp);
		List<Client1> cls = new ArrayList<>(session.getObjects(Client1.class, filter));
		Assertions.assertEquals(1, cls.size());

		exp = new Session.Exp("x", "==", 10);
		filter.clear();
		filter.add(exp);
		List<TestTarget> tgs = new ArrayList<>(session.getObjects(TestTarget.class, filter));
		Assertions.assertEquals(1, tgs.size());

		Assertions.assertEquals(cls.get(0).myTarget, tgs.get(0));

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

	private void createInsertDeleteTest5(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestTarget.class, Client1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestTarget.class, Client1.class);

		TestTarget target1 = new TestTarget();
		TestTarget target2 = new TestTarget();
		TestTarget target3 = new TestTarget();
		Client1 cl1 = new Client1();
		Client1 cl2 = new Client1();
		Client1 cl3 = new Client1();

		target1.x = 10;
		target2.x = 20;

		cl1.myTarget = target1;
		cl1.x = 5;

		session.beginTransaction();
		session.persist(cl1);
		session.persist(cl2);
		session.persist(cl3);
		session.persist(target1);
		session.persist(target2);
		session.persist(target3);
		session.endTransaction();

		Session.Exp exp = new Session.Exp("x", "==", 5);
		Set<Session.Exp> filter = new HashSet<>();
		filter.add(exp);
		List<Client1> cls = new ArrayList<>(session.getObjects(Client1.class, filter));
		Assertions.assertEquals(1, cls.size());

		exp = new Session.Exp("x", "==", 10);
		filter.clear();
		filter.add(exp);
		List<TestTarget> tgs = new ArrayList<>(session.getObjects(TestTarget.class, filter));
		Assertions.assertEquals(1, tgs.size());

		Assertions.assertEquals(cls.get(0).myTarget, tgs.get(0));

		cl1.myTarget = target2;
		session.persist(cl1);

		exp = new Session.Exp("x", "==", 5);
		filter = new HashSet<>();
		filter.add(exp);
		cls = new ArrayList<>(session.getObjects(Client1.class, filter));
		Assertions.assertEquals(1, cls.size());

		exp = new Session.Exp("x", "==", 10);
		filter = new HashSet<>();
		filter.add(exp);
		tgs = new ArrayList<>(session.getObjects(TestTarget.class, filter));
		Assertions.assertEquals(1, tgs.size());

		Assertions.assertNotEquals(cls.get(0).myTarget, tgs.get(0));

		exp = new Session.Exp("x", "==", 20);
		filter = new HashSet<>();
		filter.add(exp);
		tgs = new ArrayList<>(session.getObjects(TestTarget.class, filter));
		Assertions.assertEquals(1, tgs.size());

		Assertions.assertEquals(cls.get(0).myTarget, tgs.get(0));

		manager.terminateAll();

		Files.deleteIfExists(dbFile.toPath());

	}

}

@Entity()
@Table(name = "target")
final class TestTarget {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	public Integer x;

	@ManyToOne
	public Client1 cl;

	public TestTarget() {
	}

}

@Entity()
@Table(name = "target")
final class TestTarget2 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	public Integer x;

	public TestTarget2() {
	}

}

@Entity()
@Table(name = "target3")
final class TestTarget3 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	public Integer x;

	public TestTarget3() {
	}

}

@Entity()
@Table(name = "clientOne")
final class Client1 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "value")
	public Integer x;

	@ManyToOne
	@Column(name = "fk")
	public TestTarget myTarget;

	public Client1() {
	}
}

@Entity()
@Table(name = "clientTwo")
final class Client2 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "value")
	public Integer x;

	@ManyToOne
	@Column(name = "fk")
	public TestTarget myTarget;

	public Client2() {
	}
}

@Entity()
@Table(name = "clientThree")
final class Client3 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@ManyToOne
	@Column(name = "fk")
	public TestTarget myTarget;

	public Client3() {
	}
}

@Entity()
@Table(name = "clientOne")
final class ClientOne {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "value")
	public Integer x;

	@ManyToOne
	@Column(name = "fk")
	public TestTarget2 myTarget;

	public ClientOne() {
	}
}

@Entity()
@Table(name = "clientOne")
final class ClientTwo {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "value")
	public Integer x;

	@ManyToOne
	@Column(name = "fk")
	public TestTarget3 myTarget;

	public ClientTwo() {
	}
}
