package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_BOOL1")
public final class O2M_S_BOOL1 implements O2M__S_Object<O2M_S_BOOL1> {

	@Id
	Boolean cBoolean;

	O2M_S_BOOL1() {
	}

	public O2M_S_BOOL1(boolean x) {
		this.cBoolean = x;
	}

	@Override
	public boolean sameAs(O2M_S_BOOL1 x, O2M_S_BOOL1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_BOOL1 cBoolean " + (this.cBoolean == null ? "NULL" : ("" + this.cBoolean));
	}

}
