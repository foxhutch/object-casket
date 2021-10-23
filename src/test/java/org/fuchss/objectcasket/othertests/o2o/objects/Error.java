package org.fuchss.objectcasket.othertests.o2o.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "ERROR")
public final class Error implements O2O_Object<Error> {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	public B other;

	public Error() {
	}

	public Error(int x) {
		this.pk = x;
	}

	@Override
	public boolean sameAs(Error x, Error y) {
		return (x == y) || ((x.pk == y.pk) && ( //
		(x.other == y.other //
		) || ((x.other != null) && (y.other != null) && (x.other.pk == y.other.pk))));
	}
}