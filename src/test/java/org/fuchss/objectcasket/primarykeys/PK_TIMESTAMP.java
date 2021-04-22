package org.fuchss.objectcasket.primarykeys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_TIMESTAMP")
final class PK_TIMESTAMP implements PK__Object<PK_TIMESTAMP> {

	@Id
	@Column(columnDefinition = "TIMESTAMP")
	Date cTIMESTAMP;

	PK_TIMESTAMP() {
	}

	PK_TIMESTAMP(Date x) {
		this.cTIMESTAMP = x;
	}

	@Override
	public boolean sameAs(PK_TIMESTAMP x, PK_TIMESTAMP y) {
		return (x == y) || ((x != null) && (y != null) && (x.cTIMESTAMP.compareTo(y.cTIMESTAMP) == 0));
	}

	@Override
	public String toString() {
		return "PK_TIMESTAMP cTIMESTAMP " + (this.cTIMESTAMP == null ? "NULL" : ("" + this.cTIMESTAMP.toString()));
	}

}
