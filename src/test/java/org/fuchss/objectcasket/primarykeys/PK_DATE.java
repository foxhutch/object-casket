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

	Date attr1;

	Date attr2;

	PK_DATE() {
	}

	PK_DATE(Date x) {
		this.cDate = x;
		this.attr2 = new Date(x.getTime());
	}

	@Override
	public boolean sameAs(PK_DATE x, PK_DATE y) {
		return (((x.cDate == y.cDate) || ((x.cDate != null) && (y.cDate != null) && (x.cDate.compareTo(y.cDate) == 0))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.compareTo(y.attr1) == 0))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.compareTo(y.attr2) == 0))));
	}

	@Override
	public String toString() {
		return String.format("PK_DATE cDate = %s, attr1 = %s, attr2 = %s", "" + this.cDate, "" + this.attr1, "" + this.attr2);
	}

}
