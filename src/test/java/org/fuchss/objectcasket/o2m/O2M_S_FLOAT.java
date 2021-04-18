package org.fuchss.objectcasket.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_FLOAT1")
final class O2M_S_FLOAT1 implements O2M__S_Object<O2M_S_FLOAT1> {
	@Id
	Float cFloat;

	O2M_S_FLOAT1() {
	}

	O2M_S_FLOAT1(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_FLOAT1 x, O2M_S_FLOAT1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_FLOAT1 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}

@Entity()
@Table(name = "O2M_S_FLOAT2")
final class O2M_S_FLOAT2 implements O2M__S_Object<O2M_S_FLOAT2> {

	@Id
	float tFloat;

	O2M_S_FLOAT2() {
	}

	O2M_S_FLOAT2(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_FLOAT2 x, O2M_S_FLOAT2 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "O2M_S_FLOAT2 tFloat " + this.tFloat;
	}

}
