package org.fuchss.objectcasket.tests.o2o;

import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.tests.o2o.objects.A;
import org.fuchss.objectcasket.tests.o2o.objects.B;
import org.junit.Assert;
import org.junit.Test;

public class TestO2O extends TestBase {

	/**
	 *     ------
     *    |      |
     *  1 v      |
     *   ---     |
     *  | A | x--
     *   --- 1
     * 
	 * 
	 * This test checks whether dependencies are generated and removed automatically
	 * in the scope of one-to-one relations.
	 *
	 *
	 * First: We generate three A objects (x,y,and z) an build a circle
	 * (x->y->z->x). Then we store x. According do the dependency relation the
	 * database now contains x and y (not z). Only necessary objects are created and
	 * stored. Here we need also the object y to establish the association between x
	 * and y. We don't store z. Because the relation is directed there is no
	 * relation stored between x and z. To establish the relations between y and z.
	 * We have to store y explicitly and also z to establish the relation between z
	 * and x;
	 *
	 * Second: We alter the relation and point from x to z and store x. According to
	 * the one-to-one relation. The association between y and z is obsolete and was
	 * removed. We then terminate the session and check that the dependencies are
	 * mapped to the database.
	 *
	 * Third: At last we delete x and check that also the relation from z to x was
	 * removed from the database. Then we remove the other 2 objects and check that
	 * the database is now empty.
	 *
	 *
	 *
	 */

	@Test
	public void testA() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		A x = new A('x');
		A y = new A('y');
		A z = new A('z');

		x.other = y;
		y.other = z;
		z.other = x;

		session.persist(x);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		Set<A> objs = session.getAllObjects(A.class);
		Assert.assertTrue(objs.size() == 2);

		int i = 0;
		for (A obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
				x = obj;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(obj.other == null);
				i += 2;
				obj.other = y.other;
				y = obj;
			}
		}
		Assert.assertTrue(i == 3);
		z.other = x;

		session.persist(y);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);
		Assert.assertTrue(objs.size() == 3);

		A newX = new A('a');
		A newY = new A('b');
		A newZ = new A('c');

		i = 0;
		for (A obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
				newX = obj;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(y.sameAs(y, obj));
				i += 2;
				newY = obj;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(obj.other == null);
				i += 4;
				newZ = obj;
			}
		}
		Assert.assertTrue(i == 7);

		newZ.other = newX;
		session.persist(newZ);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);
		Assert.assertTrue(objs.size() == 3);

		i = 0;
		for (A obj : objs) {
			if (obj.pk == newX.pk) {
				Assert.assertTrue(newX.sameAs(newX, obj));
				i += 1;
				x = obj;
			}
			if (obj.pk == newY.pk) {
				Assert.assertTrue(newY.sameAs(newY, obj));
				i += 2;
				y = obj;
			}
			if (obj.pk == newZ.pk) {
				Assert.assertTrue(newZ.sameAs(newZ, obj));
				i += 4;
				z = obj;
			}
		}

		Assert.assertTrue(i == 7);

		Assert.assertTrue(x.other == y);
		Assert.assertTrue(y.other == z);
		Assert.assertTrue(z.other == x);

		x.other = z;
		session.persist(x);

		Assert.assertTrue(x.other == z);
		Assert.assertTrue(z.other == x);
		Assert.assertTrue(y.other == null);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);

		i = 0;
		for (A obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(x.sameAs(y, obj));
				i += 2;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(x.sameAs(z, obj));
				i += 4;
			}
		}
		Assert.assertTrue(i == 7);
		this.storePort.sessionManager().terminate(session);
		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);

		i = 0;
		for (A obj : objs) {
			if (obj.pk == x.pk) {
				session.delete(obj);
			}
		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);
		Assert.assertTrue(objs.size() == 2);

		i = 0;
		for (A obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(x.sameAs(y, obj));
				i += 2;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(obj.other == null);
				i += 4;
			}
		}
		Assert.assertTrue(i == 6);

		for (A obj : objs) {
			session.delete(obj);
		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class);
		session.open();

		objs = session.getAllObjects(A.class);
		Assert.assertTrue(objs.isEmpty());
		this.storePort.sessionManager().terminate(session);

	}

	/**
	 *     ------
     *    |      |
     *  1 v      |
     *   ---     |
     *  | B | x--
     *   --- 1
     * 
	 *
	 * Same as testA. But now with generated primary keys:
	 *
	 *
	 */

	@Test
	public void testB() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		B x = new B();
		B y = new B();
		B z = new B();

		x.other = y;
		y.other = z;
		z.other = x;

		session.persist(x);

		Assert.assertTrue(x.pk != null);
		Assert.assertTrue(y.pk != null);
		Assert.assertTrue(z.pk == null);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		Set<B> objs = session.getAllObjects(B.class);
		Assert.assertTrue(objs.size() == 2);

		int i = 0;
		for (B obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
				x = obj;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(obj.other == null);
				i += 2;
				obj.other = y.other;
				y = obj;
			}
		}
		Assert.assertTrue(i == 3);
		z.other = x;
		session.persist(y);

		Assert.assertTrue(x.pk != null);
		Assert.assertTrue(y.pk != null);
		Assert.assertTrue(z.pk != null);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);
		Assert.assertTrue(objs.size() == 3);

		B newX = new B();
		B newY = new B();
		B newZ = new B();

		i = 0;
		for (B obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
				newX = obj;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(y.sameAs(y, obj));
				i += 2;
				newY = obj;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(obj.other == null);
				i += 4;
				newZ = obj;
			}
		}
		Assert.assertTrue(i == 7);

		newZ.other = newX;
		session.persist(newZ);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);
		Assert.assertTrue(objs.size() == 3);

		i = 0;
		for (B obj : objs) {
			if (obj.pk == newX.pk) {
				Assert.assertTrue(newX.sameAs(newX, obj));
				i += 1;
				x = obj;
			}
			if (obj.pk == newY.pk) {
				Assert.assertTrue(newY.sameAs(newY, obj));
				i += 2;
				y = obj;
			}
			if (obj.pk == newZ.pk) {
				Assert.assertTrue(newZ.sameAs(newZ, obj));
				i += 4;
				z = obj;
			}
		}

		Assert.assertTrue(i == 7);

		Assert.assertTrue(x.other == y);
		Assert.assertTrue(y.other == z);
		Assert.assertTrue(z.other == x);

		x.other = z;
		session.persist(x);

		Assert.assertTrue(x.pk != null);
		Assert.assertTrue(y.pk != null);
		Assert.assertTrue(z.pk != null);
		Assert.assertTrue(x.other == z);
		Assert.assertTrue(z.other == x);
		Assert.assertTrue(y.other == null);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);

		i = 0;
		for (B obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(x.sameAs(y, obj));
				i += 2;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(x.sameAs(z, obj));
				i += 4;
			}
		}
		Assert.assertTrue(i == 7);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);

		i = 0;
		for (B obj : objs) {
			if (obj.pk == x.pk) {
				session.delete(obj);
			}
		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);
		Assert.assertTrue(objs.size() == 2);

		i = 0;
		for (B obj : objs) {
			if (obj.pk == x.pk) {
				Assert.assertTrue(x.sameAs(x, obj));
				i += 1;
			}
			if (obj.pk == y.pk) {
				Assert.assertTrue(x.sameAs(y, obj));
				i += 2;
			}
			if (obj.pk == z.pk) {
				Assert.assertTrue(obj.other == null);
				i += 4;
			}
		}
		Assert.assertTrue(i == 6);

		for (B obj : objs) {
			session.delete(obj);
		}
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				B.class);
		session.open();

		objs = session.getAllObjects(B.class);
		Assert.assertTrue(objs.isEmpty());
		this.storePort.sessionManager().terminate(session);

	}

}