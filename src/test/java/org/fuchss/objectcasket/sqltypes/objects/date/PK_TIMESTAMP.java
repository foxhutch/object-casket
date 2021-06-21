package org.fuchss.objectcasket.sqltypes.objects.date;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_TIMESTAMP")
public final class PK_TIMESTAMP implements PK__Object<PK_TIMESTAMP> {

	@Id
	@Column(columnDefinition = "TIMESTAMP")
	public Date cTIMESTAMP;

	@Column(columnDefinition = "TIMESTAMP")
	public Date attr1;

	public Date attr2;

	PK_TIMESTAMP() {
	}

	public PK_TIMESTAMP(Date x) {
		this.cTIMESTAMP = x;
		this.attr1 = new Date(x.getTime());
	}

	@Override
	public boolean sameAs(PK_TIMESTAMP x, PK_TIMESTAMP y) {
		return (((x.cTIMESTAMP == y.cTIMESTAMP) || ((x.cTIMESTAMP != null) && (y.cTIMESTAMP != null) && (x.cTIMESTAMP.compareTo(y.cTIMESTAMP) == 0))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.compareTo(y.attr1) == 0))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.compareTo(y.attr2) == 0))));
	}

	@Override
	public String toString() {
		return String.format("PK_TIMESTAMP cTIMESTAMP = %s, attr1 = %s, attr2 = %s", "" + this.cTIMESTAMP, "" + this.attr1, "" + this.attr2);
	}

}
