package org.fuchss.objectcasket.o2m.variants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_SET_S")
final public class O2M_SET_S implements O2M__S_Object<O2M_SET_S> {

	@Id
	@GeneratedValue
	Integer pk;

	@Override
	public boolean sameAs(O2M_SET_S x, O2M_SET_S y) {
		return (x == y) || ((x != null) && (y != null) && (x.pk.equals(y.pk)));
	}

	@Override
	public String toString() {
		return String.format("O2M_SET_S pk = %s ", this.pk);

	}

}
