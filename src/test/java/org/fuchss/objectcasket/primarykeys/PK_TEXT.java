package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_TEXT")
final class PK_TEXT implements PK__Object<PK_TEXT> {

	@Id
	@Column(columnDefinition = "TEXT")
	String tString;

	PK_TEXT() {
	}

	PK_TEXT(String x) {
		this.tString = x;
	}

	@Override
	public String toString() {
		return "PK_TEXT tString " + (this.tString == null ? "NULL" : this.tString);
	}

	@Override
	public boolean sameAs(PK_TEXT x, PK_TEXT y) {
		return (x == y) || ((x != null) && x.tString.equals(y.tString));
	}
}