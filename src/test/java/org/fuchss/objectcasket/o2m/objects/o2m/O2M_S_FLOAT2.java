package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_FLOAT2")
public final class O2M_S_FLOAT2 implements O2M__S_Object<O2M_S_FLOAT2> {

	@Id
	float tFloat;

	O2M_S_FLOAT2() {
	}

	public O2M_S_FLOAT2(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_FLOAT2 x, O2M_S_FLOAT2 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "O2M_S_FLOAT2 tFloat " + this.tFloat;
	}

}