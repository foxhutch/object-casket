package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC11")
public final class O2M_S_NUMERIC11 implements O2M__S_Object<O2M_S_NUMERIC11> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Float cFloat;

	O2M_S_NUMERIC11() {
	}

	public O2M_S_NUMERIC11(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC11 x, O2M_S_NUMERIC11 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC11 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}