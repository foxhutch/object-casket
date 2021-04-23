package org.fuchss.objectcasket.o2m.variants;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__C_Object;

@Entity()
@Table(name = "O2M_DEQUE_C")
final public class O2M_DEQUE_C implements O2M__C_Object<O2M_DEQUE_C> {

	@Id
	@GeneratedValue
	Integer pk;

	@OneToMany
	public Deque<O2M_DEQUE_S> supplier = new LinkedList<>();

	@Override
	public boolean sameAs(O2M_DEQUE_C x, O2M_DEQUE_C y) {
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
		return String.format("%s pk = %s ", this.getClass().getSimpleName(), this.pk);
	}

	@Override
	public <X> void add(Set<X> suppliers, X ignore) {
		if (ignore instanceof O2M_DEQUE_S)
			suppliers.forEach(s -> this.supplier.add((O2M_DEQUE_S) s));
	}

}
