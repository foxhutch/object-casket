package org.fuchss.objectcasket.o2m.fundamentals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_TEXT")
final class O2M_S_TEXT implements O2M__S_Object<O2M_S_TEXT> {

	@Id
	@Column(columnDefinition = "TEXT")
	String tString;

	O2M_S_TEXT() {
	}

	O2M_S_TEXT(String x) {
		this.tString = x;
	}

	@Override
	public String toString() {
		return "O2M_S_TEXT tString " + (this.tString == null ? "NULL" : this.tString);
	}

	@Override
	public boolean sameAs(O2M_S_TEXT x, O2M_S_TEXT y) {
		return (x == y) || ((x != null) && x.tString.equals(y.tString));
	}
}