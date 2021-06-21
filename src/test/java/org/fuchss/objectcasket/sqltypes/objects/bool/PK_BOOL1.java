package org.fuchss.objectcasket.sqltypes.objects.bool;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_BOOL1")
public final class PK_BOOL1 implements PK__Object<PK_BOOL1> {

	@Id
	public Boolean cBoolean;

	public Boolean attr1;

	public Boolean attr2;

	PK_BOOL1() {
	}

	public PK_BOOL1(Boolean x) {
		this.cBoolean = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_BOOL1 x, PK_BOOL1 y) {
		return (((x.cBoolean == y.cBoolean) || ((x.cBoolean != null) && (y.cBoolean != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.booleanValue() == y.attr1.booleanValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.booleanValue() == y.attr2.booleanValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_BOOL1 cBoolean = %s, attr1 = %s, attr2 = %s", "" + this.cBoolean, "" + this.attr1, "" + this.attr2);
	}

}