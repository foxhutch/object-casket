package org.fuchss.objectcasket.primarykeys;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_NUMERIC extends TestBase {

	static final int ROWS = 10;

	@Test
	public void testPkInt() {
		try {
			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_NUMERIC1.class, //
					PK_NUMERIC2.class, //
					PK_NUMERIC3.class, //
					PK_NUMERIC4.class, //
					PK_NUMERIC5.class, //
					PK_NUMERIC6.class, //
					PK_NUMERIC7.class, //
					PK_NUMERIC8.class, //
					PK_NUMERIC9.class, //
					PK_NUMERIC10.class, //
					PK_NUMERIC11.class, //
					PK_NUMERIC12.class);
			session.open();

			Set<PK_NUMERIC1> sql1 = new HashSet<>();
			Set<PK_NUMERIC2> sql2 = new HashSet<>();
			Set<PK_NUMERIC3> sql3 = new HashSet<>();
			Set<PK_NUMERIC4> sql4 = new HashSet<>();
			Set<PK_NUMERIC5> sql5 = new HashSet<>();
			Set<PK_NUMERIC6> sql6 = new HashSet<>();
			Set<PK_NUMERIC7> sql7 = new HashSet<>();
			Set<PK_NUMERIC8> sql8 = new HashSet<>();
			Set<PK_NUMERIC9> sql9 = new HashSet<>();
			Set<PK_NUMERIC10> sql10 = new HashSet<>();
			Set<PK_NUMERIC11> sql11 = new HashSet<>();
			Set<PK_NUMERIC12> sql12 = new HashSet<>();

			PK_NUMERIC1 sql1_obj = null;
			PK_NUMERIC2 sql2_obj = null;
			PK_NUMERIC3 sql3_obj = null;
			PK_NUMERIC4 sql4_obj = null;
			PK_NUMERIC5 sql5_obj = null;
			PK_NUMERIC6 sql6_obj = null;
			PK_NUMERIC7 sql7_obj = null;
			PK_NUMERIC8 sql8_obj = null;
			PK_NUMERIC9 sql9_obj = null;
			PK_NUMERIC10 sql10_obj = null;
			PK_NUMERIC11 sql11_obj = null;
			PK_NUMERIC12 sql12_obj = null;

			for (byte i = 0; i < ROWS; i++) {
				session.persist(sql1_obj = new PK_NUMERIC1(i + 1));
				session.persist(sql2_obj = new PK_NUMERIC2(i + 1));
				session.persist(sql3_obj = new PK_NUMERIC3((short) (i + 1)));
				session.persist(sql4_obj = new PK_NUMERIC4((byte) (i + 1)));
				session.persist(sql5_obj = new PK_NUMERIC5(i + 1));
				session.persist(sql6_obj = new PK_NUMERIC6(i + 1));
				session.persist(sql7_obj = new PK_NUMERIC7((short) (i + 1)));
				session.persist(sql8_obj = new PK_NUMERIC8((byte) (i + 1)));
				session.persist(sql9_obj = new PK_NUMERIC9(i + 1));
				session.persist(sql10_obj = new PK_NUMERIC10(i + 1));
				session.persist(sql11_obj = new PK_NUMERIC11(i + 1));
				session.persist(sql12_obj = new PK_NUMERIC12(i + 1));

				sql1.add(sql1_obj);
				sql2.add(sql2_obj);
				sql3.add(sql3_obj);
				sql4.add(sql4_obj);
				sql5.add(sql5_obj);
				sql6.add(sql6_obj);
				sql7.add(sql7_obj);
				sql8.add(sql8_obj);
				sql9.add(sql9_obj);
				sql10.add(sql10_obj);
				sql11.add(sql11_obj);
				sql12.add(sql12_obj);
			}
			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_NUMERIC1.class, //
					PK_NUMERIC2.class, //
					PK_NUMERIC3.class, //
					PK_NUMERIC4.class, //
					PK_NUMERIC5.class, //
					PK_NUMERIC6.class, //
					PK_NUMERIC7.class, //
					PK_NUMERIC8.class, //
					PK_NUMERIC9.class, //
					PK_NUMERIC10.class, //
					PK_NUMERIC11.class, //
					PK_NUMERIC12.class);
			session.open();

			Set<PK_NUMERIC1> sql_1 = session.getAllObjects(PK_NUMERIC1.class);
			Set<PK_NUMERIC2> sql_2 = session.getAllObjects(PK_NUMERIC2.class);
			Set<PK_NUMERIC3> sql_3 = session.getAllObjects(PK_NUMERIC3.class);
			Set<PK_NUMERIC4> sql_4 = session.getAllObjects(PK_NUMERIC4.class);
			Set<PK_NUMERIC5> sql_5 = session.getAllObjects(PK_NUMERIC5.class);
			Set<PK_NUMERIC6> sql_6 = session.getAllObjects(PK_NUMERIC6.class);
			Set<PK_NUMERIC7> sql_7 = session.getAllObjects(PK_NUMERIC7.class);
			Set<PK_NUMERIC8> sql_8 = session.getAllObjects(PK_NUMERIC8.class);
			Set<PK_NUMERIC9> sql_9 = session.getAllObjects(PK_NUMERIC9.class);
			Set<PK_NUMERIC10> sql_10 = session.getAllObjects(PK_NUMERIC10.class);
			Set<PK_NUMERIC11> sql_11 = session.getAllObjects(PK_NUMERIC11.class);
			Set<PK_NUMERIC12> sql_12 = session.getAllObjects(PK_NUMERIC12.class);

			
			
			
			for (PK_NUMERIC1 cc : sql_1)	System.out.println(cc);
			for (PK_NUMERIC2 cc : sql_2)	System.out.println(cc);
			for (PK_NUMERIC3 cc : sql_3)	System.out.println(cc);
			for (PK_NUMERIC4 cc : sql_4)	System.out.println(cc);
			for (PK_NUMERIC5 cc : sql_5)	System.out.println(cc);
			for (PK_NUMERIC6 cc : sql_6)	System.out.println(cc);
			for (PK_NUMERIC7 cc : sql_7)	System.out.println(cc);
			for (PK_NUMERIC8 cc : sql_8)	System.out.println(cc);
			for (PK_NUMERIC9 cc : sql_9)	System.out.println(cc);
			for (PK_NUMERIC10 cc : sql_10)	System.out.println(cc);
			for (PK_NUMERIC11 cc : sql_11)	System.out.println(cc);
			for (PK_NUMERIC12 cc : sql_12)	System.out.println(cc);
			
			
			Assert.assertTrue(sql1_obj.check(sql1, sql_1));
			Assert.assertTrue(sql2_obj.check(sql2, sql_2));
			Assert.assertTrue(sql3_obj.check(sql3, sql_3));
			Assert.assertTrue(sql4_obj.check(sql4, sql_4));
			Assert.assertTrue(sql5_obj.check(sql5, sql_5));
			Assert.assertTrue(sql6_obj.check(sql6, sql_6));
			Assert.assertTrue(sql7_obj.check(sql7, sql_7));
			Assert.assertTrue(sql8_obj.check(sql8, sql_8));
			Assert.assertTrue(sql9_obj.check(sql9, sql_9));
			Assert.assertTrue(sql10_obj.check(sql10, sql_10));
			Assert.assertTrue(sql11_obj.check(sql11, sql_11));
			Assert.assertTrue(sql12_obj.check(sql12, sql_12));

			this.storePort.sessionManager().terminate(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
