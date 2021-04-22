package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_REAL1")
final class PK_REAL1 implements PK__Object<PK_REAL1> {

	@Id
	@Column(columnDefinition = "REAL")
	Float cFloat;

	PK_REAL1() {
	}

	PK_REAL1(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(PK_REAL1 x, PK_REAL1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "PK_REAL1 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}

@Entity()
@Table(name = "PK_REAL2")
final class PK_REAL2 implements PK__Object<PK_REAL2> {

	@Id
	@Column(columnDefinition = "REAL")
	float tFloat;

	PK_REAL2() {
	}

	PK_REAL2(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(PK_REAL2 x, PK_REAL2 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "PK_REAL2 tFloat " + this.tFloat;
	}

}

@Entity()
@Table(name = "PK_REAL3")
final class PK_REAL3 implements PK__Object<PK_REAL3> {

	@Id
	@Column(columnDefinition = "REAL")
	Double cDouble;

	PK_REAL3() {
	}

	PK_REAL3(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(PK_REAL3 x, PK_REAL3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "PK_REAL3 cFloat " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}

@Entity()
@Table(name = "PK_REAL4")
final class PK_REAL4 implements PK__Object<PK_REAL4> {

	@Id
	@Column(columnDefinition = "REAL")
	double tDouble;

	PK_REAL4() {
	}

	PK_REAL4(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(PK_REAL4 x, PK_REAL4 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "PK_REAL4 tDouble " + this.tDouble;
	}

}
