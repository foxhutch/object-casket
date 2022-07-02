package org.fuchss.objectcasket.sqltypes;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_DOUBLE1;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_DOUBLE2;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_FLOAT1;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_FLOAT2;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_REAL1;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_REAL2;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_REAL3;
import org.fuchss.objectcasket.sqltypes.objects.real.PK_REAL4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test_DOUBLE_FLOAT_REAL extends TestBase {

	static final int ROWS = 10;

	/**
	 * This test checks that SQL double, float and real are suitable as primary keys
	 * and attributes.
	 *
	 * First part: For each Java class and type mapped to an SQL double, float, or
	 * real (Double, Float, double, float), 10 objects are created and stored.
	 *
	 * Second part: After the session is terminated, the database is opened a second
	 * time and all stored objects are read. Then we checked that all objects were
	 * found.
	 *
	 * Third part: After the session is terminated, the database is opened a third
	 * time and we delete all stored objects. Then we close the session again and
	 * check that the database is now empty.
	 *
	 * @throws Exception
	 */

	@Test
	public void test() throws Exception {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DOUBLE1.class, //
				PK_DOUBLE2.class, //
				PK_FLOAT1.class, //
				PK_FLOAT2.class, //
				PK_REAL1.class, //
				PK_REAL2.class, //
				PK_REAL3.class, //
				PK_REAL4.class);
		session.open();

		Set<PK_DOUBLE1> sql1 = new HashSet<>();
		Set<PK_DOUBLE2> sql2 = new HashSet<>();
		Set<PK_FLOAT1> sql3 = new HashSet<>();
		Set<PK_FLOAT2> sql4 = new HashSet<>();

		Set<PK_REAL1> sql5 = new HashSet<>();
		Set<PK_REAL2> sql6 = new HashSet<>();
		Set<PK_REAL3> sql7 = new HashSet<>();
		Set<PK_REAL4> sql8 = new HashSet<>();

		PK_DOUBLE1 sql1_obj = null;
		PK_DOUBLE2 sql2_obj = null;
		PK_FLOAT1 sql3_obj = null;
		PK_FLOAT2 sql4_obj = null;

		PK_REAL1 sql5_obj = null;
		PK_REAL2 sql6_obj = null;
		PK_REAL3 sql7_obj = null;
		PK_REAL4 sql8_obj = null;

		for (int i = 0; i < ROWS; i++) {
			session.persist(sql1_obj = new PK_DOUBLE1((i + 1) * 0.1));
			Assertions.assertTrue(sql1_obj.cDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql1_obj.attr1 == ((i + 1) * 0.1));
			Assertions.assertNull(sql1_obj.attr2);
			session.persist(sql2_obj = new PK_DOUBLE2((i + 1) * 0.1));
			Assertions.assertTrue(sql2_obj.tDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql2_obj.attr1 == ((i + 1) * 0.1));
			session.persist(sql3_obj = new PK_FLOAT1((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql3_obj.cFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql3_obj.attr1 == (float) ((i + 1) * 0.1));
			Assertions.assertNull(sql3_obj.attr2);
			session.persist(sql4_obj = new PK_FLOAT2((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql4_obj.tFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql4_obj.attr1 == (float) ((i + 1) * 0.1));
			session.persist(sql5_obj = new PK_REAL1((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql5_obj.cFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql5_obj.attr1 == (float) ((i + 1) * 0.1));
			Assertions.assertNull(sql5_obj.attr2);
			session.persist(sql6_obj = new PK_REAL2((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql6_obj.tFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql6_obj.attr1 == (float) ((i + 1) * 0.1));
			session.persist(sql7_obj = new PK_REAL3((i + 1) * 0.1));
			Assertions.assertTrue(sql7_obj.cDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql7_obj.attr1 == ((i + 1) * 0.1));
			Assertions.assertNull(sql7_obj.attr2);
			session.persist(sql8_obj = new PK_REAL4((i + 1) * 0.1));
			Assertions.assertTrue(sql8_obj.tDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql8_obj.attr1 == ((i + 1) * 0.1));

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
				PK_DOUBLE1.class, //
				PK_DOUBLE2.class, //
				PK_FLOAT1.class, //
				PK_FLOAT2.class, //
				PK_REAL1.class, //
				PK_REAL2.class, //
				PK_REAL3.class, //
				PK_REAL4.class);
		session.open();

		Set<PK_DOUBLE1> sql_1 = session.getAllObjects(PK_DOUBLE1.class);
		Set<PK_DOUBLE2> sql_2 = session.getAllObjects(PK_DOUBLE2.class);
		Set<PK_FLOAT1> sql_3 = session.getAllObjects(PK_FLOAT1.class);
		Set<PK_FLOAT2> sql_4 = session.getAllObjects(PK_FLOAT2.class);

		Set<PK_REAL1> sql_5 = session.getAllObjects(PK_REAL1.class);
		Set<PK_REAL2> sql_6 = session.getAllObjects(PK_REAL2.class);
		Set<PK_REAL3> sql_7 = session.getAllObjects(PK_REAL3.class);
		Set<PK_REAL4> sql_8 = session.getAllObjects(PK_REAL4.class);

		Assertions.assertTrue(sql1_obj.check(sql1, sql_1));
		Assertions.assertTrue(sql2_obj.check(sql2, sql_2));
		Assertions.assertTrue(sql3_obj.check(sql3, sql_3));
		Assertions.assertTrue(sql4_obj.check(sql4, sql_4));

		Assertions.assertTrue(sql5_obj.check(sql5, sql_5));
		Assertions.assertTrue(sql6_obj.check(sql6, sql_6));
		Assertions.assertTrue(sql7_obj.check(sql7, sql_7));
		Assertions.assertTrue(sql8_obj.check(sql8, sql_8));

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DOUBLE1.class, //
				PK_DOUBLE2.class, //
				PK_FLOAT1.class, //
				PK_FLOAT2.class, //
				PK_REAL1.class, //
				PK_REAL2.class, //
				PK_REAL3.class, //
				PK_REAL4.class);
		session.open();

		sql_1 = session.getAllObjects(PK_DOUBLE1.class);
		sql_2 = session.getAllObjects(PK_DOUBLE2.class);
		sql_3 = session.getAllObjects(PK_FLOAT1.class);
		sql_4 = session.getAllObjects(PK_FLOAT2.class);

		sql_5 = session.getAllObjects(PK_REAL1.class);
		sql_6 = session.getAllObjects(PK_REAL2.class);
		sql_7 = session.getAllObjects(PK_REAL3.class);
		sql_8 = session.getAllObjects(PK_REAL4.class);

		for (PK_DOUBLE1 cc : sql_1)
			session.delete(cc);
		for (PK_DOUBLE2 cc : sql_2)
			session.delete(cc);
		for (PK_FLOAT1 cc : sql_3)
			session.delete(cc);
		for (PK_FLOAT2 cc : sql_4)
			session.delete(cc);
		for (PK_REAL1 cc : sql_5)
			session.delete(cc);
		for (PK_REAL2 cc : sql_6)
			session.delete(cc);
		for (PK_REAL3 cc : sql_7)
			session.delete(cc);
		for (PK_REAL4 cc : sql_8)
			session.delete(cc);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DOUBLE1.class, //
				PK_DOUBLE2.class, //
				PK_FLOAT1.class, //
				PK_FLOAT2.class, //
				PK_REAL1.class, //
				PK_REAL2.class, //
				PK_REAL3.class, //
				PK_REAL4.class);
		session.open();

		sql_1 = session.getAllObjects(PK_DOUBLE1.class);
		sql_2 = session.getAllObjects(PK_DOUBLE2.class);
		sql_3 = session.getAllObjects(PK_FLOAT1.class);
		sql_4 = session.getAllObjects(PK_FLOAT2.class);

		sql_5 = session.getAllObjects(PK_REAL1.class);
		sql_6 = session.getAllObjects(PK_REAL2.class);
		sql_7 = session.getAllObjects(PK_REAL3.class);
		sql_8 = session.getAllObjects(PK_REAL4.class);

		Assertions.assertTrue(sql_1.isEmpty());
		Assertions.assertTrue(sql_2.isEmpty());
		Assertions.assertTrue(sql_3.isEmpty());
		Assertions.assertTrue(sql_4.isEmpty());
		Assertions.assertTrue(sql_5.isEmpty());
		Assertions.assertTrue(sql_6.isEmpty());
		Assertions.assertTrue(sql_7.isEmpty());
		Assertions.assertTrue(sql_8.isEmpty());

		this.storePort.sessionManager().terminate(session);

	}
}
