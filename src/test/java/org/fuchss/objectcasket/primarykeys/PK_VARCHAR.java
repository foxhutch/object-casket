package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_VARCHAR")
final class PK_VARCHAR implements PK__Object<PK_VARCHAR> {

	@Id
	String cString;

	PK_VARCHAR() {
	}

	PK_VARCHAR(String x) {
		this.cString = x;
	}

	@Override
	public String toString() {
		return "PK_VARCHAR cString " + (this.cString == null ? "NULL" : this.cString);
	}

	@Override
	public boolean sameAs(PK_VARCHAR x, PK_VARCHAR y) {
		return (x == y) || ((x != null) && x.cString.equals(y.cString));
	}
}
