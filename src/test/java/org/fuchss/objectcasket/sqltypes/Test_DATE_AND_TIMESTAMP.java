package org.fuchss.objectcasket.sqltypes;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.sqltypes.objects.date.PK_DATE;
import org.fuchss.objectcasket.sqltypes.objects.date.PK_TIMESTAMP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test_DATE_AND_TIMESTAMP extends TestBase {

	static final int ROWS = 10;

	/**
	 * This test checks that SQL date and timestamp are suitable as primary keys and
	 * attributes.
	 *
	 * First part: For Java class Date which is mapped to an SQL date and timestamp.
	 * 20 objects are created and stored. (10 as date and 10 as timestamp)
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

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DATE.class, //
				PK_TIMESTAMP.class);
		session.open();

		Set<PK_DATE> sql1 = new HashSet<>();
		PK_DATE sql1_obj = null;
		Set<PK_TIMESTAMP> sql2 = new HashSet<>();
		PK_TIMESTAMP sql2_obj = null;

		for (int i = 0; i < ROWS; i++) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			session.persist(sql1_obj = new PK_DATE(cal.getTime()));
			Assertions.assertTrue(sql1_obj.cDate.equals(cal.getTime()));
			Assertions.assertTrue(sql1_obj.attr1.equals(cal.getTime()));
			Assertions.assertNull(sql1_obj.attr2);
			sql1.add(sql1_obj);
			session.persist(sql2_obj = new PK_TIMESTAMP(cal.getTime()));
			Assertions.assertTrue(sql2_obj.cTIMESTAMP.equals(cal.getTime()));
			Assertions.assertTrue(sql2_obj.attr1.equals(cal.getTime()));
			Assertions.assertNull(sql2_obj.attr2);
			sql2.add(sql2_obj);
		}

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DATE.class, //
				PK_TIMESTAMP.class);
		session.open();

		Set<PK_DATE> sql_1 = session.getAllObjects(PK_DATE.class);
		Set<PK_TIMESTAMP> sql_2 = session.getAllObjects(PK_TIMESTAMP.class);

		Assertions.assertTrue(sql1_obj.check(sql1, sql_1));
		Assertions.assertTrue(sql2_obj.check(sql2, sql_2));

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DATE.class, //
				PK_TIMESTAMP.class);
		session.open();

		sql_1 = session.getAllObjects(PK_DATE.class);
		sql_2 = session.getAllObjects(PK_TIMESTAMP.class);

		for (PK_DATE cc : sql_1)
			session.delete(cc);
		for (PK_TIMESTAMP cc : sql_2)
			session.delete(cc);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				PK_DATE.class, //
				PK_TIMESTAMP.class);
		session.open();

		sql_1 = session.getAllObjects(PK_DATE.class);
		sql_2 = session.getAllObjects(PK_TIMESTAMP.class);

		Assertions.assertTrue(sql_1.isEmpty());
		Assertions.assertTrue(sql_2.isEmpty());

		this.storePort.sessionManager().terminate(session);

	}
}
