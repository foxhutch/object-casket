package org.fuchss.objectcasket.sqltypes.objects.integer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_INTEGER2")
public final class PK_INTEGER2 implements PK__Object<PK_INTEGER2> {

	@Id
	@GeneratedValue
	public Integer cInteger;

	public Integer attr1;

	public Integer attr2;

	public PK_INTEGER2() {
	}

	public PK_INTEGER2(Integer x) {
		this.attr1 = x;
	}

	public PK_INTEGER2(Integer x, Integer attr) {
		this.cInteger = x;
		this.attr1 = x;
		this.attr2 = attr;
	}

	@Override
	public boolean sameAs(PK_INTEGER2 x, PK_INTEGER2 y) {
		return (((x.cInteger == y.cInteger) || ((x.cInteger != null) && (y.cInteger != null) && (x.cInteger.intValue() == y.cInteger.intValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.intValue() == y.attr1.intValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.intValue() == y.attr2.intValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER2 cInteger = %s, attr1 = %s, attr2 = %s", "" + this.cInteger, "" + this.attr1, "" + this.attr2);
	}

}