package org.fuchss.objectcasket.othertests.exceptions;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMissingClass extends TestBase {

	Session session = null;

	@Test
	public void test1() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					B.class, C.class, D.class, E.class, Error.class);
		});
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains("class A"));

	}

	@Test
	public void test2() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					A.class, C.class, D.class, E.class, Error.class);
		});

		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains("class B"));

	}

	@Test
	public void test3() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					A.class, B.class, D.class, E.class, Error.class);
		});
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains("class C"));

	}

	@Test
	public void test4() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					A.class, B.class, C.class, E.class, Error.class);
		});
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains("class D"));

	}

	@Test
	public void test5() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					A.class, B.class, C.class, D.class, Error.class);
		});
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains("table E"));

	}

}

class Missing {

	@Id
	@GeneratedValue
	public Integer pk;
}

@Entity()
@Table(name = "A")
final class A extends Missing {
}

@Entity()
@Table(name = "B")
final class B extends Missing {
}

@Entity()
@Table(name = "C")
final class C extends Missing {
}

@Entity()
@Table(name = "D")
final class D extends Missing {
}

@Entity()
@Table(name = "E")
final class E extends Missing {
}

@Entity()
@Table(name = "ERROR")
final class Error {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	public A missing1;

	@ManyToOne
	public B missing2;

	@OneToMany
	public Set<C> missing3;

	@ManyToMany
	@JoinTable(name = "E")
	public Set<D> missing4;

	public Error() {
	}

}