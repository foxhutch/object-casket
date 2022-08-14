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

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TestSelfContained {

	Configuration config = null;
	private File dbFile;

	@Test
	void selfContainedM2MTest() throws CasketException, InterruptedException, IOException {
		this.selfContainedM2MTest(DB.SQLITE);
		this.selfContainedM2MTest(DB.H2);

	}

	@Test
	void selfContainedInitTest() throws CasketException, InterruptedException, IOException {
		this.selfContainedInitTest(DB.SQLITE);
		this.selfContainedInitTest(DB.H2);

	}

	@Test
	void selfContainedModifyTest() throws CasketException, InterruptedException, IOException {
		this.selfContainedModifyTest(DB.SQLITE);
		this.selfContainedModifyTest(DB.H2);

	}

	@Test
	void selfContainedDeleteTest() throws CasketException, InterruptedException, IOException {
		this.selfContainedDeleteTest(DB.SQLITE);
		this.selfContainedDeleteTest(DB.H2);

	}

	private void selfContainedM2MTest(DB dialect) throws CasketException, InterruptedException, IOException {
		this.init(dialect);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Session[] sessions = { manager.session(this.config), manager.session(this.config) };
		sessions[0].declareClass(Person.class);
		sessions[1].declareClass(Person.class);

		Map<String, Person> friends = this.mkFriends();
		for (Person person : friends.values())
			sessions[0].persist(person);

		Observer obs = new Observer();
		sessions[0].register(obs);

		Set<Person> allPersons = sessions[1].getAllObjects(Person.class);

		for (Person p1 : allPersons) {
			Person p2 = p1.friends.iterator().next();
			Person q1 = friends.get(p1.name);
			Person q2 = friends.get(p2.name);
			Assertions.assertEquals(p1.friends.size(), q1.friends.size());
			Assertions.assertTrue(q1.friends.contains(q2));

			p1.friends.remove(p2);
			sessions[1].persist(p1);
			Assertions.assertTrue(Utility.waitForChange());
			Assertions.assertFalse(obs.isEmpty());
			Assertions.assertTrue(obs.getChanged().contains(q1));

			Assertions.assertEquals(p1.friends.size(), q1.friends.size());
			Assertions.assertFalse(q1.friends.contains(q2));
		}

		manager.terminateAll();
		Files.deleteIfExists(this.dbFile.toPath());
	}

	private void selfContainedInitTest(DB dialect) throws CasketException, InterruptedException, IOException {
		this.init(dialect);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Session[] sessions = { manager.session(this.config), manager.session(this.config) };
		sessions[0].declareClass(Person.class);
		sessions[1].declareClass(Person.class);

		Map<String, Person> friends = this.mkFriends();
		for (Person person : friends.values())
			sessions[0].persist(person);

		Set<Person> allPersons = sessions[1].getAllObjects(Person.class);
		Assertions.assertEquals(friends.values().size(), allPersons.size());

		Set<String> allNames = new HashSet<>();
		for (Person person : allPersons) {
			String name = person.name;
			allNames.add(name);
			Assertions.assertEquals(friends.get(name).bestFriend.name, person.bestFriend.name);
		}
		Assertions.assertEquals(friends.values().size(), allNames.size());

		this.checkFriends(new HashSet<>(friends.values()), allPersons);

		manager.terminateAll();
		Files.deleteIfExists(this.dbFile.toPath());
	}

	private void selfContainedModifyTest(DB dialect) throws CasketException, InterruptedException, IOException {
		this.init(dialect);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Session[] sessions = { manager.session(this.config), manager.session(this.config) };
		sessions[0].declareClass(Person.class);
		sessions[1].declareClass(Person.class);

		Observer obs = new Observer();
		sessions[1].register(obs);

		Map<String, Person> friends = this.mkFriends();
		for (Person person : friends.values()) {
			sessions[0].persist(person);
			Utility.waitForChange();
			Assertions.assertTrue(obs.isEmpty());
		}

		Set<Person> allPersons = sessions[1].getAllObjects(Person.class);
		Assertions.assertEquals(friends.values().size(), allPersons.size());

		Person p1 = friends.values().iterator().next();

		p1.bestFriend = (p1.bestFriend != p1) ? p1 : friends.values().iterator().next();

		sessions[0].persist(p1);

		Assertions.assertTrue(Utility.waitForChange());
		Assertions.assertFalse(obs.isEmpty());

		Set<String> allNames = new HashSet<>();
		for (Person person : allPersons) {
			String name = person.name;
			allNames.add(name);
			String name1 = friends.get(name).bestFriend != null ? friends.get(name).bestFriend.name : "";
			String name2 = person.bestFriend != null ? person.bestFriend.name : "";
			Assertions.assertEquals(name1, name2);
		}
		Assertions.assertEquals(friends.values().size(), allNames.size());

		this.checkFriends(new HashSet<>(friends.values()), allPersons);

		manager.terminateAll();
		Files.deleteIfExists(this.dbFile.toPath());

	}

	private void selfContainedDeleteTest(DB dialect) throws CasketException, InterruptedException, IOException {
		this.init(dialect);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Session[] sessions = { manager.session(this.config), manager.session(this.config) };
		sessions[0].declareClass(Person.class);
		sessions[1].declareClass(Person.class);

		Observer obs = new Observer();
		sessions[0].register(obs);

		Map<String, Person> friends = this.mkFriends();
		for (Person person : friends.values()) {
			sessions[0].persist(person);
			Utility.waitForChange();
			Assertions.assertTrue(obs.isEmpty());
		}

		Set<Person> allPersons = sessions[1].getAllObjects(Person.class);

		for (Person p1 : allPersons) {

			Person bestFriend = p1.bestFriend;
			p1.bestFriend = null;

			sessions[1].persist(p1);

			Assertions.assertTrue(Utility.waitForChange());
			Assertions.assertFalse(obs.isEmpty());

			Person q1 = friends.get(p1.name);
			Person q2 = friends.get(bestFriend.name);
			Assertions.assertNotNull(q2);

			Set<Person> stillABestFriend = new HashSet<>();
			for (Person person : allPersons)
				if (person.bestFriend != null) {
					stillABestFriend.add(person.bestFriend);
				}

			Assertions.assertNull(q1.bestFriend);
			Set<Object> changed = obs.getChanged();

			Assertions.assertTrue(changed.contains(q1));
			if (stillABestFriend.contains(q2) && (q2 != q1))
				Assertions.assertFalse(changed.contains(q2));
			else
				Assertions.assertTrue(changed.contains(q2));
		}

		manager.terminateAll();
		Files.deleteIfExists(this.dbFile.toPath());

	}

	void checkFriends(Set<Person> friends, Set<Person> otherFriends) {
		Assertions.assertEquals(friends.size(), otherFriends.size());

		Set<String> friendNames = new HashSet<>();

		for (Person friend : friends)
			friendNames.add(friend.name);
		Assertions.assertEquals(friends.size(), friendNames.size());

		for (Person friend : otherFriends)
			friendNames.remove(friend.name);
		Assertions.assertTrue(friendNames.isEmpty());

	}

	Map<String, Person> mkFriends() {
		Person peter = new Person("Peter");
		Person paul = new Person("Paul");
		Person mary = new Person("Mary");
		Person julian = new Person("Julian");
		Person dick = new Person("Dick");
		Person anne = new Person("Anne");
		Person george = new Person("George");
		Person timmy = new Person("Timmy");

		Map<String, Person> persons = new HashMap<>();
		persons.put("Peter", peter);
		persons.put("Paul", paul);
		persons.put("Mary", mary);
		persons.put("Julian", julian);
		persons.put("Dick", dick);
		persons.put("Anne", anne);
		persons.put("George", george);
		persons.put("Timmy", timmy);

		peter.bestFriend = paul;
		paul.bestFriend = mary;
		mary.bestFriend = mary;

		julian.bestFriend = dick;
		dick.bestFriend = anne;
		anne.bestFriend = george;
		george.bestFriend = timmy;
		timmy.bestFriend = timmy;

		peter.friends.add(paul);
		peter.friends.add(mary);
		peter.friends.add(peter);

		paul.friends.add(paul);
		paul.friends.add(mary);
		paul.friends.add(peter);

		mary.friends.add(paul);
		mary.friends.add(mary);
		mary.friends.add(peter);

		julian.friends.add(dick);
		julian.friends.add(anne);
		julian.friends.add(george);
		julian.friends.add(timmy);
		julian.friends.add(julian);

		dick.friends.add(dick);
		dick.friends.add(anne);
		dick.friends.add(george);
		dick.friends.add(timmy);
		dick.friends.add(julian);

		anne.friends.add(dick);
		anne.friends.add(anne);
		anne.friends.add(george);
		anne.friends.add(timmy);
		anne.friends.add(julian);

		timmy.friends.add(dick);
		timmy.friends.add(anne);
		timmy.friends.add(george);
		timmy.friends.add(timmy);
		timmy.friends.add(julian);

		george.friends.add(dick);
		george.friends.add(anne);
		george.friends.add(george);
		george.friends.add(timmy);
		george.friends.add(julian);

		return persons;

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
		manager.addEntity(dom, Person.class);
		manager.finalizeDomain(dom);

	}

}

@Entity()
final class Person {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "name")
	String name;

	@ManyToMany
	Set<Person> friends = new HashSet<>();

	@ManyToOne
	Person bestFriend;

	@SuppressWarnings("unused")
	private Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
