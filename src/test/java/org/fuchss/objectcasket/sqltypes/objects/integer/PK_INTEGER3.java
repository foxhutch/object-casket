package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER3")
public final class PK_INTEGER3 implements PK__Object<PK_INTEGER3> {

	@Id
	@GeneratedValue
	public Short cShort;

	public Short attr1;

	public Short attr2;

	public PK_INTEGER3() {
	}

	public PK_INTEGER3(Short x) {
		this.attr1 = x;
	}

	public PK_INTEGER3(Short x, Short attr) {
		this.cShort = x;
		this.attr1 = x;
		this.attr2 = attr;
	}

	@Override
	public boolean sameAs(PK_INTEGER3 x, PK_INTEGER3 y) {
		return (((x.cShort == y.cShort) || ((x.cShort != null) && (y.cShort != null) && (x.cShort.shortValue() == y.cShort.shortValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.shortValue() == y.attr1.shortValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.shortValue() == y.attr2.shortValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER3 cShort = %s, attr1 = %s, attr2 = %s", "" + this.cShort, "" + this.attr1, "" + this.attr2);
	}

}