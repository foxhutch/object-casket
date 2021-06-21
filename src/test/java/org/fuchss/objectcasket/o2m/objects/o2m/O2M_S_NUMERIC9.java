package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC9")
public final class O2M_S_NUMERIC9 implements O2M__S_Object<O2M_S_NUMERIC9> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Double cDouble;

	O2M_S_NUMERIC9() {
	}

	public O2M_S_NUMERIC9(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC9 x, O2M_S_NUMERIC9 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC9 cDouble " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}