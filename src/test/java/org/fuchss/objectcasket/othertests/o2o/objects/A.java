package org.fuchss.objectcasket.othertests.o2o.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//    ------
//   |      |
// 1 v      |
//  ---     |
// | A | x--
//  --- 1
//

@Entity()
@Table(name = "A")
public final class A implements O2O_Object<A> {

	@Id
	public Character pk;

	@OneToOne
	public A other;

	public A() {
	}

	public A(char x) {
		this.pk = x;
	}

	@Override
	public boolean sameAs(A x, A y) {
		return (x == y) || ((x.pk == y.pk) && ( //
		(x.other == y.other //
		) || ((x.other != null) && (y.other != null) && (x.other.pk == y.other.pk))));
	}
}
