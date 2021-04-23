package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_FLOAT1")
final class PK_FLOAT1 implements PK__Object<PK_FLOAT1> {

	@Id
	Float cFloat;

	Float attr1;

	Float attr2;

	PK_FLOAT1() {
	}

	PK_FLOAT1(Float x) {
		this.cFloat = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_FLOAT1 x, PK_FLOAT1 y) {
		return (((x.cFloat == y.cFloat) || ((x.cFloat != null) && (y.cFloat != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.floatValue() == y.attr1.floatValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.floatValue() == y.attr2.floatValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_FLOAT1 cFloat = %s, attr1 = %s, attr2 = %s", "" + this.cFloat, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_FLOAT2")
final class PK_FLOAT2 implements PK__Object<PK_FLOAT2> {

	@Id
	float tFloat;

	float attr1;

	PK_FLOAT2() {
	}

	PK_FLOAT2(float x) {
		this.tFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_FLOAT2 x, PK_FLOAT2 y) {
		return ((x.tFloat == y.tFloat) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_FLOAT2 tFloat = %s, attr1 = %s", "" + this.tFloat, "" + this.attr1);
	}

}
