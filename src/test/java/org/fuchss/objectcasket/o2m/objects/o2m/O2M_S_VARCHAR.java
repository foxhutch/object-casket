package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_VARCHAR")
public final class O2M_S_VARCHAR implements O2M__S_Object<O2M_S_VARCHAR> {
	@Id
	String cString;

	O2M_S_VARCHAR() {
	}

	public O2M_S_VARCHAR(String x) {
		this.cString = x;
	}

	@Override
	public String toString() {
		return "O2M_S_VARCHAR cString " + (this.cString == null ? "NULL" : this.cString);
	}

	@Override
	public boolean sameAs(O2M_S_VARCHAR x, O2M_S_VARCHAR y) {
		return (x == y) || ((x != null) && x.cString.equals(y.cString));
	}
}