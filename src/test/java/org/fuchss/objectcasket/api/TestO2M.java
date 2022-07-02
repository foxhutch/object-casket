package org.fuchss.objectcasket.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.fuchss.objectcasket.api.objects.A;
import org.fuchss.objectcasket.api.objects.B;
import org.fuchss.objectcasket.api.objects.C;
import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestO2M extends TestBase {
	//
	// --- 1 AxB n -----
	// | A | <------------> | B |
	// --- n ----- n
	// ^ x
	// --- 1 | |
	// | C | x--------------- |
	// | | <-------------------
	// --- 1
	//

	@Test
	public void test() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class);
		session.open();

		A a1 = new A();
		B b1 = new B();
		B b2 = new B();
		C c1 = new C();

		a1.manyBs.add(b1);
		session.persist(a1);
		Assertions.assertTrue(b1.oneA == a1); // o2m: -> navigable !OK!

		b2.oneA = a1;
		session.persist(b2);

		Assertions.assertTrue(a1.manyBs.contains(b2));// o2m: <- navigable !OK!
		c1.manyBs.add(b1);
		session.persist(c1);

		Assertions.assertTrue(b1.oneC == null); // o2m: -x not navigable !OK!

		b2.oneC = c1;
		session.persist(b2);

		Assertions.assertTrue(!c1.manyBs.contains(b2)); // o2m: x- not navigable !OK!

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class);
		session.open();

		Set<A> as = session.getAllObjects(A.class);
		Set<B> bs = session.getAllObjects(B.class);
		Set<C> cs = session.getAllObjects(C.class);

		Assertions.assertTrue(((as.size() == 1) && (bs.size() == 2) && (cs.size() == 1))); // Right number of objects !OK!"

		A a1x = as.iterator().next();
		Iterator<B> iter = bs.iterator();
		B b1x = iter.next();
		B b2x = iter.next();
		C c1x = cs.iterator().next();

		Assertions.assertTrue(a1x.manyBs.contains(b1x) && a1x.manyBs.contains(b2x)); // a knows each b !OK!"

		Assertions.assertTrue((b1x.oneA == a1x) && (b2x.oneA == a1x)); // Each b knows a !OK!

		if (c1x.manyBs.contains(b1x)) {
			Assertions.assertTrue((b1x.oneC == null) && (b2x.oneC == c1x) && !c1x.manyBs.contains(b2x)); // c || b !OK!;
		} else {
			Assertions.assertTrue((b1x.oneC == c1x) && (b2x.oneC == null) && c1x.manyBs.contains(b2x)); // c || b !OK!";
		}

		this.storePort.sessionManager().terminate(session);

	}
}
