package org.fuchss.objectcasket.o2m.variants;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__C_Object;

//
//------ 1              n -----
//| C   | x------------> |  S  |
//------                  -----
//

@Entity()
@Table(name = "O2M_SET_C")
final public class O2M_SET_C implements O2M__C_Object<O2M_SET_C> {

	@Id
	@GeneratedValue
	Integer pk;

	@OneToMany
	public Set<O2M_SET_S> supplier = new HashSet<>();

	@Override
	public boolean sameAs(O2M_SET_C x, O2M_SET_C y) {
		if (x == y)
			return true;
		if (!((x != null) && (y != null) && (x.pk.equals(y.pk))))
			return false;
		if (!this.check_Supplier(x.supplier, y.supplier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("O2M_SET_C pk = %s ", this.pk);
	}

	@Override
	public <X> void add(Set<X> suppliers, X ignore) {
		if (ignore instanceof O2M_SET_S)
			suppliers.forEach(s -> this.supplier.add((O2M_SET_S) s));
	}

}
