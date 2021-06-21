package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER7")
public final class PK_INTEGER7 implements PK__Object<PK_INTEGER7> {

	@Id
	public short tShort;

	public short attr1;

	PK_INTEGER7() {
	}

	public PK_INTEGER7(short x) {
		this.tShort = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER7 x, PK_INTEGER7 y) {
		return ((x.tShort == y.tShort) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER7 tInteger = %s, attr1 = %s", "" + this.tShort, "" + this.attr1);
	}
}