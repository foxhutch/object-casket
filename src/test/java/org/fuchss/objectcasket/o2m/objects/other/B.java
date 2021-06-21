package org.fuchss.objectcasket.o2m.objects.other;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//
//  --- 1              n -----
// | B | x------------> |  D  |
//  ---                  -----
//

@Entity()
@Table(name = "B")
public final class B {

	private static final String formatString = "B (id = %s, column1 = %s, column2 = %s, myDs = %s)";

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
	public Set<D> myDs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(B.formatString, this.strId, this.column1, this.column2, this.myDs());
	}

	private String myDs() {
		String str = "[";
		for (D d : this.myDs)
			str += (str.equals("[") ? "" : ", ") + d.toString();
		return str + "]";
	}

}
