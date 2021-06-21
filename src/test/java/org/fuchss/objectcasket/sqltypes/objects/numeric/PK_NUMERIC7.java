package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC7")
public final class PK_NUMERIC7 implements PK__Object<PK_NUMERIC7> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public short tShort;

	public short attr1;

	PK_NUMERIC7() {
	}

	public PK_NUMERIC7(short x) {
		this.tShort = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC7 x, PK_NUMERIC7 y) {
		return ((x.tShort == y.tShort) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC7 tInteger = %s, attr1 = %s", "" + this.tShort, "" + this.attr1);
	}

}