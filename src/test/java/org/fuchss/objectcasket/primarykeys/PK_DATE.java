package org.fuchss.objectcasket.primarykeys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_DATE")
final class PK_DATE implements PK__Object<PK_DATE> {

	@Id
	Date cDate;

	PK_DATE() {
	}

	PK_DATE(Date x) {
		this.cDate = x;
	}

	@Override
	public boolean sameAs(PK_DATE x, PK_DATE y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDate.compareTo(y.cDate) == 0));
	}

	@Override
	public String toString() {
		return "PK_DATE cDate " + (this.cDate == null ? "NULL" : ("" + this.cDate.toString()));
	}

}
