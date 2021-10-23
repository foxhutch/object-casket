package org.fuchss.objectcasket.json.objects;

import java.util.Set;

public interface JSON_Object<T> {

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
}