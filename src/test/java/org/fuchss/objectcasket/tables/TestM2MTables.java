package org.fuchss.objectcasket.tables;

import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.tables.objects.EL;
import org.fuchss.objectcasket.tables.objects.EL1;
import org.fuchss.objectcasket.tables.objects.EL1xER1;
import org.fuchss.objectcasket.tables.objects.ELxER;
import org.fuchss.objectcasket.tables.objects.ER;
import org.fuchss.objectcasket.tables.objects.ER1;
import org.junit.Assert;
import org.junit.Test;

public class TestM2MTables extends TestBase {

	@Test
	public void test() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				ELxER.class, //
				EL.class, //
				ER.class);
		session.open();

		EL e1 = new EL();
		EL e2 = new EL();
		ER[] er = new ER[10];
		for (int i = 0; i < er.length; i++) {
			e1.m2m.add(er[i] = new ER());
		}
		session.persist(e1);

		for (int i = 0; i < er.length; i++) {
			e2.m2m.add(er[i]);
			session.persist(e2);
			Assert.assertTrue(e1.m2m.contains(er[i]));

		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				ELxER.class, //
				EL.class, //
				ER.class);
		session.open();

		Set<EL> es = session.getAllObjects(EL.class);
		Assert.assertTrue(es.size() == 2);

		for (EL e : es) {
			Assert.assertTrue(e.m2m.size() == 10);
		}
		this.storePort.sessionManager().terminate(session);

	}

	@Test
	public void test2() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				EL1xER1.class, //
				EL1.class, //
				ER1.class);
		session.open();

		EL1 e1 = new EL1();
		EL1 e2 = new EL1();
		ER1[] er = new ER1[10];
		for (int i = 0; i < er.length; i++) {
			e1.m2m.add(er[i] = new ER1());
		}
		session.persist(e1);

		for (int i = 0; i < er.length; i++) {
			e2.m2m.add(er[i]);
			session.persist(e2);
			Assert.assertTrue(e1.m2m.contains(er[i]));
			Assert.assertTrue(er[i].m2m.contains(e2));
			Assert.assertTrue(er[i].m2m.contains(e1));
		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				EL1xER1.class, //
				EL1.class, //
				ER1.class);
		session.open();

		Set<EL1> es = session.getAllObjects(EL1.class);

		for (EL1 e : es) {
			Assert.assertTrue(e.m2m.size() == 10);
			for (ER1 r : e.m2m)
				Assert.assertTrue(r.m2m.size() == 2);
		}

		this.storePort.sessionManager().terminate(session);

	}

}