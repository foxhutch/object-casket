package org.fuchss.objectcasket.tables;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.tables.objects.DL;
import org.fuchss.objectcasket.tables.objects.DR;
import org.junit.jupiter.api.Test;

public class TestM2OTables extends TestBase {

	@Test
	public void testC() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				DL.class, //
				DR.class);
		session.open();

		DL[] d = new DL[10];
		DR dr1 = new DR();
		for (int i = 0; i < d.length; i++) {
			(d[i] = new DL()).many2one = dr1;
			session.persist(d[i]);
		}

		this.storePort.sessionManager().terminate(session);
	}

}
