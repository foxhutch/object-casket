package org.fuchss.objectcasket.sqltypes;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER1;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER2;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER3;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER4;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER5;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER6;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER7;
import org.fuchss.objectcasket.sqltypes.objects.integer.PK_INTEGER8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test_INTEGER extends TestBase {

	static final int ROWS = 10;

	/**
	 * This test checks that SQL integer is suitable as primary key and attribute.
	 *
	 * First part: For each Java class and type mapped to an SQL integer (Long,
	 * Integer, Short, Byte, long, int, short, byte), 10 objects are created and
	 * stored. Primary keys are created automatically and also manually for (Long,
	 * Integer, Short, Byte). For (Long, Int, Short, Byte) the primary keys are set
	 * only manually.
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
			session.persist(sql1_obj = new PK_INTEGER1((long) (i + 1), null));
			session.persist(sql2_obj = new PK_INTEGER2(i + 1, null));
			session.persist(sql3_obj = new PK_INTEGER3((short) (i + 1), null));
			session.persist(sql4_obj = new PK_INTEGER4((byte) (i + 1), null));
			sql1.add(sql1_obj);
			Assertions.assertTrue(sql1_obj.cLong == (i + 1));
			Assertions.assertTrue(sql1_obj.attr1 == (i + 1));
			Assertions.assertNull(sql1_obj.attr2);
			sql2.add(sql2_obj);
			Assertions.assertTrue(sql2_obj.cInteger == (i + 1));
			Assertions.assertTrue(sql2_obj.attr1 == (i + 1));
			Assertions.assertNull(sql2_obj.attr2);
			sql3.add(sql3_obj);
			Assertions.assertTrue(sql3_obj.cShort == (i + 1));
			Assertions.assertTrue(sql3_obj.attr1 == (i + 1));
			Assertions.assertNull(sql3_obj.attr2);
			sql4.add(sql4_obj);
			Assertions.assertTrue(sql4_obj.cByte == (i + 1));
			Assertions.assertTrue(sql4_obj.attr1 == (i + 1));
			Assertions.assertNull(sql4_obj.attr2);
		}

		for (byte i = 0; i < ROWS; i++) {
			session.persist(sql1_obj = new PK_INTEGER1((long) (i + 1)));
			Assertions.assertNotNull(sql1_obj.cLong);
			Assertions.assertNull(sql1_obj.attr2);
			session.persist(sql2_obj = new PK_INTEGER2(i + 1));
			Assertions.assertNotNull(sql2_obj.cInteger);
			Assertions.assertNull(sql2_obj.attr2);
			session.persist(sql3_obj = new PK_INTEGER3((short) (i + 1)));
			Assertions.assertNotNull(sql3_obj.cShort);
			Assertions.assertNull(sql3_obj.attr2);
			session.persist(sql4_obj = new PK_INTEGER4((byte) (i + 1)));
			Assertions.assertNotNull(sql4_obj.cByte);
			Assertions.assertNull(sql4_obj.attr2);
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
				PK_INTEGER1.class, //
				PK_INTEGER2.class, //
				PK_INTEGER3.class, //
				PK_INTEGER4.class, //
				PK_INTEGER5.class, //
				PK_INTEGER6.class, //
				PK_INTEGER7.class, //
				PK_INTEGER8.class);
		session.open();

		sql_1 = session.getAllObjects(PK_INTEGER1.class);
		sql_2 = session.getAllObjects(PK_INTEGER2.class);
		sql_3 = session.getAllObjects(PK_INTEGER3.class);
		sql_4 = session.getAllObjects(PK_INTEGER4.class);
		sql_5 = session.getAllObjects(PK_INTEGER5.class);
		sql_6 = session.getAllObjects(PK_INTEGER6.class);
		sql_7 = session.getAllObjects(PK_INTEGER7.class);
		sql_8 = session.getAllObjects(PK_INTEGER8.class);

		for (PK_INTEGER1 cc : sql_1)
			session.delete(cc);
		for (PK_INTEGER2 cc : sql_2)
			session.delete(cc);
		for (PK_INTEGER3 cc : sql_3)
			session.delete(cc);
		for (PK_INTEGER4 cc : sql_4)
			session.delete(cc);
		for (PK_INTEGER5 cc : sql_5)
			session.delete(cc);
		for (PK_INTEGER6 cc : sql_6)
			session.delete(cc);
		for (PK_INTEGER7 cc : sql_7)
			session.delete(cc);
		for (PK_INTEGER8 cc : sql_8)
			session.delete(cc);

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

		sql_1 = session.getAllObjects(PK_INTEGER1.class);
		sql_2 = session.getAllObjects(PK_INTEGER2.class);
		sql_3 = session.getAllObjects(PK_INTEGER3.class);
		sql_4 = session.getAllObjects(PK_INTEGER4.class);
		sql_5 = session.getAllObjects(PK_INTEGER5.class);
		sql_6 = session.getAllObjects(PK_INTEGER6.class);
		sql_7 = session.getAllObjects(PK_INTEGER7.class);
		sql_8 = session.getAllObjects(PK_INTEGER8.class);

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
