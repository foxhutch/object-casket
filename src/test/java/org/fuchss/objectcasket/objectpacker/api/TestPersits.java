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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestPersits {

	@Test
	void persistTest() throws IOException, CasketException, InterruptedException {
		this.persistTest(DB.SQLITE);
		this.persistTest(DB.H2);
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
		manager.addEntity(dom, TestA.class, TestB.class, TestC.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestA.class, TestB.class, TestC.class);
		// session.declareClass(TestA.class);
		TestA a = new TestA(10, "ABC");

		session.beginTransaction();
		session.persist(a);
		session.endTransaction();

		Integer pk = a.getId();
		Integer x = a.getX();
		String y = a.getY();

		int[] xyz = a.getXYZ();
		List<String> abc = a.getABC();

		a.setX(0);
		a.setY("");
		a.setId(0);

		a.xyz = null;
		a.abc = null;

		session.beginTransaction();
		session.resync(a);
		session.endTransaction();

		Assertions.assertEquals(pk, a.getId());
		Assertions.assertEquals(x, a.getX());
		Assertions.assertEquals(y, a.getY());

		Assertions.assertArrayEquals(xyz, a.getXYZ());
		Assertions.assertIterableEquals(abc, a.getABC());

		a.setX(0);
		a.setY("");
		a.setId(0);

		a.xyz = null;
		a.abc = null;

		session.beginTransaction();
		session.resync();
		session.endTransaction();

		Assertions.assertEquals(pk, a.getId());
		Assertions.assertEquals(x, a.getX());
		Assertions.assertEquals(y, a.getY());

		Assertions.assertArrayEquals(xyz, a.getXYZ());
		Assertions.assertIterableEquals(abc, a.getABC());

		manager.terminate(session);

		session = manager.session(config);

		boolean error = false;
		try {
			session.declareClass(TestER.class);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);

		manager.terminate(session);

		Files.deleteIfExists(dbFile.toPath());

	}
}

@Entity()
@Table(name = "tableA")
final class TestA {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	int[] xyz = new int[] { 1, 3, 4 };

	ArrayList<String> abc = new ArrayList<>(Arrays.asList(new String[] { "aa", "bb", "cc" }));

	public TestA() {
	}

	public TestA(Integer x, String y) {
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

	public int[] getXYZ() {
		return this.xyz;
	}

	public List<String> getABC() {
		return this.abc;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

@Entity()
@Table(name = "tableB")
final class TestB {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	@ManyToOne
	@Column(name = "AxB")
	public TestA oneA;

	public TestB() {
	}

	public TestB(Integer x, String y) {
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

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

@Entity()
@Table(name = "tableC")
final class TestC {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	@ManyToOne
	public TestA oneA;

	public TestC() {
	}

	public TestC(Integer x, String y) {
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

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

@Entity()
@Table(name = "tableError")
final class TestER {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "name")
	private Integer x;

	@Column(name = "name")
	private String y;

	public TestER() {
	}
}
