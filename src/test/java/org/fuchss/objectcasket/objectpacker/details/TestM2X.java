package org.fuchss.objectcasket.objectpacker.details;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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

class TestM2X {

	@Test
	void createTablesTest() throws IOException, CasketException, InterruptedException {
		this.createTablesTest(DB.SQLITE);
		this.createTablesTest(DB.H2);
	}

	private void createTablesTest(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();

		Configuration config = manager.createConfiguration();

		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("user");
		config.setPassword("confidential");

		config.setFlag(Configuration.Flag.CREATE, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);

		manager.addEntity(dom, Member.class, Club.class, Section.class);

		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(Member.class, Club.class, Section.class);

		Member[] lionMembers = { new Member("Smith", "Liam"), new Member("Johnson", "Olivia"), //
				new Member("Brown", "James"), new Member("Brown", "Mary") };

		Member[] tigerMembers = { new Member("Smith", "Liam"), new Member("Johnson", "Olivia"), //
				new Member("Brown", "James"), new Member("Brown", "Mary") };

		Section[] lionSections = { new Section("soccer"), new Section("tennis") };
		Section[] tigerSections = { new Section("aerobics"), new Section("baseball") };

		Club club1 = new Club("Lions");
		Club club2 = new Club("Tigers");

		club1.address("Wild Str. 54 West");
		club2.address("Wild Str. 10 East");

		club1.president(lionMembers[0]);
		club2.president(tigerMembers[0]);
		club1.vicePresident(lionMembers[1]);
		club2.vicePresident(tigerMembers[1]);

		lionSections[0].setHead(lionMembers[2]);
		lionSections[1].setHead(lionMembers[3]);

		tigerSections[0].setHead(tigerMembers[2]);
		tigerSections[1].setHead(tigerMembers[3]);

		club1.addSection(lionSections[0]);
		club1.addSection(lionSections[1]);

		club2.addSection(tigerSections[0]);
		club2.addSection(tigerSections[1]);

		session.beginTransaction();
		session.persist(club1);
		session.persist(club2);
		session.endTransaction();

		manager.terminate(session);

		session = manager.session(config);
		session.declareClass(Member.class, Club.class, Section.class);

		Set<Club> clubs = session.getAllObjects(Club.class);

		Assertions.assertTrue(testClub(club1, clubs));
		Assertions.assertTrue(testClub(club2, clubs));
		manager.terminate(session);
		Files.deleteIfExists(dbFile.toPath());
	}

	static boolean testClub(Club club, Set<Club> clubs) {
		boolean result = false;
		for (Club cl : clubs) {
			if (cl.id == club.id) {
				result = cl.name.equals(club.name);
				result &= cl.president.id == club.president.id;
				result &= cl.president.firstName.equals(club.president.firstName);
				result &= cl.president.lastName.equals(club.president.lastName);
				result &= cl.vicePresident.id == club.vicePresident.id;
				result &= cl.vicePresident.firstName.equals(club.vicePresident.firstName);
				result &= cl.vicePresident.lastName.equals(club.vicePresident.lastName);
				result &= cl.members.size() == club.members.size();
				result &= cl.sections.size() == club.sections.size();
				for (Member mem : cl.members)
					result &= testMember(mem, club.members);
				for (Section sec : cl.sections)
					result &= testSection(sec, club.sections);
				break;
			}
		}
		return result;
	}

	static boolean testSection(Section section, Set<Section> sections) {
		boolean result = false;
		for (Section sec : sections) {
			if (sec.memberId == section.memberId) {
				result = sec.name.equals(section.name);
				result &= sec.head.id == section.head.id;
				result &= sec.head.firstName.equals(section.head.firstName);
				result &= sec.head.lastName.equals(section.head.lastName);
				result &= sec.members.size() == section.members.size();
				for (Member mem : sec.members)
					result &= testMember(mem, section.members);
				break;
			}
		}
		return result;
	}

	static boolean testMember(Member member, Set<Member> members) {
		boolean result = false;
		for (Member mem : members) {
			if (mem.id == member.id) {
				result = mem.firstName.equals(member.firstName);
				result &= mem.lastName.equals(member.lastName);
				break;
			}
		}
		return result;
	}

}

@Entity()
final class Club {

	@Id
	@GeneratedValue
	Integer id;

	String name;

	String address;

	@ManyToOne
	Member president;

	@ManyToOne
	Member vicePresident;

	@ManyToMany
	Set<Member> members = new HashSet<>();

	@ManyToMany
	Set<Section> sections = new HashSet<>();

	@SuppressWarnings("unused")
	private Club() {

	}

	public Club(String name) {
		this.name = name;
	}

	public void address(String address) {
		this.address = address;
	}

	public String address() {
		return this.address;
	}

	public void president(Member member) {
		this.president = member;
		this.members.add(this.president);
	}

	public Member president() {
		return this.president;
	}

	public void vicePresident(Member member) {
		this.vicePresident = member;
		this.members.add(this.vicePresident);
	}

	public Member vicePresident() {
		return this.vicePresident;
	}

	public void addSection(Section section) {
		this.sections.add(section);
		this.members.addAll(section.allMembers());
	}

	public void removeSection(Section section) {
		if (this.sections.remove(section))
			this.members.forEach(m -> section.removeMember(m));
	}

	public Set<Member> allMembers() {
		return new HashSet<>(this.members);
	}

	public void addMember(Member member) {
		this.members.add(member);
	}

	public boolean removeMember(Member member) {
		if (this.president.equals(member))
			return false;
		if (this.vicePresident.equals(member))
			return false;
		this.sections.forEach(sec -> sec.removeMember(member));
		this.sections.forEach(sec -> {
			if (sec.head() == null)
				sec.setHead(this.vicePresident);
		});
		this.members.remove(member);
		return true;
	}

}

@Entity()
final class Member {

	@Id
	@GeneratedValue
	Integer id;

	String lastName;

	String firstName;

	@SuppressWarnings("unused")
	private Member() { // default constructor used by leanOpack

	}

	public Member(String lName, String fName) {
		this.lastName = lName;
		this.firstName = fName;
	}

}

@Entity()
final class Section {

	@Id
	@GeneratedValue
	Integer memberId;

	String name;

	@ManyToOne
	Member head;

	@ManyToMany
	Set<Member> members = new HashSet<>();

	@SuppressWarnings("unused")
	private Section() { // default constructor used by leanOpack

	}

	public Section(String name) {
		this.name = name;
	}

	public void addMember(Member member) {
		this.members.add(member);
	}

	public void removeMember(Member member) {
		if (this.head.equals(member))
			this.head = null;
		this.members.remove(member);
	}

	public void setHead(Member member) {
		this.head = member;
		this.members.add(this.head);
	}

	public Member head() {
		return this.head;
	}

	public Set<Member> allMembers() {
		return new HashSet<>(this.members);
	}

}
