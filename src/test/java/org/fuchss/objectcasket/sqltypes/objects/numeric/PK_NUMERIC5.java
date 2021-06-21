package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC5")
public final class PK_NUMERIC5 implements PK__Object<PK_NUMERIC5> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public long tLong;

	public long attr1;

	PK_NUMERIC5() {
	}

	public PK_NUMERIC5(long x) {
		this.tLong = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC5 x, PK_NUMERIC5 y) {
		return ((x.tLong == y.tLong) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC5 tLong = %s, attr1 = %s", "" + this.tLong, "" + this.attr1);
	}

}