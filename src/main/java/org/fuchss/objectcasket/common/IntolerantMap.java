package org.fuchss.objectcasket.common;

import java.util.Map;

/**
 * A special variant of a {@link Map} which guarantees that an existing key
 * cannot be rewritten and only existing objects can be retrieved.
 *
 *
 * @param <K>
 *            - a type variable for keys.
 * @param <V>
 *            -a type variable for values.
 *
 * @see Map
 */
public interface IntolerantMap<K, V> extends Map<K, V> {

	/**
	 *
	 * @param key
	 *            - the key.
	 * @return the stored VALUE to which the key is mapped.
	 * @throws CasketException
	 *             if no mapping for this key exists or the value is null.
	 *
	 * @see Map#get(Object)
	 */

	V getIfExists(Object key) throws CasketException;

	/**
	 *
	 * @param key
	 *            - the key.
	 * @param val
	 *            - the value.
	 * @throws CasketException
	 *             if one of the parameters are null or a mapping for this key
	 *             already exists.
	 * @see Map#put(Object, Object)
	 */
	void putIfNew(K key, V val) throws CasketException;

	/**
	 * This method casts the key to the correct type and checks that the key exists.
	 *
	 * @param key
	 *            - an existing key.
	 * @return the same object.
	 * @throws CasketException
	 *             if the key is not mapped to any object.
	 */
	K keyExists(Object key) throws CasketException;

	/**
	 * This method casts the value to the correct type and checks that a mapping for
	 * this value exists.
	 *
	 * @param value
	 *            - an existing value.
	 * @return the same object.
	 * @throws CasketException
	 *             if no mapping exists or the value is null.
	 */
	V valueExists(Object value) throws CasketException;

}
