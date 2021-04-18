package org.fuchss.objectcasket.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_DOUBLE")
final class O2M_S_DOUBLE1 implements O2M__S_Object<O2M_S_DOUBLE1> {
	@Id
	Double cDouble;

	O2M_S_DOUBLE1() {
	}

	O2M_S_DOUBLE1(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_DOUBLE1 x, O2M_S_DOUBLE1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_DOUBLE1 cDouble " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}

@Entity()
@Table(name = "O2M_S_DOUBLE2")
final class O2M_S_DOUBLE2 implements O2M__S_Object<O2M_S_DOUBLE2> {

	@Id
	double tDouble;

	O2M_S_DOUBLE2() {
	}

	O2M_S_DOUBLE2(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_DOUBLE2 x, O2M_S_DOUBLE2 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "O2M_S_DOUBLE2 tDouble " + this.tDouble;
	}

}
