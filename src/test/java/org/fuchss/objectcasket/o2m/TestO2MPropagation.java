package org.fuchss.objectcasket.o2m;

import java.util.Iterator;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.o2m.objects.other.A;
import org.fuchss.objectcasket.o2m.objects.other.B;
import org.fuchss.objectcasket.o2m.objects.other.C;
import org.fuchss.objectcasket.o2m.objects.other.D;
import org.fuchss.objectcasket.o2m.objects.other.X;
import org.fuchss.objectcasket.o2m.objects.other.Y;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestO2MPropagation extends TestBase {

	@Test
	public void testAll2D() throws ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class, D.class);
		session.open();

		A[] a = { new A(), new A(), new A(), new A() };
		B[] b = { new B("b0"), new B("b1"), new B("b2"), new B("b3") };
		C[] c = { new C(), new C(), new C(), new C() };
		D[] d = { new D(), new D(), new D(), new D() };

		for (int i = 0; i < a.length; i++) {
			a[i].column1 = b[i].column1 = c[i].column1 = d[i].column1 = i;
			a[i].column2 = "a" + i;
			b[i].column2 = "b" + i;
			c[i].column2 = "c" + i;
			d[i].column2 = "z" + i;
		}

		a[0].myDs.add(d[0]);
		session.persist(a[0]);

		a[1].myDs.add(d[0]);
		a[1].myDs.add(d[1]);
		session.persist(a[1]);
		Assertions.assertTrue(a[0].myDs.isEmpty());

		a[2].myDs.add(d[0]);
		a[2].myDs.add(d[1]);
		a[2].myDs.add(d[2]);
		session.persist(a[2]);
		Assertions.assertTrue(a[1].myDs.isEmpty());

		a[3].myDs.add(d[0]);
		a[3].myDs.add(d[1]);
		a[3].myDs.add(d[2]);
		a[3].myDs.add(d[3]);
		session.persist(a[3]);
		Assertions.assertTrue(a[2].myDs.isEmpty());

		b[0].myDs.add(d[0]);
		session.persist(b[0]);
		Assertions.assertTrue(a[3].myDs.size() == 4);

		b[1].myDs.add(d[0]);
		b[1].myDs.add(d[1]);
		session.persist(b[1]);
		Assertions.assertTrue(b[0].myDs.isEmpty());
		Assertions.assertTrue(a[3].myDs.size() == 4);

		b[2].myDs.add(d[0]);
		b[2].myDs.add(d[1]);
		b[2].myDs.add(d[2]);
		session.persist(b[2]);
		Assertions.assertTrue(b[1].myDs.isEmpty());
		Assertions.assertTrue(a[3].myDs.size() == 4);

		b[3].myDs.add(d[0]);
		b[3].myDs.add(d[1]);
		b[3].myDs.add(d[2]);
		b[3].myDs.add(d[3]);
		session.persist(b[3]);
		Assertions.assertTrue(b[2].myDs.isEmpty());
		Assertions.assertTrue(a[3].myDs.size() == 4);

		c[0].myDs.add(d[0]);
		session.persist(c[0]);
		Assertions.assertTrue(d[0].theC == c[0]);
		Assertions.assertTrue(b[3].myDs.size() == 4);
		Assertions.assertTrue(a[3].myDs.size() == 4);

		c[1].myDs.add(d[0]);
		c[1].myDs.add(d[1]);
		session.persist(c[1]);
		Assertions.assertTrue(c[0].myDs.isEmpty());
		Assertions.assertTrue(d[0].theC == c[1]);
		Assertions.assertTrue(d[1].theC == c[1]);
		Assertions.assertTrue(b[3].myDs.size() == 4);
		Assertions.assertTrue(a[3].myDs.size() == 4);

		c[2].myDs.add(d[0]);
		c[2].myDs.add(d[1]);
		c[2].myDs.add(d[2]);
		session.persist(c[2]);
		Assertions.assertTrue(c[1].myDs.isEmpty());
		Assertions.assertTrue(d[0].theC == c[2]);
		Assertions.assertTrue(d[1].theC == c[2]);
		Assertions.assertTrue(d[2].theC == c[2]);
		Assertions.assertTrue(b[3].myDs.size() == 4);
		Assertions.assertTrue(a[3].myDs.size() == 4);

		c[3].myDs.add(d[0]);
		c[3].myDs.add(d[1]);
		c[3].myDs.add(d[2]);
		c[3].myDs.add(d[3]);
		session.persist(c[3]);
		Assertions.assertTrue(c[2].myDs.isEmpty());
		Assertions.assertTrue(d[0].theC == c[3]);
		Assertions.assertTrue(d[1].theC == c[3]);
		Assertions.assertTrue(d[2].theC == c[3]);
		Assertions.assertTrue(d[3].theC == c[3]);
		Assertions.assertTrue(b[3].myDs.size() == 4);
		Assertions.assertTrue(a[3].myDs.size() == 4);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class, D.class);
		session.open();

		Set<D> allDs = session.getAllObjects(D.class);
		Assertions.assertTrue(allDs.size() == 4);

		Iterator<D> iter = allDs.iterator();
		for (int i = 0; i < 4; i++)
			d[i] = iter.next();

		C theC = d[0].theC;

		for (int i = 0; i < 4; i++)
			Assertions.assertTrue(d[i].theC == theC);

		Set<A> allAs = session.getAllObjects(A.class);
		Set<B> allBs = session.getAllObjects(B.class);
		Set<C> allCs = session.getAllObjects(C.class);

		Assertions.assertTrue(allAs.size() == 4);
		Assertions.assertTrue(allBs.size() == 4);
		Assertions.assertTrue(allCs.size() == 4);

		int ac = 0;
		int bc = 0;
		int cc = 0;
		for (A a1 : allAs) {
			if (!a1.myDs.isEmpty()) {
				ac++;
				Assertions.assertTrue(a1.myDs.containsAll(allDs));
			}
		}

		for (B b1 : allBs) {
			if (!b1.myDs.isEmpty()) {
				bc++;
				Assertions.assertTrue(b1.myDs.containsAll(allDs));
			}
		}

		for (C c1 : allCs) {
			if (!c1.myDs.isEmpty()) {
				cc++;
				Assertions.assertTrue(c1.myDs.containsAll(allDs));
			}
		}

		Assertions.assertTrue(ac == 1);
		Assertions.assertTrue(bc == 1);
		Assertions.assertTrue(cc == 1);

		this.storePort.sessionManager().terminate(session);

	}

	@Test
	public void testXxY() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				Y.class, //
				X.class);
		session.open();

		Y y = new Y(true);
		X x = new X(false);

		y.myXs.add(x);

		session.persist(y);

		Assertions.assertTrue(x.myY == y);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				Y.class, //
				X.class);
		session.open();

		Set<Y> allYs = session.getAllObjects(Y.class);
		Assertions.assertTrue(allYs.size() == 1);

		Y y_new = allYs.iterator().next();
		Assertions.assertTrue(y_new.pk.equals(y.pk));
		Assertions.assertTrue(y_new.myXs.size() == 1);

		X x_new = y_new.myXs.iterator().next();
		Assertions.assertTrue(x_new.myY == y_new);

		x_new.myY = null;
		session.persist(x_new);
		Assertions.assertTrue(y_new.myXs.size() == 0);

		y_new.myXs.add(x_new);
		session.persist(y_new);
		Assertions.assertTrue(x_new.myY == y_new);

		y_new.myXs.clear();
		session.persist(y_new);
		Assertions.assertTrue(x_new.myY == null);

		this.storePort.sessionManager().terminate(session);
	}

}
