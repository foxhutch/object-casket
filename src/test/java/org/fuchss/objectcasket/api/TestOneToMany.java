package org.fuchss.objectcasket.api;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestOneToMany extends TestBase {
	//
	// ---  1      AxB     n -----
	// | A | <------------> |  B  |
	//  ---                n ----- n
	//                        ^  x
	//  --- 1                 |  |
	// | C | x---------------    |
	// |   | <-------------------
	//  --- 1
	//

	@Test
	public void test_o2m() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class);
		session.open();

		A a1 = new A();
		B b1 = new B();
		B b2 = new B();
		C c1 = new C();

		a1.manyBs.add(b1);
		session.persist(a1);
		if (b1.oneA == a1)
			System.out.println("o2m: -> navigable !OK!");
		else
			Assert.fail("o2m: -> navigable !FAILED!");

		b2.oneA = a1;
		session.persist(b2);

		if (a1.manyBs.contains(b2))
			System.out.println("o2m: <- navigable !OK!");
		else
			Assert.fail("o2m: <- navigable !FAILED!");

		c1.manyBs.add(b1);
		session.persist(c1);
		if (b1.oneC == null)
			System.out.println("o2m: -x not navigable !OK!");
		else
			Assert.fail("o2m: -x not navigable !FAILED!");

		b2.oneC = c1;
		session.persist(b2);

		if (!c1.manyBs.contains(b2))
			System.out.println("o2m: x- not navigable !OK!");
		else
			Assert.fail("o2m: x- not navigable !FAILED!");

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(A.class, B.class, C.class);
		session.open();

		Set<A> as = session.getAllObjects(A.class);
		Set<B> bs = session.getAllObjects(B.class);
		Set<C> cs = session.getAllObjects(C.class);

		if ((as.size() == 1) && (bs.size() == 2) && (cs.size() == 1))
			System.out.println("Right number of objects !OK!");
		else
			Assert.fail(
					"Right number of objects  !FAILED!" + 
			((as.size() != 1) ? ("  as.size() = " + as.size()) : "") + 
			((bs.size() != 2) ? ("  bs.size() = " + bs.size()) : "") + 
			((cs.size() != 1) ? ("  cs.size() = " + cs.size()) : ""));

		A a1x = as.iterator().next();
		Iterator<B> iter = bs.iterator();
		B b1x = iter.next();
		B b2x = iter.next();
		C c1x = cs.iterator().next();

		
		if (a1x.manyBs.contains(b1x) && a1x.manyBs.contains(b2x))
			System.out.println("a knows each b !OK!");
		else
			Assert.fail("a knows each b !FAILED!" + 
		    (a1x.manyBs.contains(b1x) ? " " : b1x + " is missed") + 
		    (a1x.manyBs.contains(b2x) ? " " : b2x + " is missed"));

		if (b1x.oneA == a1x && b2x.oneA == a1x)
			System.out.println("Each b knows a !OK!");
		else
			Assert.fail("Each b knows a !FAILED!" + 
		    (a1x.manyBs.contains(b1x) ? " " : b1x + " is missed") + 
		    (a1x.manyBs.contains(b2x) ? " " : b2x + " is missed"));

			
		if (c1x.manyBs.contains(b1x)) {
			if (b1x.oneC == null && b2x.oneC == c1x && !c1x.manyBs.contains(b2x))
				System.out.println("c || b !OK!");
			else
				Assert.fail("c || b  !FAILED!" + 
					    (b2x.oneC == c1x ? " " : b2x + " knows " + c1x) + 
					    (b1x.oneC == null? " " : b1x + " knows " + b1x.oneC ));
	
		}
			else {
				System.out.println(c1x +" doesn't know "+ b1x);
				if (b1x.oneC == c1x && b2x.oneC == null && c1x.manyBs.contains(b2x))
					System.out.println("c || b !OK!");
				else
					Assert.fail("c || b  !FAILED!" + 
						    (c1x.manyBs.contains(b2x)? " " : c1x + " knows nobody") + 
						    (b1x.oneC == c1x ? " " : b1x + " knows " + c1x) + 
						    (b2x.oneC == null? " " : b2x + " knows " + b2x.oneC ));
		}
		
		this.storePort.sessionManager().terminate(session);

	}

	@Entity
	@Table(name = "A")
	public static final class A {
		@Id
		@GeneratedValue
		public Integer id;

		@OneToMany
		@JoinColumn(name = "AxB")
		public Set<B> manyBs = new HashSet<>();
	}

	@Entity
	@Table(name = "B")
	public static final class B {
		@Id
		@GeneratedValue
		public Integer id;

		@ManyToOne
		@Column(name = "AxB")
		public A oneA;

		@ManyToOne
		public C oneC;
	}

	@Entity
	@Table(name = "C")
	public static final class C {
		@Id
		@GeneratedValue
		public Integer id;

		@OneToMany
		public Set<B> manyBs = new HashSet<>();
	}

}
