package org.fuchss.objectcasket.objectpacker.api;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

class TestDomain {

	File dbFile;

	@Test
	void createAndEdit() throws IOException, CasketException, InterruptedException {
		this.createAndEdit(DB.SQLITE);
		this.createAndEdit(DB.H2);
	}

	private void createAndEdit(DB dialect) throws IOException, CasketException, InterruptedException {
		this.dbFile = Utility.createFile(this);
		try {

			SessionManager manager = PackerPort.PORT.sessionManager();
			Configuration config = manager.createConfiguration();
			config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			config.setUri(this.dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");
			config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

			Domain dom = manager.mkDomain(config);
			manager.addEntity(dom, DomainTable.class);
			manager.finalizeDomain(dom);
			manager.terminateAll();

			dom = manager.editDomain(config);
			manager.addEntity(dom, DomainTable2.class);
			manager.finalizeDomain(dom);
			manager.terminateAll();

			Session session = manager.session(config);
			session.declareClass(DomainTable.class, DomainTable2.class);
			DomainTable d1 = new DomainTable(1, "dom1");
			DomainTable2 d2 = new DomainTable2(2, "dom2");
			session.persist(d1);
			session.persist(d2);
			manager.terminateAll();

			session = manager.session(config);
			session.declareClass(DomainTable.class, DomainTable2.class);
			Set<DomainTable> set1 = session.getAllObjects(DomainTable.class);
			Set<DomainTable2> set2 = session.getAllObjects(DomainTable2.class);
			Assertions.assertEquals(1, set1.size());
			Assertions.assertEquals(1, set2.size());
			manager.terminateAll();

		} finally {
			Utility.deleteFile(this.dbFile);
		}
	}

}

@Entity()
@Table(name = "table")
final class DomainTable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	public DomainTable() {
	}

	public DomainTable(Integer x, String y) {
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

@Entity()
@Table(name = "table2")
final class DomainTable2 {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	public DomainTable2() {
	}

	public DomainTable2(Integer x, String y) {
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
