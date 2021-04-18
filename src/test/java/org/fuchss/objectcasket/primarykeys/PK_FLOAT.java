package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_FLOAT1")
final class PK_FLOAT1 implements PK__Object<PK_FLOAT1> {

	@Id
	Float cFloat;

	PK_FLOAT1() {
	}

	PK_FLOAT1(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(PK_FLOAT1 x, PK_FLOAT1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "PK_FLOAT1 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}

@Entity()
@Table(name = "PK_FLOAT2")
final class PK_FLOAT2 implements PK__Object<PK_FLOAT2> {

	@Id
	float tFloat;

	PK_FLOAT2() {
	}

	PK_FLOAT2(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(PK_FLOAT2 x, PK_FLOAT2 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "PK_FLOAT2 tFloat " + this.tFloat;
	}

}
