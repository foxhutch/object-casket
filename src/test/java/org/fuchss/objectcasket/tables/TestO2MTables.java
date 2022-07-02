package org.fuchss.objectcasket.tables;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.tables.objects.C1L;
import org.fuchss.objectcasket.tables.objects.C1R;
import org.fuchss.objectcasket.tables.objects.CL;
import org.fuchss.objectcasket.tables.objects.CR;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestO2MTables extends TestBase {

	@Test
	public void testC() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				CL.class, //
				CR.class);
		session.open();

		CL c1 = new CL();
		CL c2 = new CL();
		CR[] cr = new CR[10];
		for (int i = 0; i < cr.length; i++) {
			c1.one2many.add(cr[i] = new CR());
		}
		session.persist(c1);

		for (int i = 0; i < cr.length; i++) {
			c2.one2many.add(cr[i]);
			session.persist(c2);
			Assertions.assertFalse(c1.one2many.contains(cr[i]));

		}
		this.storePort.sessionManager().terminate(session);
	}

	@Test
	public void testC1() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				C1L.class, //
				C1R.class);
		session.open();

		C1L c1 = new C1L();
		C1L c2 = new C1L();
		C1R[] cr = new C1R[10];
		for (int i = 0; i < cr.length; i++) {
			c1.one2many.add(cr[i] = new C1R());
		}
		session.persist(c1);
		for (int i = 0; i < cr.length; i++) {
			Assertions.assertTrue(cr[i].many2One == c1);
		}

		for (int i = 0; i < cr.length; i++) {
			c2.one2many.add(cr[i]);
			session.persist(c2);
			Assertions.assertFalse(c1.one2many.contains(cr[i]));
		}
		this.storePort.sessionManager().terminate(session);
	}

}
