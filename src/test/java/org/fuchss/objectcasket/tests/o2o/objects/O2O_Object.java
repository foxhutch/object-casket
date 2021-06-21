package org.fuchss.objectcasket.tests.o2o.objects;

import java.util.Set;

public interface O2O_Object<T> {

	boolean sameAs(T x, T y);

	default boolean check(Set<T> xs, Set<T> ys) {
		if (xs.size() != ys.size())
			return false;

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

}
