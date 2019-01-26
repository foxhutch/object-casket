package org.fuchss.objectcasket.api;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.tablemodule.port.TableModuleException;
import org.junit.Assert;
import org.junit.Test;

public class TestWrongTable extends TestBase {
	@Test

	public void test_wrongTableName() throws IOException, ObjectCasketException {
		Throwable error = null;
		Session session = null;
		try {
			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(A.class);
			session.open();
			this.storePort.sessionManager().terminate(session);
		} catch (ObjectCasketException e) {
			error = e.getCause();
		}
		if ((error == null) || !(error instanceof TableModuleException)) {
			Assert.fail("Wrong table name not detected!");
		}

	}

	@Entity
	@Table(name = "A B")
	public static final class A {
		@Id
		@GeneratedValue
		public Integer id;
	}

}
