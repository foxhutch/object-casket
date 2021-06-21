package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER4")
public final class O2M_S_INTEGER4 implements O2M__S_Object<O2M_S_INTEGER4> {

	@Id
	@GeneratedValue
	Byte cByte;

	@Override
	public String toString() {
		return "O2M_S_INTEGER4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER4 x, O2M_S_INTEGER4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}
}