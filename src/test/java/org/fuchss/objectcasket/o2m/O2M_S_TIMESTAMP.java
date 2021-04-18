package org.fuchss.objectcasket.o2m;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_TIMESTAMP")
final class O2M_S_TIMESTAMP implements O2M__S_Object<O2M_S_TIMESTAMP> {

	@Id
	@Column(columnDefinition = "TIMESTAMP")
	Date cDate;

	O2M_S_TIMESTAMP() {
	}

	O2M_S_TIMESTAMP(Date x) {
		this.cDate = x;
	}

	@Override
	public boolean sameAs(O2M_S_TIMESTAMP x, O2M_S_TIMESTAMP y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDate.compareTo(y.cDate) == 0));
	}

	@Override
	public String toString() {
		return "O2M_S_TIMESTAMP cDate " + (this.cDate == null ? "NULL" : ("" + this.cDate.toString()));
	}
}
