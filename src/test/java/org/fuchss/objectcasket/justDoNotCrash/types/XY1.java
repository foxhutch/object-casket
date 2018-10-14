package org.fuchss.objectcasket.justDoNotCrash.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "realynewaccount")
public final class XY1 {

	private static String toStringString = "realynewaccount(id = %s, column1 = %s, column2 = %s, column3 = %s, column4 = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "column1", columnDefinition = "Integer")
	public Integer column;

	@Column
	public String column2;

	@ManyToOne()
	@JoinColumn(name = "transaction")
	public XY2 column3;

	@ManyToOne()
	public XY2 column4;

	@Override
	public String toString() {
		return String.format(XY1.toStringString, this.id, this.column, this.column2, this.column3, this.column4);
	}

}