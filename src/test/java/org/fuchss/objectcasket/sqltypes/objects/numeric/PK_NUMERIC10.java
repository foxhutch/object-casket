package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC10")
public final class PK_NUMERIC10 implements PK__Object<PK_NUMERIC10> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public double tDouble;

	public double attr1;

	PK_NUMERIC10() {
	}

	public PK_NUMERIC10(double x) {
		this.tDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC10 x, PK_NUMERIC10 y) {
		return ((x.tDouble == y.tDouble) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC10 tDouble = %s, attr1 = %s", "" + this.tDouble, "" + this.attr1);
	}
}