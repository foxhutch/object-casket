package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC8")
public final class PK_NUMERIC8 implements PK__Object<PK_NUMERIC8> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public byte tByte;

	public byte attr1;

	PK_NUMERIC8() {
	}

	public PK_NUMERIC8(byte x) {
		this.tByte = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC8 x, PK_NUMERIC8 y) {
		return ((x.tByte == y.tByte) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC8 tByte = %s, attr1 = %s", "" + this.tByte, "" + this.attr1);
	}

}