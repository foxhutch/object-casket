package org.fuchss.objectcasket.o2m.fundamentals;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_DATE")
final class O2M_S_DATE implements O2M__S_Object<O2M_S_DATE> {

	@Id
	Date cDate;

	O2M_S_DATE() {
	}

	O2M_S_DATE(Date x) {
		this.cDate = x;
	}

	@Override
	public boolean sameAs(O2M_S_DATE x, O2M_S_DATE y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDate.compareTo(y.cDate) == 0));
	}

	@Override
	public String toString() {
		return "O2M_S_DATE cDate " + (this.cDate == null ? "NULL" : ("" + this.cDate.toString()));
	}

}
