package org.fuchss.objectcasket.tests.o2o.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
//    ------
//   |      |
// 1 v      |
//  ---     |
// | B | x--
//  --- 1
//

@Entity()
@Table(name = "B")
public final class B implements O2O_Object<B> {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	public B other;

	public B() {
	}

	public B(int x) {
		this.pk = x;
	}

	@Override
	public boolean sameAs(B x, B y) {
		return (x == y) || ((x.pk == y.pk) && ( //
		(x.other == y.other //
		) || ((x.other != null) && (y.other != null) && (x.other.pk == y.other.pk))));
	}
}
