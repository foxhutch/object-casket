package org.fuchss.objectcasket.justDoNotCrash.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public final class XY2 {

	private static String toStringString = "transaction(id = %s, column1 = %s, column2 = %s, column3 = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "column1", columnDefinition = "Integer")
	public Integer column;

	@Column
	public String column2;

	@ManyToOne()
	public XY2 column3;

	@Override
	public String toString() {
		return String.format(XY2.toStringString, this.id, this.column, this.column2, this.column3);
	}
}