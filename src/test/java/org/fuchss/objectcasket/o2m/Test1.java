package org.fuchss.objectcasket.o2m;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.o2m.types.A;
import org.fuchss.objectcasket.o2m.types.B;
import org.fuchss.objectcasket.o2m.types.C;
import org.fuchss.objectcasket.o2m.types.Z;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class Test1 extends TestBase {

	@Test
	public void test_o2m() throws ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class, Z.class);
		session.open();

		A[] a = { new A(), new A(), new A(), new A() };
		B[] b = { new B("b0"), new B("b1"), new B("b2"), new B("b3") };
		// B[] b = { new B(), new B(), new B(), new B() };
		C[] c = { new C(), new C(), new C(), new C() };
		Z[] z = { new Z(), new Z(), new Z(), new Z() };

		for (int i = 0; i < a.length; i++) {
			a[i].column1 = b[i].column1 = c[i].column1 = z[i].column1 = i;
			a[i].column2 = "a" + i;
			b[i].column2 = "b" + i;
			c[i].column2 = "c" + i;
			z[i].column2 = "z" + i;
		}

		a[0].myZs.add(z[0]);

		a[1].myZs.add(z[0]);
		a[1].myZs.add(z[1]);

		a[2].myZs.add(z[0]);
		a[2].myZs.add(z[1]);
		a[2].myZs.add(z[2]);

		a[3].myZs.add(z[0]);
		a[3].myZs.add(z[1]);
		a[3].myZs.add(z[2]);
		a[3].myZs.add(z[3]);

		b[0].myZs.add(z[0]);

		b[1].myZs.add(z[0]);
		b[1].myZs.add(z[1]);

		b[2].myZs.add(z[0]);
		b[2].myZs.add(z[1]);
		b[2].myZs.add(z[2]);

		b[3].myZs.add(z[0]);
		b[3].myZs.add(z[1]);
		b[3].myZs.add(z[2]);
		b[3].myZs.add(z[3]);

		c[0].myZs.add(z[0]);

		c[1].myZs.add(z[1]);

		c[2].myZs.add(z[2]);
		c[2].myZs.add(z[3]);

		for (int i = 0; i < a.length; i++) {
			session.persist(a[i]);
			session.persist(b[i]);
			session.persist(c[i]);
			session.persist(z[i]);
			System.out.println(a[i]);
			System.out.println(b[i]);
			System.out.println(c[i]);
			System.out.println(z[i]);
		}

		this.storePort.sessionManager().terminate(session);
	}

}
