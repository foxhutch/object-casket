
package org.fuchss.objectcasket.othertests.justDoNotCrash.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simple3")
public final class ABC {

	private static String toStringString = "simple3(id = %s, column = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@ManyToOne()
	public ABC column = this;

	@Override
	public String toString() {
		return String.format(ABC.toStringString, this.id, (this == this.column) ? "this" : this.column);
	}

}