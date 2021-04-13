package org.fuchss.objectcasket.o2m.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "Z")

public final class Z {
	private static final String formatString = "Z (id = %s, column1 = %s, column2 = %s, theC = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@ManyToOne
	@Column(name = "CxZ")
	public C theC;

	@Override
	public String toString() {
		return String.format(Z.formatString, this.id, this.column1, this.column2, this.theC.id);
	}
}