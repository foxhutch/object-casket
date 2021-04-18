package org.fuchss.objectcasket.primarykeys;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestPk_DATE_AND_TIMESTAMP extends TestBase {

	static final int ROWS = 10;

	@Test
	public void testPkDate() {
		try {

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
				sql1.add(sql1_obj);
				session.persist(sql2_obj = new PK_TIMESTAMP(cal.getTime()));
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

			for (PK_DATE cc : sql_1)		System.out.println(cc);
			for (PK_TIMESTAMP cc : sql_2)	System.out.println(cc);

			Assert.assertTrue(sql1_obj.check(sql1, sql_1));
			Assert.assertTrue(sql2_obj.check(sql2, sql_2));

			this.storePort.sessionManager().terminate(session);
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
