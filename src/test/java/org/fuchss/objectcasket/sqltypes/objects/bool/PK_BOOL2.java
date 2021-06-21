package org.fuchss.objectcasket.sqltypes.objects.bool;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_BOOL2")
public final class PK_BOOL2 implements PK__Object<PK_BOOL2> {

	@Id
	public boolean tBoolean;

	public boolean attr1;

	PK_BOOL2() {
	}

	public PK_BOOL2(boolean x) {
		this.tBoolean = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_BOOL2 x, PK_BOOL2 y) {
		return ((x.tBoolean == y.tBoolean) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_BOOL2 tBoolean = %s, attr1 = %s", "" + this.tBoolean, "" + this.attr1);
	}

}