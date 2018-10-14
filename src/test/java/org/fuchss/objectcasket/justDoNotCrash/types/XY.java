package org.fuchss.objectcasket.justDoNotCrash.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "XY")
@Table(name = "simple")
public final class XY {

	private static String toStringString = "simple(id = %s, columnX = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "columnX", columnDefinition = "Integer")
	public Integer column;

	@Override
	public String toString() {
		return String.format(XY.toStringString, this.id, this.column);
	}

}