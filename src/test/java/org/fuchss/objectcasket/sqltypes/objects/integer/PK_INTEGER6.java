package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER6")
public final class PK_INTEGER6 implements PK__Object<PK_INTEGER6> {

	@Id
	public int tInteger;

	public int attr1;

	PK_INTEGER6() {
	}

	public PK_INTEGER6(int x) {
		this.tInteger = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER6 x, PK_INTEGER6 y) {
		return ((x.tInteger == y.tInteger) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER6 tInteger = %s, attr1 = %s", "" + this.tInteger, "" + this.attr1);
	}

}