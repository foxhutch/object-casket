package org.fuchss.objectcasket.objectpacker.details;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.fuchss.objectcasket.common.CasketError;
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

class TestDelete {

	@Test
	void deleteTest() throws IOException, CasketException, InterruptedException {
		this.deleteTest(DB.SQLITE);
		// this.deleteTest(DB.H2);
	}

	private void deleteTest(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();

		Configuration config = manager.createConfiguration();

		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");

		config.setFlag(Configuration.Flag.CREATE, Configuration.Flag.WRITE, Configuration.Flag.SESSIONS);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, EDT1.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(EDT1.class);

		EDT1[] edt1 = { new EDT1("A"), new EDT1("B"), new EDT1("C"), new EDT1("D"), new EDT1("E"), new EDT1("F"), new EDT1("G") };

		for (EDT1 e : edt1) {
			session.persist(e);
		}
		for (EDT1 e : edt1) {
			e.e1 = e;
			e.e1s.addAll(Arrays.asList(edt1));
			session.persist(e);
		}

		CasketException exc = null;
		try {
			session.delete(edt1[0]);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNotNull(exc);
		Assertions.assertEquals(CasketError.CE1.OBJECT_IN_USE, exc.error());

		for (EDT1 e : edt1) {
			e.e1s.remove(edt1[0]);
		}
		exc = null;
		try {
			session.delete(edt1[0]);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNotNull(exc);
		Assertions.assertEquals(CasketError.CE1.OBJECT_IN_USE, exc.error());

		for (EDT1 e : edt1) {
			session.persist(e);
		}

		exc = null;
		try {
			session.delete(edt1[0]);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNotNull(exc);
		Assertions.assertEquals(CasketError.CE1.OBJECT_IN_USE, exc.error());

		edt1[0].e1 = null;
		edt1[0].e1s.clear();
		exc = null;
		try {
			session.delete(edt1[0]);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNull(exc);

		Session session2 = manager.session(config);
		session2.declareClass(EDT1.class);

		Set<EDT1> otherEdt1 = session2.getAllObjects(EDT1.class);

		Assertions.assertEquals(edt1.length - 1, otherEdt1.size());

		manager.terminateAll();
		Files.deleteIfExists(dbFile.toPath());
	}
}

@Entity()
final class EDT1 {

	@Id
	@GeneratedValue
	Integer id;

	String name;

	@ManyToOne
	EDT1 e1;

	@ManyToMany
	Set<EDT1> e1s = new HashSet<>();

	@SuppressWarnings("unused")
	private EDT1() {
	}

	EDT1(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String[] str = new String[this.e1s.size()];
		int i = 0;
		for (EDT1 e : this.e1s)
			str[i++] = e.name;
		return String.format("(id: %d\tname: %s\tself = %s\tothers = %s) ", this.id, this.name, (this.e1 == null) ? "null" : this.e1.name, Arrays.toString(str));
	}

}
