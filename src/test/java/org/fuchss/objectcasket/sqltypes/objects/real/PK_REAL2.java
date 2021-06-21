package org.fuchss.objectcasket.sqltypes.objects.real;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_REAL2")
public final class PK_REAL2 implements PK__Object<PK_REAL2> {

	@Id
	@Column(columnDefinition = "REAL")
	public float tFloat;

	public float attr1;

	PK_REAL2() {
	}

	public PK_REAL2(float x) {
		this.tFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_REAL2 x, PK_REAL2 y) {
		return ((x.tFloat == y.tFloat) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_FLOAT2 tFloat = %s, attr1 = %s", "" + this.tFloat, "" + this.attr1);
	}
}