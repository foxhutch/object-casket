package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER1")
public final class PK_INTEGER1 implements PK__Object<PK_INTEGER1> {

	@Id
	@GeneratedValue
	public Long cLong;

	public Long attr1;

	public Long attr2;

	public PK_INTEGER1() {
	}

	public PK_INTEGER1(Long x) {
		this.attr1 = x;
	}

	public PK_INTEGER1(Long x, Long attr) {
		this.cLong = x;
		this.attr1 = x;
		this.attr2 = attr;
	}

	@Override
	public boolean sameAs(PK_INTEGER1 x, PK_INTEGER1 y) {
		return (((x.cLong == y.cLong) || ((x.cLong != null) && (y.cLong != null) && (x.cLong.longValue() == y.cLong.longValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.longValue() == y.attr1.longValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.longValue() == y.attr2.longValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER1 cLong = %s, attr1 = %s, attr2 = %s", "" + this.cLong, "" + this.attr1, "" + this.attr2);
	}

}
