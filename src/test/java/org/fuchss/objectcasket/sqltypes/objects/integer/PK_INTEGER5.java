package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER5")
public final class PK_INTEGER5 implements PK__Object<PK_INTEGER5> {

	@Id
	public long tLong;

	public long attr1;

	PK_INTEGER5() {
	}

	public PK_INTEGER5(long x) {
		this.tLong = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER5 x, PK_INTEGER5 y) {
		return ((x.tLong == y.tLong) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER5 tLong = %s, attr1 = %s", "" + this.tLong, "" + this.attr1);
	}

}