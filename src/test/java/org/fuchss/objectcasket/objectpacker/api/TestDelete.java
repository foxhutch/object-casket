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

class TestDelete {

	@Test
	void deleteTest() throws IOException, CasketException, InterruptedException {
		this.deleteTest(DB.SQLITE);
		this.deleteTest(DB.H2);
	}

	private void deleteTest(DB dialect) throws IOException, CasketException, InterruptedException {

		File dbFile = Utility.createFile(this);

		SessionManager manager = PackerPort.PORT.sessionManager();
		Configuration config = manager.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

		Domain dom = manager.mkDomain(config);
		manager.addEntity(dom, TestClassDelete.class);
		manager.finalizeDomain(dom);

		Session session = manager.session(config);
		session.declareClass(TestClassDelete.class);

		TestClassDelete[] a = new TestClassDelete[] { new TestClassDelete(10, "ABC10"), new TestClassDelete(20, "ABC20"), new TestClassDelete(30, "ABC30"), new TestClassDelete(40, "ABC40") };

		session.beginTransaction();
		session.persist(a[0]);
		session.persist(a[1]);
		session.persist(a[2]);
		session.persist(a[3]);
		session.endTransaction();

		session.beginTransaction();
		session.delete(a[0]);
		session.delete(a[1]);
		session.endTransaction();

		manager.terminate(session);
		boolean res = Files.deleteIfExists(dbFile.toPath());
		Assertions.assertTrue(res);

	}
}

@Entity()
@Table(name = "table")
final class TestClassDelete {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	public TestClassDelete() {
	}

	public TestClassDelete(Integer x, String y) {
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

}
