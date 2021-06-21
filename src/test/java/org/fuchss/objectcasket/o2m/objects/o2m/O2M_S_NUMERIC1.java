package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC1")
public final class O2M_S_NUMERIC1 implements O2M__S_Object<O2M_S_NUMERIC1> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Long cLong;

	O2M_S_NUMERIC1() {
	}

	public O2M_S_NUMERIC1(long x) {
		this.cLong = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC1 cLong " + this.cLong;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC1 x, O2M_S_NUMERIC1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

}
