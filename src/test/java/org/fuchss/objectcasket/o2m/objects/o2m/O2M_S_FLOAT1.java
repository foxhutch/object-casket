package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_FLOAT1")
public final class O2M_S_FLOAT1 implements O2M__S_Object<O2M_S_FLOAT1> {
	@Id
	Float cFloat;

	O2M_S_FLOAT1() {
	}

	public O2M_S_FLOAT1(float x) {
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
