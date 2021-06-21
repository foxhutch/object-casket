package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC3")
public final class PK_NUMERIC3 implements PK__Object<PK_NUMERIC3> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public Short cShort;

	public Short attr1;

	public Short attr2;

	PK_NUMERIC3() {
	}

	public PK_NUMERIC3(Short x) {
		this.cShort = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC3 x, PK_NUMERIC3 y) {
		return (((x.cShort == y.cShort) || ((x.cShort != null) && (y.cShort != null) && (x.cShort.shortValue() == y.cShort.shortValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.shortValue() == y.attr1.shortValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.shortValue() == y.attr2.shortValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC3 cShort = %s, attr1 = %s, attr2 = %s", "" + this.cShort, "" + this.attr1, "" + this.attr2);
	}

}