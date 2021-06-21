package org.fuchss.objectcasket.o2m.objects.other;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//
// --- 1     CxZ      n -----
//| C | <------------> |  D  |
// ---                  -----
//

@Entity()
@Table(name = "C")
public final class C {

	private static final String formatString = "C (id = %s, column1 = %s, column2 = %s, myDs = %s)";
	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@OneToMany
	@JoinColumn(name = "CxD")
	public Set<D> myDs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(C.formatString, this.id, this.column1, this.column2, this.myDs());
	}

	private String myDs() {
		String str = "[";
		for (D d : this.myDs)
			str += (str.equals("[") ? "" : ", ") + d.toString();
		return str + "]";
	}

}