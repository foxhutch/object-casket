package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC8")
public final class O2M_S_NUMERIC8 implements O2M__S_Object<O2M_S_NUMERIC8> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	byte tByte;

	O2M_S_NUMERIC8() {
	}

	public O2M_S_NUMERIC8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC8 x, O2M_S_NUMERIC8 y) {
		return x.tByte == y.tByte;
	}

}