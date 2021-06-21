package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC12")
public final class PK_NUMERIC12 implements PK__Object<PK_NUMERIC12> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public float tFloat;

	public float attr1;

	PK_NUMERIC12() {
	}

	public PK_NUMERIC12(float x) {
		this.tFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC12 x, PK_NUMERIC12 y) {
		return ((x.tFloat == y.tFloat) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC12 tFloat = %s, attr1 = %s", "" + this.tFloat, "" + this.attr1);
	}

}