package org.fuchss.objectcasket.o2m.types;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//
// --- 1              n -----
//| A | x------------> |  Z  |
// ---                  -----
//

@Entity()
@Table(name = "A")
public final class A {

	private static final String formatString = "A (id = %s, column1 = %s, column2 = %s, myZs = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@OneToMany
	public Set<Z> myZs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(A.formatString, this.id, this.column1, this.column2, this.myZs());
	}

	private String myZs() {
		String str = "[";
		for (Z z : this.myZs)
			str += (str.equals("[") ? "" : ", ") + z.toString();
		return str + "]";
	}

}
