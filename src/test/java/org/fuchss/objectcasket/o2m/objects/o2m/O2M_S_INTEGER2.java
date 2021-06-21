package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER2")
public final class O2M_S_INTEGER2 implements O2M__S_Object<O2M_S_INTEGER2> {

	@Id
	@GeneratedValue
	Integer cInteger;

	@Override
	public String toString() {
		return "O2M_S_INTEGER2 cInteger " + this.cInteger;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER2 x, O2M_S_INTEGER2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cInteger.intValue() == y.cInteger.intValue()));
	}

}