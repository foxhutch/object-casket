package org.fuchss.objectcasket.common;

import java.io.Serial;
import java.util.HashMap;

/**
 * The implementation of {@link IntolerantHashMap}.
 *
 * @param <K> -a type variable for keys.
 * @param <V> -a type variable for values.
 */
public class IntolerantHashMap<K, V> extends HashMap<K, V> implements IntolerantMap<K, V> {

	@Serial
	private static final long serialVersionUID = 1L;

	@Override
	public V getIfExists(Object key) throws CasketException {
		V val = this.get(key);
		if (val == null) {
			throw CasketError.UNKNOWN_OBJECT.build();
		}
		return val;
	}

	@Override
	public void putIfNew(K key, V val) throws CasketException {
		Util.objectsNotNull(key, val);
		if (this.containsKey(key)) {
			throw CasketError.UNKNOWN_OBJECT.build();
		}
		this.put(key, val);
	}

	@SuppressWarnings("unchecked")
	@Override
	public K keyExists(Object key) throws CasketException {
		if (this.containsKey(key))
			return (K) key;
		throw CasketError.UNKNOWN_OBJECT.build();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V valueExists(Object value) throws CasketException {
		if (this.containsValue(value))
			return (V) value;
		throw CasketError.UNKNOWN_OBJECT.build();
	}

}
