package org.fuchss.objectcasket.o2m.objects.other;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//
// --- 1              n -----
//| A | x------------> |  D  |
// ---                  -----
//

@Entity()
@Table(name = "A")
public final class A {

	private static final String formatString = "A (id = %s, column1 = %s, column2 = %s, myDs = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	public Integer column1;

	public String column2;

	@OneToMany
	public Set<D> myDs = new HashSet<>();

	@Override
	public String toString() {
		return String.format(A.formatString, this.id, this.column1, this.column2, this.myDs());
	}

	private String myDs() {
		String str = "[";
		for (D d : this.myDs)
			str += (str.equals("[") ? "" : ", ") + d.toString();
		return str + "]";
	}

}
