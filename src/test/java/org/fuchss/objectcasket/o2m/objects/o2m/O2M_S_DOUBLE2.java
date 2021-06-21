package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_DOUBLE2")
public final class O2M_S_DOUBLE2 implements O2M__S_Object<O2M_S_DOUBLE2> {

	@Id
	double tDouble;

	O2M_S_DOUBLE2() {
	}

	public O2M_S_DOUBLE2(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_DOUBLE2 x, O2M_S_DOUBLE2 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "O2M_S_DOUBLE2 tDouble " + this.tDouble;
	}

}