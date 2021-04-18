package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_DOUBLE1")
final class PK_DOUBLE1 implements PK__Object<PK_DOUBLE1> {

	@Id
	Double cDouble;

	PK_DOUBLE1() {
	}

	PK_DOUBLE1(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(PK_DOUBLE1 x, PK_DOUBLE1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "PK_DOUBLE1 cDouble " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}

@Entity()
@Table(name = "PK_DOUBLE2")
final class PK_DOUBLE2 implements PK__Object<PK_DOUBLE2> {

	@Id
	double tDouble;

	PK_DOUBLE2() {
	}

	PK_DOUBLE2(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(PK_DOUBLE2 x, PK_DOUBLE2 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "PK_DOUBLE2 tDouble " + this.tDouble;
	}

}
