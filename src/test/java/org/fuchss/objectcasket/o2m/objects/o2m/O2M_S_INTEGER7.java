package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER7")
public final class O2M_S_INTEGER7 implements O2M__S_Object<O2M_S_INTEGER7> {

	@Id
	short tShort;

	O2M_S_INTEGER7() {
	}

	public O2M_S_INTEGER7(short x) {
		this.tShort = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER7 tShort " + this.tShort;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER7 x, O2M_S_INTEGER7 y) {
		return x.tShort == y.tShort;
	}

}