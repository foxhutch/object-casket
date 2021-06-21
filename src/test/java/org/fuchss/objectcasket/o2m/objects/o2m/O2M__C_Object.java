package org.fuchss.objectcasket.o2m.objects.o2m;

import java.util.Set;

public interface O2M__C_Object<T> extends O2M__S_Object<T> {

	<X> void add(Set<X> suppliers, X ignore);

	default <X extends O2M__S_Object<X>> boolean check_Supplier(Set<X> x, Set<X> y) {
		if (x.isEmpty() && y.isEmpty())
			return true;
		if (x.size() != y.size())
			return false;

		X xs = x.iterator().next();
		X ys = y.iterator().next();
		return (xs == null) ? ((ys == null) ? true : ys.check(x, y)) : xs.check(x, y);
	}

}
