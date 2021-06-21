package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_NUMERIC12")
public final class O2M_S_NUMERIC12 implements O2M__S_Object<O2M_S_NUMERIC12> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	float tFloat;

	O2M_S_NUMERIC12() {
	}

	public O2M_S_NUMERIC12(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC12 x, O2M_S_NUMERIC12 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC11 tFloat " + this.tFloat;
	}

}