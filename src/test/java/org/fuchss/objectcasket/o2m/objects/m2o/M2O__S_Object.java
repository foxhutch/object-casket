package org.fuchss.objectcasket.o2m.objects.m2o;

import java.util.Set;

public interface M2O__S_Object<T> {

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