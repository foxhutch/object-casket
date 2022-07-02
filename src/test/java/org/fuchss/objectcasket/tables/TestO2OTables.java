package org.fuchss.objectcasket.tables;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.tables.objects.AL;
import org.fuchss.objectcasket.tables.objects.AR;
import org.fuchss.objectcasket.tables.objects.BL;
import org.fuchss.objectcasket.tables.objects.BR;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestO2OTables extends TestBase {

	@Test
	public void testA() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				AL.class, //
				AR.class);
		session.open();

		AL a1 = new AL();
		AL a2 = new AL();
		AR a3 = new AR();

		a1.one2one = a3;
		session.persist(a1);

		a1.one2one = null;
		session.persist(a1);

		a2.one2one = a3;
		session.persist(a2);

		Assertions.assertTrue(a1.one2one == null);

		this.storePort.sessionManager().terminate(session);

	}

	@Test
	public void testB() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BL.class, //
				BR.class);
		session.open();

		BL b1 = new BL();
		BL b2 = new BL();
		BR b3 = new BR();

		b1.one2one = b3;
		session.persist(b1);

		Assertions.assertTrue(b3.one2one == b1);

		b1.one2one = null;
		session.persist(b1);

		b2.one2one = b3;
		session.persist(b2);

		Assertions.assertTrue(b1.one2one == null);

		this.storePort.sessionManager().terminate(session);

	}

}