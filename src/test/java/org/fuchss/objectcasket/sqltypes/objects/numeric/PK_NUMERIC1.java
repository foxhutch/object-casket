package org.fuchss.objectcasket.sqltypes.objects.numeric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_NUMERIC1")
public final class PK_NUMERIC1 implements PK__Object<PK_NUMERIC1> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	public Long cLong;

	public Long attr1;

	public Long attr2;

	PK_NUMERIC1() {
	}

	public PK_NUMERIC1(Long x) {
		this.cLong = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC1 x, PK_NUMERIC1 y) {
		return (((x.cLong == y.cLong) || ((x.cLong != null) && (y.cLong != null) && (x.cLong.longValue() == y.cLong.longValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.longValue() == y.attr1.longValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.longValue() == y.attr2.longValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC1 cLong = %s, attr1 = %s, attr2 = %s", "" + this.cLong, "" + this.attr1, "" + this.attr2);
	}
}
