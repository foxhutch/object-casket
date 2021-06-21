package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC4")
public final class PK_NUMERIC4 implements PK__Object<PK_NUMERIC4> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public Byte cByte;

	public Byte attr1;

	public Byte attr2;

	PK_NUMERIC4() {
	}

	public PK_NUMERIC4(Byte x) {
		this.cByte = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC4 x, PK_NUMERIC4 y) {
		return (((x.cByte == y.cByte) || ((x.cByte != null) && (y.cByte != null) && (x.cByte.byteValue() == y.cByte.byteValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.byteValue() == y.attr1.byteValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.byteValue() == y.attr2.byteValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC4 cByte = %s, attr1 = %s, attr2 = %s", "" + this.cByte, "" + this.attr1, "" + this.attr2);
	}

}