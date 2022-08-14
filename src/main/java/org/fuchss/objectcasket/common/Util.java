package org.fuchss.objectcasket.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Common utilities.
 */
public interface Util {

	/**
	 * The method checks whether all names are suitable for tables and columns.
	 *
	 * @param names - the names to check.
	 * @return true iff all names are suitable.
	 */
	static boolean isWellformed(String... names) {
		for (String name : names) {
			if ((name == null) || (name.length() > 256) || !name.matches("[a-zA-Z_][\\w#@$]*"))
				return false;
		}
		return true;
	}

	/**
	 * This method throws a {@link NullPointerException} exception if one object is
	 * null. A multiple version of {@link Objects#requireNonNull(Object)}.
	 *
	 * @param objs - the objects to check.
	 */
	static void objectsNotNull(Object... objs) {
		for (int idx = 0; idx < objs.length; idx++)
			Objects.requireNonNull(objs[idx]);

	}

	/**
	 * This method checks whether the object is a number or not. An instance of
	 * {@link Long}, {@link Integer}, {@link Short}, or {@link Byte}.
	 *
	 * @param obj - possibly a number.
	 * @return true iff the object is a number.
	 */
	static boolean isNumber(Object obj) {
		return ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte));
	}

	/**
	 * Copies a {@link Map} and removes all keys that will be ignored.
	 *
	 * @param <K>    - s type variable for keys.
	 * @param <V>    -a type variable for values.
	 * @param values - the original map.
	 * @param ignore - keys to ignore.
	 * @return a copy of the original map without the ignored keys.
	 */

	@SafeVarargs
	static <K, V> Map<K, V> copyAndIgnore(Map<K, V> values, K... ignore) {
		Map<K, V> result = new HashMap<>(values);
		for (K key : ignore)
			result.remove(key);
		return result;
	}

	/**
	 * An extended equality check where null equals null, but otherwise null is less
	 * than any other value.
	 *
	 * @param <T> - a type variable representing the type of both arguments.
	 * @param x   - the first object.
	 * @param y   - the second object.
	 * @return -1 iff x {@literal <} y
	 * <p>
	 * 1 iff x {@literal >} y
	 * <p>
	 * 0 iff x {@literal =} y
	 */
	static <T extends Comparable<T>> int compare(T x, T y) {
		if (x == null)
			return (y == null ? 0 : -1);
		if (y == null)
			return 1;
		return x.compareTo(y);
	}

}
