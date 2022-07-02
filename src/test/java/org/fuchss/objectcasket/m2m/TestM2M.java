package org.fuchss.objectcasket.m2m;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.m2m.objects.A;
import org.fuchss.objectcasket.m2m.objects.AxB;
import org.fuchss.objectcasket.m2m.objects.AxC;
import org.fuchss.objectcasket.m2m.objects.B;
import org.fuchss.objectcasket.m2m.objects.BxC;
import org.fuchss.objectcasket.m2m.objects.C;
import org.fuchss.objectcasket.m2m.objects.D;
import org.fuchss.objectcasket.m2m.objects.DxE;
import org.fuchss.objectcasket.m2m.objects.E;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestM2M extends TestBase {

	@Test
	public void testAxBxC() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class, //
				B.class, //
				C.class, //
				AxB.class, //
				BxC.class, //
				AxC.class);
		session.open();

		A a1 = new A('1');
		A a2 = new A('2');
		A a3 = new A('3');

		B b1 = new B('1');
		B b2 = new B('2');
		B b3 = new B('3');

		C c1 = new C('1');
		C c2 = new C('2');
		C c3 = new C('3');

		a1.b.add(b1);
		a1.b.add(b2);
		a2.b.add(b2);
		a2.b.add(b3);
		a3.b.add(b3);
		a3.b.add(b1);

		b1.c.add(c1);
		b1.c.add(c2);
		b2.c.add(c2);
		b2.c.add(c3);
		b3.c.add(c3);
		b3.c.add(c1);

		c1.a.add(a1);
		c1.a.add(a2);
		c2.a.add(a2);
		c2.a.add(a3);
		c3.a.add(a3);
		c3.a.add(a1);

		session.persist(a1);
		Assertions.assertTrue(b1.a.contains(a1));
		Assertions.assertTrue(b2.a.contains(a1));
		session.persist(a2);
		Assertions.assertTrue(b2.a.contains(a2));
		Assertions.assertTrue(b3.a.contains(a2));
		session.persist(a3);
		Assertions.assertTrue(b3.a.contains(a3));
		Assertions.assertTrue(b1.a.contains(a3));

		session.persist(b1);
		Assertions.assertTrue(c1.b.contains(b1));
		Assertions.assertTrue(c2.b.contains(b1));
		session.persist(b2);
		Assertions.assertTrue(c2.b.contains(b2));
		Assertions.assertTrue(c3.b.contains(b2));
		session.persist(b3);
		Assertions.assertTrue(c3.b.contains(b3));
		Assertions.assertTrue(c1.b.contains(b3));

		session.persist(c1);
		Assertions.assertTrue(a1.c.contains(c1));
		Assertions.assertTrue(a2.c.contains(c1));
		session.persist(c2);
		Assertions.assertTrue(a2.c.contains(c2));
		Assertions.assertTrue(a3.c.contains(c2));
		session.persist(c3);
		Assertions.assertTrue(a3.c.contains(c3));
		Assertions.assertTrue(a1.c.contains(c3));
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class, //
				B.class, //
				C.class, //
				AxB.class, //
				BxC.class, //
				AxC.class);
		session.open();

		Set<A> allAs = session.getAllObjects(A.class);
		Set<B> ab = new HashSet<>();
		Set<C> bc = new HashSet<>();
		Set<A> ca = new HashSet<>();
		Set<B> cb = new HashSet<>();
		Set<A> ba = new HashSet<>();
		Set<C> ac = new HashSet<>();

		Set<A> as = new HashSet<>();
		Set<B> bs = new HashSet<>();
		Set<C> cs = new HashSet<>();

		for (A a : allAs) {
			as.add(a);
			for (B b : a.b) {
				ab.add(b);
				bs.add(b);
				for (C c : b.c) {
					bc.add(c);
					cs.add(c);
					for (A aa : c.a) {
						ca.add(aa);
						as.add(aa);
					}
					for (B bb : c.b) {
						cb.add(bb);
						bs.add(bb);
						for (A aaa : bb.a) {
							ba.add(aaa);
							as.add(aaa);
							for (C cc : aaa.c) {
								ac.add(cc);
								cs.add(cc);
							}
						}

					}

				}
			}
		}

		Assertions.assertTrue(allAs.size() == 3);
		Assertions.assertTrue(ab.size() == 3);
		Assertions.assertTrue(bc.size() == 3);
		Assertions.assertTrue(ca.size() == 3);
		Assertions.assertTrue(cb.size() == 3);
		Assertions.assertTrue(ba.size() == 3);
		Assertions.assertTrue(ac.size() == 3);
		Assertions.assertTrue(as.size() == 3);
		Assertions.assertTrue(bs.size() == 3);
		Assertions.assertTrue(cs.size() == 3);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class, //
				B.class, //
				C.class, //
				AxB.class, //
				BxC.class, //
				AxC.class);
		session.open();

		Set<B> allBs = session.getAllObjects(B.class);
		Set<B> ab2 = new HashSet<>();
		Set<C> bc2 = new HashSet<>();
		Set<A> ca2 = new HashSet<>();
		Set<B> cb2 = new HashSet<>();
		Set<A> ba2 = new HashSet<>();
		Set<C> ac2 = new HashSet<>();

		Set<A> as2 = new HashSet<>();
		Set<B> bs2 = new HashSet<>();
		Set<C> cs2 = new HashSet<>();

		for (B b : allBs) {
			bs2.add(b);
			for (C c : b.c) {
				bc2.add(c);
				cs2.add(c);
				for (A a : c.a) {
					ca2.add(a);
					as2.add(a);
					for (B bb : a.b) {
						ab2.add(bb);
						bs2.add(bb);
					}
					for (C cc : a.c) {
						ac2.add(cc);
						cs2.add(cc);
						for (B bbb : cc.b) {
							cb2.add(bbb);
							bs2.add(bbb);
							for (A aa : bbb.a) {
								ba2.add(aa);
								as2.add(aa);
							}
						}

					}

				}
			}
		}

		Assertions.assertTrue(allBs.size() == 3);
		Assertions.assertTrue(ab2.size() == 3);
		Assertions.assertTrue(bc2.size() == 3);
		Assertions.assertTrue(ca2.size() == 3);
		Assertions.assertTrue(cb2.size() == 3);
		Assertions.assertTrue(ba2.size() == 3);
		Assertions.assertTrue(ac2.size() == 3);
		Assertions.assertTrue(as2.size() == 3);
		Assertions.assertTrue(bs2.size() == 3);
		Assertions.assertTrue(cs2.size() == 3);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				A.class, //
				B.class, //
				C.class, //
				AxB.class, //
				BxC.class, //
				AxC.class);
		session.open();

		Set<C> allCs = session.getAllObjects(C.class);
		Set<B> ab3 = new HashSet<>();
		Set<C> bc3 = new HashSet<>();
		Set<A> ca3 = new HashSet<>();
		Set<B> cb3 = new HashSet<>();
		Set<A> ba3 = new HashSet<>();
		Set<C> ac3 = new HashSet<>();

		Set<A> as3 = new HashSet<>();
		Set<B> bs3 = new HashSet<>();
		Set<C> cs3 = new HashSet<>();

		for (C c : allCs) {
			cs3.add(c);
			for (A a : c.a) {
				ca3.add(a);
				as3.add(a);
				for (B b : a.b) {
					ab3.add(b);
					bs3.add(b);
					for (C cc : b.c) {
						bc3.add(cc);
						cs3.add(cc);
					}
					for (A aa : b.a) {
						ba3.add(aa);
						as3.add(aa);
						for (C ccc : aa.c) {
							ac3.add(ccc);
							cs3.add(ccc);
							for (B bb : ccc.b) {
								cb3.add(bb);
								bs3.add(bb);
							}
						}

					}

				}
			}
		}

		Assertions.assertTrue(allCs.size() == 3);
		Assertions.assertTrue(ab3.size() == 3);
		Assertions.assertTrue(bc3.size() == 3);
		Assertions.assertTrue(ca3.size() == 3);
		Assertions.assertTrue(cb3.size() == 3);
		Assertions.assertTrue(ba3.size() == 3);
		Assertions.assertTrue(ac3.size() == 3);
		Assertions.assertTrue(as3.size() == 3);
		Assertions.assertTrue(bs3.size() == 3);
		Assertions.assertTrue(cs3.size() == 3);

		this.storePort.sessionManager().terminate(session);

	}

	@Test
	public void testDxE() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				D.class, //
				E.class, //
				DxE.class);
		session.open();

		D d = new D('d');
		E e = new E('e');

		d.e.add(e);
		session.persist(d);

		DxE de = session.joinTableEntity(d, e, DxE.class);
		Assertions.assertTrue(de.d == d);
		Assertions.assertTrue(de.e == e);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				D.class, //
				E.class, //
				DxE.class);
		session.open();

		Set<D> ds = session.getAllObjects(D.class);
		Assertions.assertTrue(ds.size() == 1);
		for (D dd : ds) {
			Assertions.assertTrue(d.e.size() == 1);
			for (E ee : dd.e) {
				ee.d.contains(dd);
				Assertions.assertTrue(ee.d.size() == 1);
				DxE dxe = session.joinTableEntity(dd, ee, DxE.class);
				Assertions.assertNotNull(dxe);
				Assertions.assertTrue(dxe.d == dd);
				Assertions.assertTrue(dxe.e == ee);
			}
		}
		this.storePort.sessionManager().terminate(session);
	}

	@Test
	public void test1() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				D.class, //
				E.class, //
				DxE.class);
		session.open();

		D x = new D('x');
		D y = new D('y');
		E e1 = new E('1');
		E e2 = new E('2');
		E e3 = new E('3');
		E e4 = new E('4');
		E e5 = new E('5');
		E e6 = new E('6');

		x.e.add(e1);
		x.e.add(e2);
		x.e.add(e3);
		session.persist(x);

		y.e.add(e4);
		session.persist(y);
		y.e.add(e5);
		session.persist(y);
		y.e.add(e6);
		session.persist(y);

		DxE xe1 = session.joinTableEntity(x, e1, DxE.class);
		Assertions.assertTrue(xe1.d == x);
		Assertions.assertTrue(xe1.e == e1);
		DxE xe2 = session.joinTableEntity(x, e2, DxE.class);
		Assertions.assertTrue(xe2.d == x);
		Assertions.assertTrue(xe2.e == e2);
		DxE xe3 = session.joinTableEntity(x, e3, DxE.class);
		Assertions.assertTrue(xe3.d == x);
		Assertions.assertTrue(xe3.e == e3);

		DxE ye4 = session.joinTableEntity(y, e4, DxE.class);
		Assertions.assertTrue(ye4.d == y);
		Assertions.assertTrue(ye4.e == e4);
		DxE ye5 = session.joinTableEntity(y, e5, DxE.class);
		Assertions.assertTrue(ye5.d == y);
		Assertions.assertTrue(ye5.e == e5);
		DxE ye6 = session.joinTableEntity(y, e6, DxE.class);
		Assertions.assertTrue(ye6.d == y);
		Assertions.assertTrue(ye6.e == e6);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				D.class, //
				E.class, //
				DxE.class);
		session.open();

		Set<E> es = session.getAllObjects(E.class);
		Assertions.assertTrue(es.size() == 6);
		for (E e : es) {
			Assertions.assertTrue(e.d.size() == 1);
			for (D d : e.d) {
				d.e.contains(e);
				Assertions.assertTrue(d.e.size() == 3);
				DxE dxe = session.joinTableEntity(d, e, DxE.class);
				Assertions.assertTrue(dxe.d == d);
				Assertions.assertTrue(dxe.e == e);
			}
		}
		this.storePort.sessionManager().terminate(session);
	}

}
