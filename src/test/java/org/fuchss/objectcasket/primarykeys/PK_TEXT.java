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
	String cString;

	String attr1;

	String attr2;

	PK_TEXT() {
	}

	PK_TEXT(String x) {
		this.cString = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_TEXT x, PK_TEXT y) {
		return (((x.cString == y.cString) || ((x.cString != null) && (y.cString != null) && (x.cString.equals(y.cString)))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.equals(y.attr1)))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.equals(y.attr2)))));
	}

	@Override
	public String toString() {
		return String.format("PK_TEXT cString = %s, attr1 = %s, attr2 = %s", this.cString, "" + this.attr1, this.attr2);
	}

}