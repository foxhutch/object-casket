package org.fuchss.objectcasket.primarykeys;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_DOUBLE_AND_FLOAT extends TestBase {

	static final int ROWS = 1;

	@Test
	public void testPkVarchar() {
		try {
			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_DOUBLE1.class, //
					PK_DOUBLE2.class, //
					PK_FLOAT1.class, //
					PK_FLOAT2.class);
			session.open();

			Set<PK_DOUBLE1> sql1 = new HashSet<>();
			Set<PK_DOUBLE2> sql2 = new HashSet<>();
			Set<PK_FLOAT1> sql3 = new HashSet<>();
			Set<PK_FLOAT2> sql4 = new HashSet<>();

			PK_DOUBLE1 sql1_obj = null;
			PK_DOUBLE2 sql2_obj = null;
			PK_FLOAT1 sql3_obj = null;
			PK_FLOAT2 sql4_obj = null;

			for (int i = 0; i < ROWS; i++) {
				session.persist(sql1_obj = new PK_DOUBLE1((i + 1) * 0.1));
				session.persist(sql2_obj = new PK_DOUBLE2((i + 1) * 0.1));
				session.persist(sql3_obj = new PK_FLOAT1((float) ((i + 1) * 0.1)));
				session.persist(sql4_obj = new PK_FLOAT2((float) ((i + 1) * 0.1)));
				sql1.add(sql1_obj);
				sql2.add(sql2_obj);
				sql3.add(sql3_obj);
				sql4.add(sql4_obj);
			}

			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					PK_DOUBLE1.class, //
					PK_DOUBLE2.class, //
					PK_FLOAT1.class, //
					PK_FLOAT2.class);
			session.open();

			Set<PK_DOUBLE1> sql_1 = session.getAllObjects(PK_DOUBLE1.class);
			Set<PK_DOUBLE2> sql_2 = session.getAllObjects(PK_DOUBLE2.class);
			Set<PK_FLOAT1> sql_3 = session.getAllObjects(PK_FLOAT1.class);
			Set<PK_FLOAT2> sql_4 = session.getAllObjects(PK_FLOAT2.class);

			for (PK_DOUBLE1 cc : sql_1)
				System.out.println(cc);
			for (PK_DOUBLE2 cc : sql_2)
				System.out.println(cc);
			for (PK_FLOAT1 cc : sql_3)
				System.out.println(cc);
			for (PK_FLOAT2 cc : sql_4)
				System.out.println(cc);

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
