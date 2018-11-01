package org.fuchss.objectcasket;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class TestManyToOneToMany extends TestBase {
	@Test
	public void test() throws Exception {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(ClassA.class, ClassB.class);
		session.open();

		ClassA a = new ClassA();
		ClassB b = new ClassB();

		a.clazzBs.add(b);

		// session.persist(b);
		session.persist(a);

		System.out.println(b.classA);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(ClassA.class, ClassB.class);
		session.open();

		System.out.println(session.getAllObjects(ClassB.class).iterator().next().classA);
	}

	@Entity
	@Table(name = "ClassA")
	public static final class ClassA {
		@Id
		@GeneratedValue
		public Integer id;

		@OneToMany
		@JoinColumn(name = "map")
		public Set<ClassB> clazzBs = new HashSet<>();

	}

	@Entity
	@Table(name = "ClassB")
	public static final class ClassB {
		@Id
		@GeneratedValue
		public Integer id;

		@ManyToOne
		@Column(name = "map")
		public ClassA classA;
	}

}
