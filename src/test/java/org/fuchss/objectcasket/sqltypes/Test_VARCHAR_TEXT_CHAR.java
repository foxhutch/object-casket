package org.fuchss.objectcasket.sqltypes;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.character.PK_CHAR1;
import org.fuchss.objectcasket.sqltypes.objects.character.PK_CHAR2;
import org.fuchss.objectcasket.sqltypes.objects.text.PK_TEXT;
import org.fuchss.objectcasket.sqltypes.objects.text.PK_VARCHAR;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test_VARCHAR_TEXT_CHAR extends TestBase {

	static final int ROWS = 10;

	/**
	 * This test checks that SQL varchar, text, and char are suitable as primary
	 * keys and attributes.
	 *
	 * First part: For each Java class and type mapped to SQL varchar, text, or char
	 * (String, Character and char) 10 objects are created and stored.
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
			Assertions.assertTrue(sql1_obj.cString.equals("" + (i + 1)));
			Assertions.assertTrue(sql1_obj.attr1.equals("" + (i + 1)));
			Assertions.assertNull(sql1_obj.attr2);

			session.persist(sql2_obj = new PK_TEXT("" + (i + 1)));
			Assertions.assertTrue(sql2_obj.cString.equals("" + (i + 1)));
			Assertions.assertTrue(sql2_obj.attr1.equals("" + (i + 1)));
			Assertions.assertNull(sql2_obj.attr2);

			session.persist(sql3_obj = new PK_CHAR1((char) (65 + i)));
			Assertions.assertTrue(sql3_obj.cCharacter == (char) (65 + i));
			Assertions.assertTrue(sql3_obj.attr1 == (char) (65 + i));
			Assertions.assertNull(sql3_obj.attr2);

			session.persist(sql4_obj = new PK_CHAR2((char) (97 + i)));
			Assertions.assertTrue(sql4_obj.tCharacter == (char) (97 + i));
			Assertions.assertTrue(sql4_obj.attr1 == (char) (97 + i));

			sql1.add(sql1_obj);
			sql2.add(sql2_obj);
			sql3.add(sql3_obj);
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

		Assertions.assertTrue(sql1_obj.check(sql1, sql_1));
		Assertions.assertTrue(sql2_obj.check(sql2, sql_2));
		Assertions.assertTrue(sql3_obj.check(sql3, sql_3));
		Assertions.assertTrue(sql4_obj.check(sql4, sql_4));

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_VARCHAR.class, //
				PK_TEXT.class, //
				PK_CHAR1.class, //
				PK_CHAR2.class);
		session.open();

		sql_1 = session.getAllObjects(PK_VARCHAR.class);
		sql_2 = session.getAllObjects(PK_TEXT.class);
		sql_3 = session.getAllObjects(PK_CHAR1.class);
		sql_4 = session.getAllObjects(PK_CHAR2.class);

		for (PK_VARCHAR cc : sql_1)
			session.delete(cc);
		for (PK_TEXT cc : sql_2)
			session.delete(cc);
		for (PK_CHAR1 cc : sql_3)
			session.delete(cc);
		for (PK_CHAR2 cc : sql_4)
			session.delete(cc);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_VARCHAR.class, //
				PK_TEXT.class, //
				PK_CHAR1.class, //
				PK_CHAR2.class);
		session.open();

		sql_1 = session.getAllObjects(PK_VARCHAR.class);
		sql_2 = session.getAllObjects(PK_TEXT.class);
		sql_3 = session.getAllObjects(PK_CHAR1.class);
		sql_4 = session.getAllObjects(PK_CHAR2.class);

		Assertions.assertTrue(sql_1.isEmpty());
		Assertions.assertTrue(sql_2.isEmpty());
		Assertions.assertTrue(sql_3.isEmpty());
		Assertions.assertTrue(sql_4.isEmpty());

		this.storePort.sessionManager().terminate(session);

	}
}
