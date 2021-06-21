package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER1")
public final class O2M_S_INTEGER1 implements O2M__S_Object<O2M_S_INTEGER1> {

	@Id
	@GeneratedValue
	Long cLong;

	@Override
	public boolean sameAs(O2M_S_INTEGER1 x, O2M_S_INTEGER1 y) {

		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER1 cLong " + this.cLong;
	}

}
