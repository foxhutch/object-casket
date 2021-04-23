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

	Float attr1;

	Float attr2;

	PK_REAL1() {
	}

	PK_REAL1(Float x) {
		this.cFloat = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_REAL1 x, PK_REAL1 y) {
		return (((x.cFloat == y.cFloat) || ((x.cFloat != null) && (y.cFloat != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.floatValue() == y.attr1.floatValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.floatValue() == y.attr2.floatValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_REAL1 cFloat = %s, attr1 = %s, attr2 = %s", "" + this.cFloat, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_REAL2")
final class PK_REAL2 implements PK__Object<PK_REAL2> {

	@Id
	@Column(columnDefinition = "REAL")
	float tFloat;

	float attr1;

	PK_REAL2() {
	}

	PK_REAL2(float x) {
		this.tFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_REAL2 x, PK_REAL2 y) {
		return ((x.tFloat == y.tFloat) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_FLOAT2 tFloat = %s, attr1 = %s", "" + this.tFloat, "" + this.attr1);
	}
}

@Entity()
@Table(name = "PK_REAL3")
final class PK_REAL3 implements PK__Object<PK_REAL3> {

	@Id
	@Column(columnDefinition = "REAL")
	Double cDouble;

	Double attr1;

	Double attr2;

	PK_REAL3() {
	}

	PK_REAL3(Double x) {
		this.cDouble = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_REAL3 x, PK_REAL3 y) {
		return (((x.cDouble == y.cDouble) || ((x.cDouble != null) && (y.cDouble != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.doubleValue() == y.attr1.doubleValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.doubleValue() == y.attr2.doubleValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_REAL3 cDouble = %s, attr1 = %s, attr2 = %s", "" + this.cDouble, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_REAL4")
final class PK_REAL4 implements PK__Object<PK_REAL4> {

	@Id
	@Column(columnDefinition = "REAL")
	double tDouble;

	double attr1;

	PK_REAL4() {
	}

	PK_REAL4(double x) {
		this.tDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_REAL4 x, PK_REAL4 y) {
		return ((x.tDouble == y.tDouble) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_REAL4 tDouble = %s, attr1 = %s", "" + this.tDouble, "" + this.attr1);
	}

}
