package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_INTEGER8")
public final class O2M_S_INTEGER8 implements O2M__S_Object<O2M_S_INTEGER8> {

	@Id
	byte tByte;

	O2M_S_INTEGER8() {
	}

	public O2M_S_INTEGER8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER8 x, O2M_S_INTEGER8 y) {
		return x.tByte == y.tByte;
	}

}