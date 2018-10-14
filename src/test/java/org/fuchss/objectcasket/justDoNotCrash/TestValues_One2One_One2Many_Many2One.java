package org.fuchss.objectcasket.justDoNotCrash;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class TestValues_One2One_One2Many_Many2One extends TestBase {
	@Test
	public void test() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(MValue.class);
		session.declareClass(Value.class, FValue.class);
		session.open();

		Value x = new Value();
		x.valueA = 10;
		x.valueB = "x10";

		Value y = new Value();
		y.valueA = 20;
		y.valueB = "y20";

		Value z = new Value();
		z.valueA = 30;
		z.valueB = "z30";

		session.persist(x);
		session.persist(y);
		session.persist(z);

		FValue f1 = new FValue();
		f1.value = x;
		f1.text = x.valueB;

		FValue f2 = new FValue();
		f2.value = y;
		f2.text = y.valueB;

		FValue f3 = new FValue();
		f3.value = z;
		f3.text = z.valueB;

		session.persist(f1);
		session.persist(f2);
		session.persist(f3);

		f1.isomorphicValue = y;
		session.persist(f1);

		f2.isomorphicValue = y;
		session.persist(f2);

		if (f1.isomorphicValue == null) {
			System.out.println("Right");
		} else {
			System.out.println("Wrong");
		}

		f3.megamorphicValue = f1;
		session.persist(f3);

		f3.megamorphicValue = f3;
		session.persist(f3);

		MValue m1 = new MValue();
		m1.text = "m1";

		MValue m2 = new MValue();
		m2.text = "m2";

		session.persist(m1);
		session.persist(m2);

		m1.value = new HashSet<>();
		m1.value.add(x);
		session.persist(m1);
		m1.value.add(y);
		session.persist(m1);
		m1.value.add(z);
		m1.value.remove(y);
		session.persist(m1);

		m2.value = new HashSet<>();
		m2.value.add(x);
		session.persist(m2);

		session.delete(m2);

		this.storePort.sessionManager().terminate(session);
	}
}

@Entity()
@Table(name = "Values")
final class Value {

	@Override
	public String toString() {
		return "Value [id=" + this.id + ", valueA=" + this.valueA + ", valueB=" + this.valueB + "]";
	}

	@Id
	@GeneratedValue
	Integer id;

	Integer valueA;

	public String valueB;

}

@Entity()
@Table(name = "FValues")
final class FValue {

	@Id
	@GeneratedValue
	Integer id;

	@ManyToOne()
	Value value;

	@OneToOne()
	Value isomorphicValue;

	@OneToOne()
	FValue megamorphicValue;

	public String text;

}

@Entity()
@Table(name = "MValues")
final class MValue {

	@Id
	@GeneratedValue
	Integer id;

	@OneToMany()
	Set<Value> value;

	public String text;

}
