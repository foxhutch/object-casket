package org.fuchss.objectcasket.primarykeys;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_VARCHAR_TEXT_CHAR extends TestBase {

	static final int ROWS = 10;

	@Test
	public void testPkVarchar() {
		try {
			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_VARCHAR.class, //
					PK_TEXT.class, //
					PK_CHAR1.class, //
					PK_CHAR2.class);
			session.open();

			Set<PK_VARCHAR> sql1 = new HashSet<>();
			PK_VARCHAR sql1_obj = null;
			Set<PK_TEXT> sql2 = new HashSet<>();
			PK_TEXT sql2_obj = null;

			Set<PK_CHAR1> sql3 = new HashSet<>();
			PK_CHAR1 sql3_obj = null;
			Set<PK_CHAR2> sql4 = new HashSet<>();
			PK_CHAR2 sql4_obj = null;

			for (int i = 0; i < ROWS; i++) {
				session.persist(sql1_obj = new PK_VARCHAR("" + (i + 1)));
				sql1.add(sql1_obj);
				session.persist(sql2_obj = new PK_TEXT("" + (i + 1)));
				sql2.add(sql2_obj);

				session.persist(sql3_obj = new PK_CHAR1((char) (65 + i)));
				sql3.add(sql3_obj);
				session.persist(sql4_obj = new PK_CHAR2((char) (97 + i)));
				sql4.add(sql4_obj);

			}

			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_VARCHAR.class, //
					PK_TEXT.class, //
					PK_CHAR1.class, //
					PK_CHAR2.class);
			session.open();

			Set<PK_VARCHAR> sql_1 = session.getAllObjects(PK_VARCHAR.class);
			Set<PK_TEXT> sql_2 = session.getAllObjects(PK_TEXT.class);
			Set<PK_CHAR1> sql_3 = session.getAllObjects(PK_CHAR1.class);
			Set<PK_CHAR2> sql_4 = session.getAllObjects(PK_CHAR2.class);

			for (PK_VARCHAR cc : sql_1) System.out.println(cc);
			for (PK_TEXT cc : sql_2)    System.out.println(cc);
			for (PK_CHAR1 cc : sql_3)   System.out.println(cc);
			for (PK_CHAR2 cc : sql_4)   System.out.println(cc);

			Assert.assertTrue(sql1_obj.check(sql1, sql_1));
			Assert.assertTrue(sql2_obj.check(sql2, sql_2));
			Assert.assertTrue(sql3_obj.check(sql3, sql_3));
			Assert.assertTrue(sql4_obj.check(sql4, sql_4));

			this.storePort.sessionManager().terminate(session);
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
