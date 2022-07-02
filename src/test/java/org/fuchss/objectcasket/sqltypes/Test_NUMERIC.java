package org.fuchss.objectcasket.sqltypes;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC1;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC10;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC11;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC12;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC2;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC3;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC4;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC5;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC6;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC7;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC8;
import org.fuchss.objectcasket.sqltypes.objects.numeric.PK_NUMERIC9;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test_NUMERIC extends TestBase {

	static final int ROWS = 10;

	/**
	 * This test checks that SQL numeric is suitable as primary key and attribute.
	 *
	 * First part: For each Java class and type mapped to an SQL numeric (Long,
	 * Integer, Short, Byte, Double, Float, and the corresponding types) 10 objects
	 * are created and stored.
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
			session.persist(sql1_obj = new PK_NUMERIC1((long) (i + 1)));
			Assertions.assertTrue(sql1_obj.cLong == (i + 1));
			Assertions.assertTrue(sql1_obj.attr1 == (i + 1));
			Assertions.assertNull(sql1_obj.attr2);

			session.persist(sql2_obj = new PK_NUMERIC2(i + 1));
			Assertions.assertTrue(sql2_obj.cInteger == (i + 1));
			Assertions.assertTrue(sql2_obj.attr1 == (i + 1));
			Assertions.assertNull(sql2_obj.attr2);

			session.persist(sql3_obj = new PK_NUMERIC3((short) (i + 1)));
			Assertions.assertTrue(sql3_obj.cShort == (short) (i + 1));
			Assertions.assertTrue(sql3_obj.attr1 == (short) (i + 1));
			Assertions.assertNull(sql3_obj.attr2);

			session.persist(sql4_obj = new PK_NUMERIC4((byte) (i + 1)));
			Assertions.assertTrue(sql4_obj.cByte == (byte) (i + 1));
			Assertions.assertTrue(sql4_obj.attr1 == (byte) (i + 1));
			Assertions.assertNull(sql4_obj.attr2);

			session.persist(sql5_obj = new PK_NUMERIC5(i + 1));
			Assertions.assertTrue(sql5_obj.tLong == (i + 1));
			Assertions.assertTrue(sql5_obj.attr1 == (i + 1));

			session.persist(sql6_obj = new PK_NUMERIC6(i + 1));
			Assertions.assertTrue(sql6_obj.tInteger == (i + 1));
			Assertions.assertTrue(sql6_obj.attr1 == (i + 1));

			session.persist(sql7_obj = new PK_NUMERIC7((short) (i + 1)));
			Assertions.assertTrue(sql7_obj.tShort == (short) (i + 1));
			Assertions.assertTrue(sql7_obj.attr1 == (short) (i + 1));

			session.persist(sql8_obj = new PK_NUMERIC8((byte) (i + 1)));
			Assertions.assertTrue(sql8_obj.tByte == (byte) (i + 1));
			Assertions.assertTrue(sql8_obj.attr1 == (byte) (i + 1));

			session.persist(sql9_obj = new PK_NUMERIC9((i + 1) * 0.1));
			Assertions.assertTrue(sql9_obj.cDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql9_obj.attr1 == ((i + 1) * 0.1));
			Assertions.assertNull(sql9_obj.attr2);

			session.persist(sql10_obj = new PK_NUMERIC10((i + 1) * 0.1));
			Assertions.assertTrue(sql10_obj.tDouble == ((i + 1) * 0.1));
			Assertions.assertTrue(sql10_obj.attr1 == ((i + 1) * 0.1));

			session.persist(sql11_obj = new PK_NUMERIC11((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql11_obj.cFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql11_obj.attr1 == (float) ((i + 1) * 0.1));
			Assertions.assertNull(sql11_obj.attr2);

			session.persist(sql12_obj = new PK_NUMERIC12((float) ((i + 1) * 0.1)));
			Assertions.assertTrue(sql12_obj.tFloat == (float) ((i + 1) * 0.1));
			Assertions.assertTrue(sql12_obj.attr1 == (float) ((i + 1) * 0.1));

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

		Assertions.assertTrue(sql1_obj.check(sql1, sql_1));
		Assertions.assertTrue(sql2_obj.check(sql2, sql_2));
		Assertions.assertTrue(sql3_obj.check(sql3, sql_3));
		Assertions.assertTrue(sql4_obj.check(sql4, sql_4));
		Assertions.assertTrue(sql5_obj.check(sql5, sql_5));
		Assertions.assertTrue(sql6_obj.check(sql6, sql_6));
		Assertions.assertTrue(sql7_obj.check(sql7, sql_7));
		Assertions.assertTrue(sql8_obj.check(sql8, sql_8));
		Assertions.assertTrue(sql9_obj.check(sql9, sql_9));
		Assertions.assertTrue(sql10_obj.check(sql10, sql_10));
		Assertions.assertTrue(sql11_obj.check(sql11, sql_11));
		Assertions.assertTrue(sql12_obj.check(sql12, sql_12));

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

		sql_1 = session.getAllObjects(PK_NUMERIC1.class);
		sql_2 = session.getAllObjects(PK_NUMERIC2.class);
		sql_3 = session.getAllObjects(PK_NUMERIC3.class);
		sql_4 = session.getAllObjects(PK_NUMERIC4.class);
		sql_5 = session.getAllObjects(PK_NUMERIC5.class);
		sql_6 = session.getAllObjects(PK_NUMERIC6.class);
		sql_7 = session.getAllObjects(PK_NUMERIC7.class);
		sql_8 = session.getAllObjects(PK_NUMERIC8.class);
		sql_9 = session.getAllObjects(PK_NUMERIC9.class);
		sql_10 = session.getAllObjects(PK_NUMERIC10.class);
		sql_11 = session.getAllObjects(PK_NUMERIC11.class);
		sql_12 = session.getAllObjects(PK_NUMERIC12.class);

		for (PK_NUMERIC1 cc : sql_1)
			session.delete(cc);
		for (PK_NUMERIC2 cc : sql_2)
			session.delete(cc);
		for (PK_NUMERIC3 cc : sql_3)
			session.delete(cc);
		for (PK_NUMERIC4 cc : sql_4)
			session.delete(cc);
		for (PK_NUMERIC5 cc : sql_5)
			session.delete(cc);
		for (PK_NUMERIC6 cc : sql_6)
			session.delete(cc);
		for (PK_NUMERIC7 cc : sql_7)
			session.delete(cc);
		for (PK_NUMERIC8 cc : sql_8)
			session.delete(cc);
		for (PK_NUMERIC9 cc : sql_9)
			session.delete(cc);
		for (PK_NUMERIC10 cc : sql_10)
			session.delete(cc);
		for (PK_NUMERIC11 cc : sql_11)
			session.delete(cc);
		for (PK_NUMERIC12 cc : sql_12)
			session.delete(cc);

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

		sql_1 = session.getAllObjects(PK_NUMERIC1.class);
		sql_2 = session.getAllObjects(PK_NUMERIC2.class);
		sql_3 = session.getAllObjects(PK_NUMERIC3.class);
		sql_4 = session.getAllObjects(PK_NUMERIC4.class);
		sql_5 = session.getAllObjects(PK_NUMERIC5.class);
		sql_6 = session.getAllObjects(PK_NUMERIC6.class);
		sql_7 = session.getAllObjects(PK_NUMERIC7.class);
		sql_8 = session.getAllObjects(PK_NUMERIC8.class);
		sql_9 = session.getAllObjects(PK_NUMERIC9.class);
		sql_10 = session.getAllObjects(PK_NUMERIC10.class);
		sql_11 = session.getAllObjects(PK_NUMERIC11.class);
		sql_12 = session.getAllObjects(PK_NUMERIC12.class);

		Assertions.assertTrue(sql_1.isEmpty());
		Assertions.assertTrue(sql_2.isEmpty());
		Assertions.assertTrue(sql_3.isEmpty());
		Assertions.assertTrue(sql_4.isEmpty());
		Assertions.assertTrue(sql_5.isEmpty());
		Assertions.assertTrue(sql_6.isEmpty());
		Assertions.assertTrue(sql_7.isEmpty());
		Assertions.assertTrue(sql_8.isEmpty());
		Assertions.assertTrue(sql_9.isEmpty());
		Assertions.assertTrue(sql_10.isEmpty());
		Assertions.assertTrue(sql_11.isEmpty());
		Assertions.assertTrue(sql_12.isEmpty());

		this.storePort.sessionManager().terminate(session);

	}
}
