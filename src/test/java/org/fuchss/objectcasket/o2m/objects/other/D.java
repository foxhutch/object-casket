package org.fuchss.objectcasket.o2m.objects.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "D")

public final class D {
	private static final String formatString = "D (id = %s, column1 = %s, column2 = %s, theC = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@ManyToOne
	@Column(name = "CxD")
	public C theC;

	@Override
	public String toString() {
		return String.format(D.formatString, this.id, this.column1, this.column2, this.theC.id);
	}
}