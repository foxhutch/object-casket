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

import jakarta.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

class TestMany2Many {

	@Test
	void createTablesTest() throws IOException, CasketException, InterruptedException {
		this.createTablesTest(DB.SQLITE);
		this.createTablesTest(DB.H2);
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
		manager.addEntity(dom, M2MTestClient.class, Supplier.class, M2MTestClient2.class, Supplier2.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);

		session.declareClass(M2MTestClient.class, Supplier.class, M2MTestClient2.class, Supplier2.class);

		manager.terminateAll();
		session = manager.session(config);

		session.declareClass(M2MTestClient.class, Supplier.class, M2MTestClient2.class, Supplier2.class);

		M2MTestClient target = new M2MTestClient();

		Supplier supp = new Supplier("a");

		target.suppliers.add(supp);

		M2MTestClient2 target2 = new M2MTestClient2();

		Supplier2 supp2 = new Supplier2("a");

		target2.suppliers.add(supp2);

		session.beginTransaction();
		session.persist(target);
		session.persist(target2);
		session.endTransaction();

		manager.terminateAll();
		session = manager.session(config);

		session.declareClass(M2MTestClient.class, Supplier.class, M2MTestClient2.class, Supplier2.class);

		session.beginTransaction();
		Set<M2MTestClient> targets = session.getAllObjects(M2MTestClient.class);
		Set<M2MTestClient2> targets2 = session.getAllObjects(M2MTestClient2.class);
		session.endTransaction();

		M2MTestClient targetb = targets.iterator().next();
		M2MTestClient2 target2b = targets2.iterator().next();

		targetb.suppliers.clear();
		target2b.suppliers.clear();

		session.beginTransaction();
		session.resync();
		session.endTransaction();

		manager.terminateAll();

		boolean res = Files.deleteIfExists(dbFile.toPath());
		Assertions.assertTrue(res);

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
		manager.addEntity(dom, M2MTestClient.class, Supplier.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);

		session.declareClass(M2MTestClient.class, Supplier.class);

		Supplier[] target = { new Supplier("a"), new Supplier("b"), new Supplier("c"), new Supplier("d"), new Supplier("e"), new Supplier("f") };
		List<Supplier> suppliers = Arrays.asList(target);
		M2MTestClient client = new M2MTestClient();
		client.x = 10;
		suppliers.forEach(client.suppliers::add);

		int size = client.suppliers.size();
		session.persist(client);
		Assertions.assertEquals(client.suppliers.size(), size);

		List<M2MTestClient> cls = new ArrayList<>(session.getAllObjects(M2MTestClient.class));
		Assertions.assertEquals(1, cls.size());

		Assertions.assertEquals(client, cls.get(0));

		Assertions.assertEquals(client.suppliers.size(), size);

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
		manager.addEntity(dom, M2MTestClient.class, Supplier.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);

		session.declareClass(M2MTestClient.class, Supplier.class);

		Supplier[] target = { new Supplier("a"), new Supplier("b"), new Supplier("c"), new Supplier("d"), new Supplier("e"), new Supplier("f") };
		List<Supplier> suppliers = Arrays.asList(target);
		M2MTestClient client = new M2MTestClient();
		client.x = 10;
		suppliers.forEach(client.suppliers::add);

		int size = client.suppliers.size();
		session.persist(client);
		Assertions.assertEquals(client.suppliers.size(), size);

		Supplier s = new Supplier("x");
		client.suppliers.add(s);
		session.resync(client);

		Assertions.assertFalse(client.suppliers.contains(s));

		client.suppliers.add(s);
		session.persist(client);

		Session.Exp exp = new Session.Exp("id", "==", "x");
		Set<Session.Exp> filter = new HashSet<>();
		filter.add(exp);

		List<Supplier> supps = new ArrayList<>(session.getObjects(Supplier.class, filter));

		Assertions.assertEquals(1, supps.size());

		Assertions.assertEquals(supps.get(0), s);

		// Assertions.assertTrue(cls.size() == 1);

		// Assertions.assertTrue(client == cls.get(0));

		Assertions.assertEquals(client.suppliers.size(), size + 1);

		manager.terminateAll();
		Files.deleteIfExists(dbFile.toPath());

	}

}

@Entity()
@Table(name = "Client2")
final class M2MTestClient2 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	public Integer x;

	@ManyToMany
	public Set<Supplier2> suppliers = new HashSet<>();

	public M2MTestClient2() {
	}

}

@Entity()
@Table(name = "Supplier2")
final class Supplier2 {

	@Id
	@Column(name = "sid")
	String id;

	@Column(name = "value")
	public Integer x;

	public Supplier2() {
	}

	public Supplier2(String x) {
		this.id = x;
	}
}

@Entity()
@Table(name = "Client")
final class M2MTestClient {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	public Integer x;

	@ManyToMany
	public Set<Supplier> suppliers = new HashSet<>();

	public M2MTestClient() {
	}

}

@Entity()
@Table(name = "Supplier")
final class Supplier {

	@Id
	@Column(name = "sid")
	String id;

	@Column(name = "value")
	public Integer x;

	public Supplier() {
	}

	public Supplier(String x) {
		this.id = x;
	}
}
