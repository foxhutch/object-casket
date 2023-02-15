package org.fuchss.objectcasket.objectpacker.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.PackerPort;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.objectpacker.port.SessionObserver;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPackerPort {

	File dbFile;

	@Test
	void usingThePackerPort() throws IOException, CasketException {
		this.usingThePackerPort(DB.SQLITE);
		this.usingThePackerPort(DB.H2);
	}

	private void usingThePackerPort(DB dialect) throws IOException, CasketException {
		this.dbFile = Utility.createFile(this);
		Exception exc = null;
		try {
			// 1st: Get the session manager!

			SessionManager manager = PackerPort.PORT.sessionManager();

			// 2nd: Create a configuration!

			Configuration config = manager.createConfiguration();
			config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			config.setUri(this.dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");
			config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

			// 3rd: Build a domain!

			Domain dom = manager.mkDomain(config);
			manager.addEntity(dom, Club.class, Member.class, Department.class);
			manager.finalizeDomain(dom);

			// 4th: Open a session!

			Session session = manager.session(config);

			// 5th: Declare the used classes!

			session.declareClass(Department.class, Club.class, Member.class);

			// 6th: Work with the domain!

			Member[] members = { //
					new Member("Mary"), new Member("Patricia"), new Member("Jennifer"), //
					new Member("Linda"), new Member("Elizabeth"), new Member("Barbara"), //
					new Member("James"), new Member("Robert"), new Member("John"), //
					new Member("Michael"), new Member("David"), new Member("William") };

			Department[][] departments = { //
					{ //
							new Department("soccer"), new Department("tennis"), //
							new Department("aerobics"), new Department("baseball") }, //
					{ //
							new Department("soccer"), new Department("tennis"), //
							new Department("aerobics"), new Department("baseball") }//
			};

			Club[] clubs = { new Club("Lions"), new Club("Tigers") };

			/*
			 * For complex operations use begin and end transaction!
			 */
			session.beginTransaction();

			for (Member member : members)
				session.persist(member);
			for (int c = 0; c < clubs.length; c++)
				for (Department department : departments[c])
					session.persist(department);

			session.endTransaction();

			/*
			 * For simplicity, one can omit begin-end brackets. Without begin-end brackets,
			 * each operation is executed in a separate transaction.
			 */
			session.persist(clubs[0]);
			session.persist(clubs[1]);

			for (int c = 0; c < clubs.length; c++)
				for (Department department : departments[c])
					clubs[c].addDepartment(department);

			session.beginTransaction();
			for (Club club : clubs)
				session.persist(club);
			session.endTransaction();

			clubs[0].president(members[0]);
			clubs[0].vicePresident(members[1]);

			clubs[1].president(members[6]);
			clubs[1].vicePresident(members[7]);

			session.persist(clubs[0]);
			session.persist(clubs[1]);

			int i = 0;
			for (Member member : members) {
				clubs[0].addMember(member);
				clubs[1].addMember(member);
				departments[0][i % departments[0].length].addMember(member);
				departments[1][(i + 1) % departments[0].length].addMember(member);
				departments[0][(i + 2) % departments[0].length].addMember(member);
				departments[1][(i + 3) % departments[0].length].addMember(member);
				i++;
			}
			for (int j = 0; j < 2; j++) {
				for (Department department : departments[j]) {
					Set<Member> member = department.allMembers();
					if (member.isEmpty())
						department.setDirector(clubs[j].vicePresident());
					else
						department.setDirector(member.iterator().next());
				}
			}

			session.beginTransaction();
			session.persist(clubs[0]);
			session.persist(clubs[1]);

			// Attention: "persist" does not works in a deep manner. So departments are
			// assigned
			// to clubs. But every change inside a department must be persisted separately.
			for (int j = 0; j < 2; j++)
				for (Department department : departments[j])
					session.persist(department);

			session.endTransaction();

			manager.terminateAll();

			/*
			 * Part II: More sessions and existing domains!
			 */

			Session session1 = manager.session(config);
			Session session2 = manager.session(config);

			session1.declareClass(Department.class, Club.class, Member.class);
			session2.declareClass(Department.class, Club.class, Member.class);

			// 7th: Register an observer to stay up to date!

			Observer obs = new Observer();
			session2.register(obs);

			Set<Club> allClubs = session1.getAllObjects(Club.class);

			// Now we change the president of the Lions.

			Club club = null;
			for (Club aClub : allClubs)
				if (aClub.getName().equals("Lions"))
					club = aClub;

			Member aMember = null;
			for (Member member : club.allMembers())
				if ((club.president() != member) && (club.vicePresident() != member)) {
					aMember = member;
					break;
				}
			club.president(aMember);

			session1.persist(club);

			Assertions.assertTrue(Utility.waitForChange());

			// Nothing happened in session2!

			Assertions.assertTrue(obs.getChanged().isEmpty());

			// Now session 2 also knows clubs and members.

			Set<Club> allClubs2 = session2.getAllObjects(Club.class);
			Assertions.assertEquals(2, allClubs2.size());

			// Now we change the vice president of the Lions.

			aMember = null;
			for (Member member : club.allMembers())
				if ((club.president() != member) && (club.vicePresident() != member)) {
					aMember = member;
					break;
				}
			club.vicePresident(aMember);

			session1.persist(club);

			Assertions.assertTrue(Utility.waitForChange());

			Set<Object> changedObjs = obs.getChanged();
			Assertions.assertFalse(changedObjs.isEmpty());

			// Because entities also contain information about existing references, one also
			// gets some changed members and not only the changed association.

			// 8th: Close the session!

			manager.terminateAll();

			// That's all. Also try to delete members, departments and clubs. Or search for
			// specific departments...

		} catch (Exception e) {
			exc = e;
			e.printStackTrace();
		} finally {
			Files.deleteIfExists(this.dbFile.toPath());
		}
		Assertions.assertNull(exc);
	}

}

/**
 * A session observer, so one gets informed if something happens.
 */
class Observer implements SessionObserver {

	Set<Object> deletedObjects = new HashSet<>();
	Set<Object> changedObjects = new HashSet<>();

	@Override
	public synchronized void externDeleted(Object deleted) {
		this.deletedObjects.add(deleted);
	}

	@Override
	public synchronized void externChanged(Object changed) {
		this.changedObjects.add(changed);
	}

	public synchronized Set<Object> getDeleted() {
		try {
			return new HashSet<>(this.deletedObjects);
		} finally {
			this.deletedObjects.clear();
		}
	}

	public synchronized Set<Object> getChanged() {
		try {
			return new HashSet<>(this.changedObjects);
		} finally {
			this.changedObjects.clear();
		}
	}

	public synchronized boolean isEmpty() {
		return this.deletedObjects.isEmpty() && this.changedObjects.isEmpty();
	}

}

/**
 * Our domain: Clubs with members and different departments. Each club has a
 * president and a vice president. Each department has a director.
 */

@Entity()
final class Club {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@ManyToOne
	private Member president;

	@ManyToOne
	private Member vicePresident;

	@ManyToMany
	Set<Member> members = new HashSet<>();

	@ManyToMany
	Set<Department> departments = new HashSet<>();

	/**
	 * Each entity must have a default constructor. This constructor is used be the
	 * Object Casket system to create the necessary objects.
	 */
	@SuppressWarnings("unused")
	private Club() {
	}

	public Club(String name) {
		this.name = name;
	}

	/**
	 * In a multi-session application, access to an entity should be synchronized.
	 * Because the object casket synchronizes entities in the background.
	 */
	public synchronized String getName() {
		return this.name;
	}

	public synchronized void president(Member member) {
		this.president = member;
		this.members.add(this.president);
	}

	public synchronized Member president() {
		return this.president;
	}

	public synchronized void vicePresident(Member member) {
		this.vicePresident = member;
		this.members.add(this.vicePresident);
	}

	public synchronized Member vicePresident() {
		return this.vicePresident;
	}

	public synchronized void addDepartment(Department department) {
		this.departments.add(department);
		this.members.addAll(department.allMembers());
	}

	public synchronized void removeDepartment(Department department) {
		if (this.departments.remove(department))
			this.members.forEach(m -> department.removeMember(m));
	}

	public synchronized Set<Member> allMembers() {
		return new HashSet<>(this.members);
	}

	public synchronized void addMember(Member member) {
		this.members.add(member);
	}

	public synchronized boolean removeMember(Member member) {
		if (this.president.equals(member))
			return false;
		if (this.vicePresident.equals(member))
			return false;
		this.departments.forEach(d -> d.removeMember(member));
		this.departments.forEach(d -> {
			if (d.getDirector() == null)
				d.setDirector(this.vicePresident);
		});
		this.members.remove(member);
		return true;
	}

	@Override
	public synchronized String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Club %s: President is %s, \tVice Precident is %s \t(Departments: %d, Members: %d)\n", //
				this.name, this.president, this.vicePresident, //
				this.departments.size(), this.members.size()));
		for (Department department : this.departments)
			sb.append(department.toString());
		return sb.toString();
	}
}

@Entity()
final class Department {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@ManyToOne
	private Member director;

	@ManyToMany
	private Set<Member> members = new HashSet<>();

	@SuppressWarnings("unused")
	private Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public synchronized void addMember(Member member) {
		this.members.add(member);
	}

	public synchronized void removeMember(Member member) {
		if (this.director.equals(member))
			this.director = null;
		this.members.remove(member);
	}

	public synchronized void setDirector(Member member) {
		this.director = member;
		this.members.add(this.director);
	}

	public synchronized Member getDirector() {
		return this.director;
	}

	public synchronized Set<Member> allMembers() {
		return new HashSet<>(this.members);
	}

	@Override
	public synchronized String toString() {
		return String.format("\tDepartment %s: Director: %s, Members: %d\n", //
				this.name, this.director, this.members.size());
	}

}

@Entity()
final class Member {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@SuppressWarnings("unused")
	private Member() {
	}

	public Member(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
