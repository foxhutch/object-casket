package org.fuchss.objectcasket.o2m.objects.m2o;

import java.util.Set;

public interface M2O__C_Object<T> extends M2O__S_Object<T> {

	<X> void add(Set<X> suppliers, X ignore);

	default <X extends M2O__S_Object<X>> boolean check_Supplier(Set<X> x, Set<X> y) {
		if (x.isEmpty() && y.isEmpty())
			return true;
		if (x.size() != y.size())
			return false;

		X xs = x.iterator().next();
		X ys = y.iterator().next();
		return (xs == null) ? ((ys == null) ? true : ys.check(x, y)) : xs.check(x, y);
	}

}