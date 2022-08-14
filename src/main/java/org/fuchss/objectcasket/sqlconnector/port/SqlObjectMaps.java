package org.fuchss.objectcasket.sqlconnector.port;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Additional {@link SqlObject} features.
 *
 *
 */
public abstract class SqlObjectMaps implements SqlObject {

	private static final Map<Class<? extends Serializable>, UnaryOperator<Object>> _CAST = new HashMap<>();
	static {

		SqlObjectMaps._CAST.put(String.class, n -> n == null ? null : n.toString());
		SqlObjectMaps._CAST.put(Character.class, n -> n == null ? null : n.toString().charAt(0));
		SqlObjectMaps._CAST.put(Double.class, n -> n == null ? null : ((Number) n).doubleValue());
		SqlObjectMaps._CAST.put(Float.class, n -> n == null ? null : ((Number) n).floatValue());
		SqlObjectMaps._CAST.put(Long.class, n -> n == null ? null : ((Number) n).longValue());
		SqlObjectMaps._CAST.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
		SqlObjectMaps._CAST.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
		SqlObjectMaps._CAST.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());
		SqlObjectMaps._CAST.put(Boolean.class, n -> n == null ? null : ((Number) n).longValue() != 0);
		SqlObjectMaps._CAST.put(Date.class, n -> n == null ? null : new Date(((Number) n).longValue()));
		SqlObjectMaps._CAST.put(Character.TYPE, n -> n == null ? ((char) 0) : n.toString().charAt(0));
		SqlObjectMaps._CAST.put(Double.TYPE, n -> n == null ? ((double) 0.0) : ((Number) n).doubleValue());
		SqlObjectMaps._CAST.put(Float.TYPE, n -> n == null ? ((float) 0.0) : ((Number) n).floatValue());
		SqlObjectMaps._CAST.put(Long.TYPE, n -> n == null ? ((long) 0) : ((Number) n).longValue());
		SqlObjectMaps._CAST.put(Integer.TYPE, n -> n == null ? ((int) 0) : ((Number) n).intValue());
		SqlObjectMaps._CAST.put(Short.TYPE, n -> n == null ? ((short) 0) : ((Number) n).shortValue());
		SqlObjectMaps._CAST.put(Byte.TYPE, n -> n == null ? ((byte) 0) : ((Number) n).byteValue());
		SqlObjectMaps._CAST.put(Boolean.TYPE, n -> (n != null) && ((((Number) n).longValue()) != 0));
	}

	/**
	 * A set of operations to convert an element storable in a database to the
	 * corresponding Java type or class object.
	 */
	protected static final Map<Class<? extends Serializable>, UnaryOperator<Object>> CAST = Collections.unmodifiableMap(_CAST);

	private static final Map<Class<? extends Serializable>, Class<? extends Serializable>> AUTOBOX = new HashMap<>();
	static {
		SqlObjectMaps.AUTOBOX.put(Character.TYPE, Character.class);
		SqlObjectMaps.AUTOBOX.put(Double.TYPE, Double.class);
		SqlObjectMaps.AUTOBOX.put(Float.TYPE, Float.class);
		SqlObjectMaps.AUTOBOX.put(Long.TYPE, Long.class);
		SqlObjectMaps.AUTOBOX.put(Integer.TYPE, Integer.class);
		SqlObjectMaps.AUTOBOX.put(Short.TYPE, Short.class);
		SqlObjectMaps.AUTOBOX.put(Byte.TYPE, Byte.class);
		SqlObjectMaps.AUTOBOX.put(Boolean.TYPE, Boolean.class);
	}

	/**
	 * For internal use only.
	 */
	protected SqlObjectMaps() {
	}

	/**
	 * This operation converts a Java type in the corresponding Java class. So it is
	 * possible to check whether an SQL object can be assigned to an attribute which
	 * is only a Java type.
	 *
	 * @param clazz
	 *            - a Java class or type.
	 * @return the class for the type and the class for the class.
	 */
	public static Class<? extends Serializable> respectBoxing(Class<? extends Serializable> clazz) {
		Class<? extends Serializable> boxed = AUTOBOX.get(clazz);
		return boxed == null ? clazz : boxed;
	}

}
