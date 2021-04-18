package org.fuchss.objectcasket.primarykeys;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_BOOL extends TestBase {

	@Test
	public void testPkInt() {
		try {
			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_BOOL1.class, //
					PK_BOOL2.class);
			session.open();

			Set<PK_BOOL1> sql1 = new HashSet<>();
			Set<PK_BOOL2> sql2 = new HashSet<>();

			PK_BOOL1 sql1_obj = null;
			PK_BOOL2 sql2_obj = null;

			session.persist(sql1_obj = new PK_BOOL1(true));
			sql1.add(sql1_obj);
			session.persist(sql1_obj = new PK_BOOL1(false));
			sql1.add(sql1_obj);
			session.persist(sql2_obj = new PK_BOOL2(true));
			sql2.add(sql2_obj);
			session.persist(sql2_obj = new PK_BOOL2(false));
			sql2.add(sql2_obj);

			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_BOOL1.class, //
					PK_BOOL2.class);
			session.open();

			Set<PK_BOOL1> sql_1 = session.getAllObjects(PK_BOOL1.class);
			Set<PK_BOOL2> sql_2 = session.getAllObjects(PK_BOOL2.class);

			for (PK_BOOL1 cc : sql_1)
				System.out.println(cc);
			for (PK_BOOL2 cc : sql_2)
				System.out.println(cc);

			Assert.assertTrue(sql1_obj.check(sql1, sql_1));
			Assert.assertTrue(sql2_obj.check(sql2, sql_2));

			this.storePort.sessionManager().terminate(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}