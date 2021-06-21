package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC4")
public final class O2M_S_NUMERIC4 implements O2M__S_Object<O2M_S_NUMERIC4> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Byte cByte;

	O2M_S_NUMERIC4() {
	}

	public O2M_S_NUMERIC4(byte x) {
		this.cByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC4 x, O2M_S_NUMERIC4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}

}