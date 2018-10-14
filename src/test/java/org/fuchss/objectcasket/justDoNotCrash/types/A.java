
package org.fuchss.objectcasket.justDoNotCrash.types;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "inherited")
public final class A extends B {
	private static String toStringString = "inherited(id = %s, columnA = %s, %s)";

	@Id
	@GeneratedValue
	public Integer id;

	public Integer columnA;

	public String column;

	@Override
	public String toString() {
		return String.format(A.toStringString, this.id, this.columnA, super.toString());
	}

}