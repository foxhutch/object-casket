package org.fuchss.objectcasket.justDoNotCrash;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.justDoNotCrash.types.A;
import org.fuchss.objectcasket.justDoNotCrash.types.AB;
import org.fuchss.objectcasket.justDoNotCrash.types.ABC;
import org.fuchss.objectcasket.justDoNotCrash.types.XY;
import org.fuchss.objectcasket.justDoNotCrash.types.XY1;
import org.fuchss.objectcasket.justDoNotCrash.types.XY2;
import org.fuchss.objectcasket.justDoNotCrash.types.XY3;
import org.fuchss.objectcasket.justDoNotCrash.types.XY4;
import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class TestPersist extends TestBase {

	@Test
	public void checkPersistNotFail() throws Exception {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(XY.class, AB.class, ABC.class, XY1.class, XY2.class, XY3.class, XY4.class, A.class);
		session.open();

		XY xy = new XY();
		xy.column = 2;
		session.persist(xy);

		AB ab = new AB();
		ab.column = xy;
		session.persist(ab);

		AB ab2 = new AB();
		ab2.column = new XY();
		ab2.column.column = 3;
		session.persist(ab2);

		ABC abc = new ABC();
		session.persist(abc);

		xy.column = 11;
		session.persist(xy);

		A a = new A();

		a.columnA = 10;
		a.columnB = 20;
		a.setA(30);
		session.persist(a);

		XY2 objXY2a = new XY2();
		objXY2a.column = 10;
		objXY2a.column2 = "objXY2a";

		XY2 objXY2b = new XY2();
		objXY2b.column = 20;
		objXY2b.column2 = "objXY2b";
		objXY2b.column3 = objXY2a;

		XY1 objXY1a = new XY1();
		objXY1a.column = 100;
		objXY1a.column2 = "objXY1a";
		objXY1a.column3 = objXY2a;
		objXY1a.column4 = objXY2b;

		XY1 objXY1b = new XY1();
		objXY1b.column = 200;
		objXY1b.column2 = "objXY1b";
		objXY1b.column3 = objXY2b;
		objXY1b.column4 = objXY2b;

		XY1 objXY1c = new XY1();
		objXY1c.column = 300;
		objXY1c.column2 = "objXY1c";
		objXY1c.column3 = objXY2b;
		objXY1c.column4 = objXY2b;

		session.persist(objXY1c);
		session.persist(objXY1b);
		session.persist(objXY1a);
		session.persist(objXY2b);
		session.persist(objXY2a);

		this.storePort.sessionManager().terminate(session);
	}

}