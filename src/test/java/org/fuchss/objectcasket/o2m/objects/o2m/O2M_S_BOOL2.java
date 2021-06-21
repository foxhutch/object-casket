package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_BOOL2")
public final class O2M_S_BOOL2 implements O2M__S_Object<O2M_S_BOOL2> {

	@Id
	boolean tBoolean;

	O2M_S_BOOL2() {
	}

	public O2M_S_BOOL2(boolean x) {
		this.tBoolean = x;
	}

	@Override
	public boolean sameAs(O2M_S_BOOL2 x, O2M_S_BOOL2 y) {
		return x.tBoolean == y.tBoolean;
	}

	@Override
	public String toString() {
		return "O2M_S_BOOL2 tBoolean " + this.tBoolean;
	}
}