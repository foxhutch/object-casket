package org.fuchss.objectcasket.primarykeys;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_INTEGER extends TestBase {

	static final int ROWS = 10;

	@Test
	public void testPkInt() {
		try {
			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_INTEGER1.class, //
					PK_INTEGER2.class, //
					PK_INTEGER3.class, //
					PK_INTEGER4.class, //
					PK_INTEGER5.class, //
					PK_INTEGER6.class, //
					PK_INTEGER7.class, //
					PK_INTEGER8.class);
			session.open();

			Set<PK_INTEGER1> sql1 = new HashSet<>();
			Set<PK_INTEGER2> sql2 = new HashSet<>();
			Set<PK_INTEGER3> sql3 = new HashSet<>();
			Set<PK_INTEGER4> sql4 = new HashSet<>();
			Set<PK_INTEGER5> sql5 = new HashSet<>();
			Set<PK_INTEGER6> sql6 = new HashSet<>();
			Set<PK_INTEGER7> sql7 = new HashSet<>();
			Set<PK_INTEGER8> sql8 = new HashSet<>();

			PK_INTEGER1 sql1_obj = null;
			PK_INTEGER2 sql2_obj = null;
			PK_INTEGER3 sql3_obj = null;
			PK_INTEGER4 sql4_obj = null;
			PK_INTEGER5 sql5_obj = null;
			PK_INTEGER6 sql6_obj = null;
			PK_INTEGER7 sql7_obj = null;
			PK_INTEGER8 sql8_obj = null;

			for (byte i = 0; i < ROWS; i++) {
				session.persist(sql1_obj = new PK_INTEGER1());
				session.persist(sql2_obj = new PK_INTEGER2());
				session.persist(sql3_obj = new PK_INTEGER3());
				session.persist(sql4_obj = new PK_INTEGER4());
				session.persist(sql5_obj = new PK_INTEGER5(i + 1));
				session.persist(sql6_obj = new PK_INTEGER6(i + 1));
				session.persist(sql7_obj = new PK_INTEGER7((short) (i + 1)));
				session.persist(sql8_obj = new PK_INTEGER8((byte) (i + 1)));

				sql1.add(sql1_obj);
				sql2.add(sql2_obj);
				sql3.add(sql3_obj);
				sql4.add(sql4_obj);
				sql5.add(sql5_obj);
				sql6.add(sql6_obj);
				sql7.add(sql7_obj);
				sql8.add(sql8_obj);
			}
			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_INTEGER1.class, //
					PK_INTEGER2.class, //
					PK_INTEGER3.class, //
					PK_INTEGER4.class, //
					PK_INTEGER5.class, //
					PK_INTEGER6.class, //
					PK_INTEGER7.class, //
					PK_INTEGER8.class);
			session.open();

			Set<PK_INTEGER1> sql_1 = session.getAllObjects(PK_INTEGER1.class);
			Set<PK_INTEGER2> sql_2 = session.getAllObjects(PK_INTEGER2.class);
			Set<PK_INTEGER3> sql_3 = session.getAllObjects(PK_INTEGER3.class);
			Set<PK_INTEGER4> sql_4 = session.getAllObjects(PK_INTEGER4.class);
			Set<PK_INTEGER5> sql_5 = session.getAllObjects(PK_INTEGER5.class);
			Set<PK_INTEGER6> sql_6 = session.getAllObjects(PK_INTEGER6.class);
			Set<PK_INTEGER7> sql_7 = session.getAllObjects(PK_INTEGER7.class);
			Set<PK_INTEGER8> sql_8 = session.getAllObjects(PK_INTEGER8.class);

			for (PK_INTEGER1 cc : sql_1)	System.out.println(cc);
			for (PK_INTEGER2 cc : sql_2)	System.out.println(cc);
			for (PK_INTEGER3 cc : sql_3)	System.out.println(cc);
			for (PK_INTEGER4 cc : sql_4)	System.out.println(cc);
			for (PK_INTEGER5 cc : sql_5)	System.out.println(cc);
			for (PK_INTEGER6 cc : sql_6)	System.out.println(cc);
			for (PK_INTEGER7 cc : sql_7)	System.out.println(cc);
			for (PK_INTEGER8 cc : sql_8)	System.out.println(cc);

			Assert.assertTrue(sql1_obj.check(sql1, sql_1));
			Assert.assertTrue(sql2_obj.check(sql2, sql_2));
			Assert.assertTrue(sql3_obj.check(sql3, sql_3));
			Assert.assertTrue(sql4_obj.check(sql4, sql_4));
			Assert.assertTrue(sql5_obj.check(sql5, sql_5));
			Assert.assertTrue(sql6_obj.check(sql6, sql_6));
			Assert.assertTrue(sql7_obj.check(sql7, sql_7));
			Assert.assertTrue(sql8_obj.check(sql8, sql_8));

			this.storePort.sessionManager().terminate(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
