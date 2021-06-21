package org.fuchss.objectcasket.tests.o2o;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class SimpleO2OTest extends TestBase {
	@Test
	public void justInit() throws ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(Human.class, HumanDetails.class);
		session.open();
		this.storePort.sessionManager().terminate(session);
	}

	@Test
	public void testBasics() throws ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(Human.class, HumanDetails.class);
		session.open();

		Human h1 = new Human();
		h1.name = "H1";
		HumanDetails hd1 = new HumanDetails();
		h1.details = hd1;
		hd1.age = 1;
		hd1.country = "C1";

		Human h2 = new Human();
		h2.name = "H2";
		HumanDetails hd2 = new HumanDetails();
		h2.details = hd2;
		hd2.age = 2;
		hd2.country = "C2";
		// Persist both
		session.persist(h1);
		session.persist(h2);

		Assert.assertEquals(2, session.getAllObjects(Human.class).size());
		Assert.assertEquals(2, session.getAllObjects(HumanDetails.class).size());

		// After deletion the reference to details shall be cleared but hd1
		// shall still exist
		session.delete(h1);
		Assert.assertNull(h1.details);
		Assert.assertEquals(1, session.getAllObjects(Human.class).size());
		Assert.assertEquals(2, session.getAllObjects(HumanDetails.class).size());

		h1.details = hd1;
		session.persist(h1);

		Assert.assertEquals(2, session.getAllObjects(Human.class).size());
		Assert.assertEquals(2, session.getAllObjects(HumanDetails.class).size());

		session.delete(h2);

		Assert.assertEquals(1, session.getAllObjects(Human.class).size());
		Assert.assertEquals(2, session.getAllObjects(HumanDetails.class).size());

		// Method getAllObjects() shall reference to the local objects.
		hd1.age = -1;
		Assert.assertEquals(-1, session.getAllObjects(Human.class).iterator().next().details.age);

		// DB shall be different
		Human humanBySession = this.getHumanWithSession();
		Assert.assertEquals(1, humanBySession.details.age);

		// Persist h1 shall not cascade
		session.persist(h1);
		Assert.assertEquals(-1, session.getAllObjects(Human.class).iterator().next().details.age);

		humanBySession = this.getHumanWithSession();
		Assert.assertEquals(1, humanBySession.details.age);

		// Persist hd1 shall save changes
		session.persist(hd1);
		Assert.assertEquals(-1, session.getAllObjects(Human.class).iterator().next().details.age);

		humanBySession = this.getHumanWithSession();
		Assert.assertEquals(-1, humanBySession.details.age);

		this.storePort.sessionManager().terminate(session);
	}

	private Human getHumanWithSession() throws ObjectCasketException {
		// Get Human with data in DB.
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(Human.class, HumanDetails.class);
		session.open();
		Human human = session.getAllObjects(Human.class).iterator().next();
		this.storePort.sessionManager().terminate(session);
		return human;
	}

	@Entity
	@Table(name = "HumanTable")
	public static final class Human {
		@Id
		@GeneratedValue
		Integer pk;

		String name;
		@OneToOne
		HumanDetails details;
	}

	@Entity
	public static final class HumanDetails {
		@Id
		@GeneratedValue
		Integer pk;

		int age;
		String country;
	}
}
