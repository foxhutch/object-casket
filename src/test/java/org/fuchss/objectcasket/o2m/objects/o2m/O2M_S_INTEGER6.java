package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER6")
public final class O2M_S_INTEGER6 implements O2M__S_Object<O2M_S_INTEGER6> {

	@Id
	int tInteger;

	O2M_S_INTEGER6() {
	}

	public O2M_S_INTEGER6(int x) {
		this.tInteger = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER6 tInteger " + this.tInteger;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER6 x, O2M_S_INTEGER6 y) {
		return x.tInteger == y.tInteger;
	}

}