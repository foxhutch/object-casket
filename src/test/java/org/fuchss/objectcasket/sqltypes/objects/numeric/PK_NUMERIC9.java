package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC9")
public final class PK_NUMERIC9 implements PK__Object<PK_NUMERIC9> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public Double cDouble;

	public Double attr1;

	public Double attr2;

	PK_NUMERIC9() {
	}

	public PK_NUMERIC9(double x) {
		this.cDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC9 x, PK_NUMERIC9 y) {
		return (((x.cDouble == y.cDouble) || ((x.cDouble != null) && (y.cDouble != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.doubleValue() == y.attr1.doubleValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.doubleValue() == y.attr2.doubleValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC9 cDouble = %s, attr1 = %s, attr2 = %s", "" + this.cDouble, "" + this.attr1, "" + this.attr2);
	}

}