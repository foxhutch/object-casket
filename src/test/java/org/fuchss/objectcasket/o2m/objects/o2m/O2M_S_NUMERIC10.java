package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC10")
public final class O2M_S_NUMERIC10 implements O2M__S_Object<O2M_S_NUMERIC10> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	double tDouble;

	O2M_S_NUMERIC10() {
	}

	public O2M_S_NUMERIC10(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC10 x, O2M_S_NUMERIC10 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC10 tDouble " + this.tDouble;
	}

}