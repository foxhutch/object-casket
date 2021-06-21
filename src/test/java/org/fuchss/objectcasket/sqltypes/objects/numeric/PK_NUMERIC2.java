package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC2")
public final class PK_NUMERIC2 implements PK__Object<PK_NUMERIC2> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public Integer cInteger;

	public Integer attr1;

	public Integer attr2;

	PK_NUMERIC2() {
	}

	public PK_NUMERIC2(Integer x) {
		this.cInteger = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC2 x, PK_NUMERIC2 y) {
		return (((x.cInteger == y.cInteger) || ((x.cInteger != null) && (y.cInteger != null) && (x.cInteger.intValue() == y.cInteger.intValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.intValue() == y.attr1.intValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.intValue() == y.attr2.intValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC2 cInteger = %s, attr1 = %s, attr2 = %s", "" + this.cInteger, "" + this.attr1, "" + this.attr2);
	}

}