package org.fuchss.objectcasket.objectpacker.details;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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

class TestDate {

	@Test
	void dateTest() throws IOException, CasketException, InterruptedException {
		// this.dateTest(DB.SQLITE);
		this.dateTest(DB.H2);
	}

	private void dateTest(DB dialect) throws IOException, CasketException, InterruptedException {

		try {
			File dbFile = Utility.createFile(this);

			SessionManager manager = PackerPort.PORT.sessionManager();

			Configuration config = manager.createConfiguration();

			config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			config.setUri(dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");

			config.setFlag(Configuration.Flag.CREATE, Configuration.Flag.WRITE, Configuration.Flag.SESSIONS);

			Domain dom = manager.mkDomain(config);
			manager.addEntity(dom, DateEntity.class);

			manager.finalizeDomain(dom);

			Session session = manager.session(config);
			session.declareClass(DateEntity.class);

			DateEntity entity = new DateEntity();
			entity.name = "test";

			Calendar cal = Calendar.getInstance();
			cal.set(1967, 0, 6);
			entity.date = cal.getTime();

			session.persist(entity);

			Set<DateEntity> entities = session.getAllObjects(DateEntity.class);

			Assertions.assertNotNull(entities);
		} catch (CasketException e) {
			e.printStackTrace();
		}
	}
}

@Entity
final class DateEntity {

	@Id
	@GeneratedValue
	private Integer id;

	protected DateEntity() {

	}

	protected String name;

	protected Date date;

}
