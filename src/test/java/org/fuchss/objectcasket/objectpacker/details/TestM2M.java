package org.fuchss.objectcasket.objectpacker.details;

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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

class TestM2M {

	Configuration config = null;
	private File dbFile;
	SessionManager manager = PackerPort.PORT.sessionManager();

	@Test
	void sessionEqualsTest1() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest1(DB.SQLITE);
		this.sessionEqualsTest1(DB.H2);
	}

	@Test
	void sessionEqualsTest2() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest2(DB.SQLITE);
		this.sessionEqualsTest2(DB.H2);
	}

	@Test
	void sessionEqualsTest3() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest3(DB.SQLITE);
		this.sessionEqualsTest3(DB.H2);
	}

	@Test
	void sessionEqualsTest4() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest4(DB.SQLITE);
		this.sessionEqualsTest4(DB.H2);
	}

	@Test
	void sessionEqualsTest5() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest5(DB.SQLITE);
		this.sessionEqualsTest5(DB.H2);
	}

	@Test
	void sessionEqualsTest6() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest6(DB.SQLITE);
		this.sessionEqualsTest6(DB.H2);
	}

	@Test
	void sessionEqualsTest7() throws IOException, CasketException, InterruptedException {
		this.sessionEqualsTest7(DB.SQLITE);
		this.sessionEqualsTest7(DB.H2);
	}

	private void sessionEqualsTest1(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();

		Session session1 = this.manager.session(this.config);
		Session session2 = this.manager.session(this.config);

		session1.declareClass(EntityObj.class);
		session2.declareClass(EntityObj.class);

		Set<EntityObj> added = new HashSet<>();

		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);
			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
		}

		this.quit();
	}

	private void sessionEqualsTest2(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);

		Set<EntityObj> added = new HashSet<>();

		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);

			Session session2 = this.manager.session(this.config);
			Assertions.assertNotEquals(session1, session2);
			session2.declareClass(EntityObj.class);

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			this.manager.terminate(session2);
		}
		this.quit();
	}

	private void sessionEqualsTest3(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);

		Set<EntityObj> added = new HashSet<>();

		session1.beginTransaction();
		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);
		}
		session1.endTransaction();

		Session session2 = this.manager.session(this.config);
		Assertions.assertNotEquals(session1, session2);
		session2.declareClass(EntityObj.class);

		Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
		Assertions.assertEquals(added.size(), find.size());
		Map<Integer, EntityObj> findEntities = new HashMap<>();
		for (EntityObj e : find) {
			findEntities.put(e.id, e);
		}
		for (EntityObj e : added) {
			Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
		}

		this.quit();
	}

	private void sessionEqualsTest4(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);

		Set<EntityObj> added = new HashSet<>();

		session1.beginTransaction();
		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);
		}
		session1.endTransaction();

		for (EntityObj entity : allEntities.values()) {

			added.remove(entity);
			session1.delete(entity);

			Session session2 = this.manager.session(this.config);
			Assertions.assertNotEquals(session1, session2);
			session2.declareClass(EntityObj.class);

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			this.manager.terminate(session2);
		}
		Assertions.assertTrue(added.isEmpty());
		this.quit();
	}

	private void sessionEqualsTest5(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();
		List<EntityObj> entityList = new ArrayList<>(allEntities.values());

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);

		Set<EntityObj> added = new HashSet<>();

		session1.beginTransaction();
		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);
		}
		session1.endTransaction();

		int i = 0;
		for (EntityObj entity : allEntities.values()) {

			entity.assigned.add(entityList.get(i));
			session1.persist(entity);

			Session session2 = this.manager.session(this.config);
			Assertions.assertNotEquals(session1, session2);
			session2.declareClass(EntityObj.class);

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			this.manager.terminate(session2);
			i++;
		}

		i = 0;
		for (EntityObj entity : allEntities.values()) {

			entity.assigned.remove(entityList.get(i));
			session1.persist(entity);

			Session session2 = this.manager.session(this.config);
			Assertions.assertNotEquals(session1, session2);
			session2.declareClass(EntityObj.class);

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			this.manager.terminate(session2);
			i++;
		}

		this.quit();
	}

	private void sessionEqualsTest6(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();
		List<EntityObj> entityList = new ArrayList<>(allEntities.values());

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);
		Session session2 = this.manager.session(this.config);
		session2.declareClass(EntityObj.class);
		Assertions.assertNotEquals(session1, session2);

		Set<EntityObj> added = new HashSet<>();

		session1.beginTransaction();
		for (EntityObj entity : allEntities.values()) {
			added.add(entity);
			session1.persist(entity);
		}
		session1.endTransaction();

		int i = 0;
		for (EntityObj entity : allEntities.values()) {

			entity.assigned.add(entityList.get(i));
			session1.persist(entity);
			Assertions.assertTrue(Utility.waitForChange());

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			i++;
		}

		i = 0;
		for (EntityObj entity : allEntities.values()) {

			entity.assigned.remove(entityList.get(i));
			session1.persist(entity);
			Assertions.assertTrue(Utility.waitForChange());

			Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
			Assertions.assertEquals(added.size(), find.size());
			Map<Integer, EntityObj> findEntities = new HashMap<>();
			for (EntityObj e : find) {
				findEntities.put(e.id, e);
			}
			for (EntityObj e : added) {
				Assertions.assertTrue(e.sameAs(findEntities.get(e.id)));
			}
			i++;
		}

		this.quit();
	}

	private void sessionEqualsTest7(DB dialect) throws IOException, CasketException, InterruptedException {
		this.init(dialect);

		Map<String, EntityObj> allEntities = this.mkEntities();
		List<EntityObj> entityList = new ArrayList<>(allEntities.values());

		Session session1 = this.manager.session(this.config);
		session1.declareClass(EntityObj.class);
		Session session2 = this.manager.session(this.config);
		session2.declareClass(EntityObj.class);
		Assertions.assertNotEquals(session1, session2);

		Observer obs = new Observer();
		session2.register(obs);

		EntityObj p1 = entityList.get(0);
		session1.beginTransaction();
		session1.persist(p1);
		session1.endTransaction();

		Set<EntityObj> find = session2.getAllObjects(EntityObj.class);
		Assertions.assertEquals(1, find.size());

		EntityObj q1 = find.iterator().next();
		Assertions.assertTrue(p1.sameAs(q1));

		for (int i = 0; i < allEntities.values().size(); i++) {

			p1.assigned.add(entityList.get(i));
			session1.persist(p1);
			Assertions.assertTrue(Utility.waitForChange());

			Set<Object> changed = obs.getChanged();
			Assertions.assertTrue(changed.contains(q1));
			Assertions.assertEquals(1, changed.size());
			Assertions.assertTrue(p1.sameAs(q1));
		}

		this.quit();
	}

	void quit() throws CasketException, InterruptedException, IOException {
		this.manager.terminateAll();
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
		manager.addEntity(dom, EntityObj.class);
		manager.finalizeDomain(dom);

	}

	Map<String, EntityObj> mkEntities() {

		EntityObj peter = new EntityObj("Peter");
		EntityObj paul = new EntityObj("Paul");
		EntityObj mary = new EntityObj("Mary");
		EntityObj julian = new EntityObj("Julian");
		EntityObj dick = new EntityObj("Dick");
		EntityObj anne = new EntityObj("Anne");
		EntityObj george = new EntityObj("George");
		EntityObj timmy = new EntityObj("Timmy");

		Map<String, EntityObj> entityMap = new HashMap<>();
		entityMap.put("Peter", peter);
		entityMap.put("Paul", paul);
		entityMap.put("Mary", mary);
		entityMap.put("Julian", julian);
		entityMap.put("Dick", dick);
		entityMap.put("Anne", anne);
		entityMap.put("George", george);
		entityMap.put("Timmy", timmy);
		return entityMap;
	}

}

@Entity()
final class EntityObj {

	@Id
	@GeneratedValue
	Integer id;

	String name;

	@ManyToMany
	Set<EntityObj> assigned = new HashSet<>();

	@SuppressWarnings("unused")
	private EntityObj() {
	}

	EntityObj(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	boolean sameAs(EntityObj other) {
		boolean res = true;

		res &= this.id == other.id;
		res &= this.name == null ? other.name == null : this.name.equals(other.name);
		res &= this.assigned.size() == other.assigned.size();

		for (EntityObj e : this.assigned) {
			res &= other.findAssigned(e);
		}
		return res;

	}

	boolean findAssigned(EntityObj other) {
		for (EntityObj assignedEntity : this.assigned) {
			if (assignedEntity.id == other.id)
				return true;
		}
		return false;
	}

}
