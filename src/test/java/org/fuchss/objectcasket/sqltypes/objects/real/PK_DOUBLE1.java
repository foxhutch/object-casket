package org.fuchss.objectcasket.sqltypes.objects.real;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_DOUBLE1")
public final class PK_DOUBLE1 implements PK__Object<PK_DOUBLE1> {

	@Id
	public Double cDouble;

	public Double attr1;

	public Double attr2;

	PK_DOUBLE1() {
	}

	public PK_DOUBLE1(Double x) {
		this.cDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_DOUBLE1 x, PK_DOUBLE1 y) {
		return (((x.cDouble == y.cDouble) || ((x.cDouble != null) && (y.cDouble != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.doubleValue() == y.attr1.doubleValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.doubleValue() == y.attr2.doubleValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_DOUBLE1 cDouble = %s, attr1 = %s, attr2 = %s", "" + this.cDouble, "" + this.attr1, "" + this.attr2);
	}

}
