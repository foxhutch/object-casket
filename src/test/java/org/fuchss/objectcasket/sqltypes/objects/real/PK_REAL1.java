package org.fuchss.objectcasket.sqltypes.objects.real;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_REAL1")
public final class PK_REAL1 implements PK__Object<PK_REAL1> {

	@Id
	@Column(columnDefinition = "REAL")
	public Float cFloat;

	public Float attr1;

	public Float attr2;

	PK_REAL1() {
	}

	public PK_REAL1(Float x) {
		this.cFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_REAL1 x, PK_REAL1 y) {
		return (((x.cFloat == y.cFloat) || ((x.cFloat != null) && (y.cFloat != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.floatValue() == y.attr1.floatValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.floatValue() == y.attr2.floatValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_REAL1 cFloat = %s, attr1 = %s, attr2 = %s", "" + this.cFloat, "" + this.attr1, "" + this.attr2);
	}

}
