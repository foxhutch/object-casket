package org.fuchss.objectcasket.objectpacker.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.fuchss.objectcasket.common.CasketError.CE4;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.PackerPort;
import org.fuchss.objectcasket.objectpacker.common.Observer;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestObserver {

	Configuration config = null;
	private File dbFile;

	@Test
	void observerTest() throws IOException, CasketException, InterruptedException {
		this.observerTest(DB.SQLITE);
		this.observerTest(DB.H2);
	}

	private void observerTest(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Session[] sessions = { manager.session(this.config), manager.session(this.config) };
		Assertions.assertNotEquals(sessions[0], sessions[1]);

		sessions[0].declareClass(TestEntity.class);
		sessions[1].declareClass(TestEntity.class);

		Observer obs = new Observer();
		sessions[1].register(obs);

		TestEntity[] entities = { new TestEntity(10, "A"), new TestEntity(20, "B"), new TestEntity(30, "C"), new TestEntity(40, "D") };

		sessions[0].beginTransaction();
		for (TestEntity entity : entities)
			sessions[0].persist(entity);
		sessions[0].endTransaction();

		Assertions.assertTrue(obs.isEmpty());

		Set<TestEntity> objs = sessions[1].getAllObjects(TestEntity.class);

		Assertions.assertEquals(entities.length, objs.size());

		entities[1].setY("BB");
		sessions[0].persist(entities[1]);

		Assertions.assertTrue(Utility.waitForChange());
		Assertions.assertFalse(obs.isEmpty());
		List<Object> changedObjs = new ArrayList<>(obs.getChanged());
		List<Object> deletedObjs = new ArrayList<>(obs.getDeleted());

		Assertions.assertEquals(1, changedObjs.size());
		Assertions.assertTrue(deletedObjs.isEmpty());

		TestEntity entity = (TestEntity) changedObjs.get(0);

		Assertions.assertNotEquals(entities[1], entity);
		Assertions.assertEquals(entities[1].id, entity.id);
		Assertions.assertEquals(entities[1].getX(), entity.getX());
		Assertions.assertEquals(entities[1].getY(), entity.getY());
		Assertions.assertTrue(objs.contains(entity));

		Assertions.assertTrue(obs.isEmpty());

		sessions[0].delete(entities[0]);

		Assertions.assertTrue(Utility.waitForChange());

		Assertions.assertFalse(obs.isEmpty());

		changedObjs = new ArrayList<>(obs.getChanged());
		deletedObjs = new ArrayList<>(obs.getDeleted());

		Assertions.assertEquals(1, deletedObjs.size());
		Assertions.assertTrue(changedObjs.isEmpty());

		entity = (TestEntity) deletedObjs.get(0);

		Assertions.assertNotEquals(entities[0], entity);
		Assertions.assertEquals(entities[0].id, entity.id);
		Assertions.assertEquals(entities[0].getX(), entity.getX());
		Assertions.assertEquals(entities[0].getY(), entity.getY());

		objs = sessions[1].getAllObjects(TestEntity.class);

		CasketException exc = null;
		try {
			sessions[1].resync(entity);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CE4.UNKNOWN_MANAGED_OBJECT, exc.error());

		Assertions.assertFalse(objs.contains(entity));

		manager.terminateAll();
		Files.deleteIfExists(this.dbFile.toPath());

	}

	void init(DB dialect) throws IOException, CasketException, InterruptedException {

		this.dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		this.config = manager.createConfiguration();
		this.config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		this.config.setUri(this.dbFile.toURI().getPath());
		this.config.setUser("");
		this.config.setPassword("");
		this.config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(this.config);
		manager.addEntity(dom, TestEntity.class);
		manager.finalizeDomain(dom);

	}
}

@Entity()
@Table(name = "tableEntity")
final class TestEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	@SuppressWarnings("unused")
	private TestEntity() {
	}

	public TestEntity(Integer x, String y) {
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

	@Override
	public String toString() {
		return this.y;
	}

}
