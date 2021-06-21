
package org.fuchss.objectcasket.justDoNotCrash.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simple2")
public final class AB {

	private static String toStringString = "simple2(id = %s, column = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@ManyToOne()
	public XY column;

	@Override
	public String toString() {
		return String.format(AB.toStringString, this.id, this.column);
	}

}