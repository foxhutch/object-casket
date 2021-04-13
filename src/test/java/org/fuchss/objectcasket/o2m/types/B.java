package org.fuchss.objectcasket.o2m.types;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//
//  --- 1              n -----
// | B | x------------> |  Z  |
//  ---                  -----
//

@Entity()
@Table(name = "B")
public final class B {

	private static final String formatString = "B (id = %s, column1 = %s, column2 = %s, myZs = %s)";

	public B(String pk) {
		this.strId = pk;
	}

	public B() {
	}

	@Id
	public String strId;

	public Integer column1;

	public String column2;

	@OneToMany
	public Set<Z> myZs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(B.formatString, this.strId, this.column1, this.column2, this.myZs());
	}

	private String myZs() {
		String str = "[";
		for (Z z : this.myZs)
			str += (str.equals("[") ? "" : ", ") + z.toString();
		return str + "]";
	}

}
