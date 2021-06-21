package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER3")
public final class O2M_S_INTEGER3 implements O2M__S_Object<O2M_S_INTEGER3> {

	@Id
	@GeneratedValue
	Short cShort;

	@Override
	public String toString() {
		return "O2M_S_INTEGER3 cShort " + this.cShort;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER3 x, O2M_S_INTEGER3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cShort.intValue() == y.cShort.intValue()));
	}

}