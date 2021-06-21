package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC5")
public final class O2M_S_NUMERIC5 implements O2M__S_Object<O2M_S_NUMERIC5> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	long tLong;

	O2M_S_NUMERIC5() {
	}

	public O2M_S_NUMERIC5(long x) {
		this.tLong = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC5 tLong " + this.tLong;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC5 x, O2M_S_NUMERIC5 y) {
		return x.tLong == y.tLong;
	}

}