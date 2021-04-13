package org.fuchss.objectcasket.o2m.types;

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
//| C | <------------> |  Z  |
// ---                  -----
//

@Entity()
@Table(name = "C")
public final class C {

	private static final String formatString = "C (id = %s, column1 = %s, column2 = %s, myZs = %s)";
	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@OneToMany
	@JoinColumn(name = "CxZ")
	public Set<Z> myZs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(C.formatString, this.id, this.column1, this.column2, this.myZs());
	}

	private String myZs() {
		String str = "[";
		for (Z z : this.myZs)
			str += (str.equals("[") ? "" : ", ") + z.toString();
		return str + "]";
	}

}