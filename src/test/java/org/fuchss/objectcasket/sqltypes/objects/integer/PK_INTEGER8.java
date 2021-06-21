package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER8")
public final class PK_INTEGER8 implements PK__Object<PK_INTEGER8> {

	@Id
	public byte tByte;

	public byte attr1;

	PK_INTEGER8() {
	}

	public PK_INTEGER8(byte x) {
		this.tByte = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER8 x, PK_INTEGER8 y) {
		return ((x.tByte == y.tByte) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER8 tByte = %s, attr1 = %s", "" + this.tByte, "" + this.attr1);
	}

}