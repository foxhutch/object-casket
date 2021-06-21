package org.fuchss.objectcasket.sqltypes.objects.real;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_DOUBLE2")
public final class PK_DOUBLE2 implements PK__Object<PK_DOUBLE2> {

	@Id
	public double tDouble;

	public double attr1;

	PK_DOUBLE2() {
	}

	public PK_DOUBLE2(double x) {
		this.tDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_DOUBLE2 x, PK_DOUBLE2 y) {
		return ((x.tDouble == y.tDouble) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_DOUBLE2 tDouble = %s, attr1 = %s", "" + this.tDouble, "" + this.attr1);
	}

}