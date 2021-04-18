package org.fuchss.objectcasket.o2m;

import java.util.Set;

public interface O2M__C_Object<T> {

	<X> void add(Set<X> suppliers, X ignore);

	boolean sameAs(T x, T y);

	default boolean check(Set<T> xs, Set<T> ys) {
		for (T x : xs) {
			boolean res = false;
			for (T y : ys) {
				res |= this.sameAs(x, y);
			}
			if (res == false)
				return res;
		}
		return true;
	}

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
