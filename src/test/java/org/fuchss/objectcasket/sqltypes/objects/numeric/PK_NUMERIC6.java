package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC6")
public final class PK_NUMERIC6 implements PK__Object<PK_NUMERIC6> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public int tInteger;

	public int attr1;

	PK_NUMERIC6() {
	}

	public PK_NUMERIC6(int x) {
		this.tInteger = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC6 x, PK_NUMERIC6 y) {
		return ((x.tInteger == y.tInteger) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC6 tInteger = %s, attr1 = %s", "" + this.tInteger, "" + this.attr1);
	}

}