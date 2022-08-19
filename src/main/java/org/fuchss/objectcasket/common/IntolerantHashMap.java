package org.fuchss.objectcasket.common;

import java.io.Serial;
import java.util.HashMap;

import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketError.CE4;

/**
 * The implementation of {@link IntolerantHashMap}.
 *
 * @param <K>
 *            -a type variable for keys.
 * @param <V>
 *            -a type variable for values.
 */
public class IntolerantHashMap<K, V> extends HashMap<K, V> implements IntolerantMap<K, V> {

	@Serial
	private static final long serialVersionUID = 1L;

	@Override
	public V getIfExists(Object key) throws CasketException {
		V val = this.get(key);
		if (val == null) {
			throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Key", key, this.getClass(), this);
		}
		return val;
	}

	@Override
	public void putIfNew(K key, V val) throws CasketException {
		Util.objectsNotNull(key, val);
		if (this.containsKey(key)) {
			throw CE2.KEY_EXISTS.defaultBuild(key, this);
		}
		this.put(key, val);
	}

	@SuppressWarnings("unchecked")
	@Override
	public K keyExists(Object key) throws CasketException {
		if (this.containsKey(key))
			return (K) key;
		throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Key", key, this.getClass(), this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V valueExists(Object value) throws CasketException {
		if (this.containsValue(value))
			return (V) value;
		throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Value", value, this.getClass(), this);
	}

}
