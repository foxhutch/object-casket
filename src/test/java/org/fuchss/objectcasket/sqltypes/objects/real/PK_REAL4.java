package org.fuchss.objectcasket.sqltypes.objects.real;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_REAL4")
public final class PK_REAL4 implements PK__Object<PK_REAL4> {

	@Id
	@Column(columnDefinition = "REAL")
	public double tDouble;

	public double attr1;

	PK_REAL4() {
	}

	public PK_REAL4(double x) {
		this.tDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_REAL4 x, PK_REAL4 y) {
		return ((x.tDouble == y.tDouble) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_REAL4 tDouble = %s, attr1 = %s", "" + this.tDouble, "" + this.attr1);
	}

}