package org.fuchss.objectcasket.sqltypes;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.bool.PK_BOOL1;
import org.fuchss.objectcasket.sqltypes.objects.bool.PK_BOOL2;
import org.junit.Assert;
import org.junit.Test;

public class Test_BOOL extends TestBase {

	/**
	 * Test checks that SQL bool is suitable as primary key and attribute.
	 *
	 * First part: For each Java class and type mapped to an SQL bool (Boolean and
	 * boolean) 2 objects are generated and stored. Two with boolean objects true
	 * and false and two with the corresponding types.
	 *
	 * Second part: After terminating the session, the database is opened for a
	 * second time and all stored objects are read. Then we check that all objects
	 * were found.
	 *
	 * Third part:After the session is terminated, the database is opened a third
	 * time and we delete all stored objects. Then we close the session again and
	 * check that the database is now empty.
	 *
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
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
		Assert.assertTrue(sql1_obj.cBoolean);
		Assert.assertTrue(sql1_obj.attr1);
		Assert.assertNull(sql1_obj.attr2);
		sql1.add(sql1_obj);
		session.persist(sql1_obj = new PK_BOOL1(false));
		Assert.assertFalse(sql1_obj.cBoolean);
		Assert.assertFalse(sql1_obj.attr1);
		Assert.assertNull(sql1_obj.attr2);
		sql1.add(sql1_obj);
		session.persist(sql2_obj = new PK_BOOL2(true));
		Assert.assertTrue(sql2_obj.tBoolean);
		Assert.assertTrue(sql2_obj.attr1);
		sql2.add(sql2_obj);
		session.persist(sql2_obj = new PK_BOOL2(false));
		Assert.assertFalse(sql2_obj.tBoolean);
		Assert.assertFalse(sql2_obj.attr1);
		sql2.add(sql2_obj);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_BOOL1.class, //
				PK_BOOL2.class);
		session.open();

		Set<PK_BOOL1> sql_1 = session.getAllObjects(PK_BOOL1.class);
		Set<PK_BOOL2> sql_2 = session.getAllObjects(PK_BOOL2.class);

		Assert.assertTrue(sql_1.size() == 2);
		Assert.assertTrue(sql_2.size() == 2);
		Assert.assertTrue(sql1_obj.check(sql1, sql_1));
		Assert.assertTrue(sql2_obj.check(sql2, sql_2));

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_BOOL1.class, //
				PK_BOOL2.class);
		session.open();

		sql_1 = session.getAllObjects(PK_BOOL1.class);
		sql_2 = session.getAllObjects(PK_BOOL2.class);

		for (PK_BOOL1 cc : sql_1)
			session.delete(cc);
		for (PK_BOOL2 cc : sql_2)
			session.delete(cc);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_BOOL1.class, //
				PK_BOOL2.class);
		session.open();

		sql_1 = session.getAllObjects(PK_BOOL1.class);
		sql_2 = session.getAllObjects(PK_BOOL2.class);

		Assert.assertTrue(sql_1.isEmpty());
		Assert.assertTrue(sql_2.isEmpty());

		this.storePort.sessionManager().terminate(session);

	}
}