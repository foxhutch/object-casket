package org.fuchss.objectcasket.sqlconnector.port;

import java.io.Serializable;
import java.util.*;

/**
 * This enumeration defines the mapping between java types and SQL types.
 */
@SuppressWarnings("unchecked")
public enum StorageClass {

	/**
	 * The Java types long, int, short, byte, boolean and also the class Date are
	 * mapped to SQL INTEGER.
	 */
	LONG(Long.class, Long.TYPE, Integer.class, Integer.TYPE, Short.class, Short.TYPE, Byte.class, Byte.TYPE, Boolean.class, Boolean.TYPE, Date.class),

	/**
	 * The Java types double and float are mapped to SQL REAL.
	 */
	DOUBLE(Double.class, Double.TYPE, Float.class, Float.TYPE),

	/**
	 * The Java type char and also the class String are mapped to SQL TEXT.
	 */
	TEXT(Character.class, Character.TYPE, String.class),

	/**
	 * All java classes which are serializable can be stored in a SQL BLOB.
	 */
	BLOB(Serializable.class);

	private static final Set<StorageClass> thePkSqlTypes = new HashSet<>();

	static {
		StorageClass.thePkSqlTypes.add(LONG);
		StorageClass.thePkSqlTypes.add(DOUBLE);
		StorageClass.thePkSqlTypes.add(TEXT);
	}

	/**
	 * Only INTEGER, REAL, and TEXT are possible SQL types for primary keys.
	 */
	public static final Set<StorageClass> PK_SQL_TYPES = Collections.unmodifiableSet(thePkSqlTypes);

	private static final Set<Class<? extends Serializable>> thePkJavaTypes = new HashSet<>();

	static {
		StorageClass.PK_SQL_TYPES.forEach(t -> StorageClass.thePkJavaTypes.addAll(t.types));
	}

	/**
	 * All Java types that are mapped to an SQL INTEGER, REAL, or TEXT can be used
	 * as primary keys.
	 */
	public static final Set<Class<? extends Serializable>> PK_JAVA_TYPES = Collections.unmodifiableSet(thePkJavaTypes);

	private static final Set<StorageClass> theAutoIncrementedSqlTypes = new HashSet<>();

	static {
		StorageClass.theAutoIncrementedSqlTypes.add(LONG);
	}

	/**
	 * INTEGER is the only possible SQL type for auto generated primary keys.
	 */
	public static final Set<StorageClass> AUTOINCREMENT_SQL_TYPES = Collections.unmodifiableSet(theAutoIncrementedSqlTypes);

	private static final Set<Class<? extends Serializable>> theAutoIncrementedJavaTypes = new HashSet<>();

	static {
		for (StorageClass sqlType : StorageClass.AUTOINCREMENT_SQL_TYPES) {
			for (Class<? extends Serializable> clazz : sqlType.types) {
				if (!clazz.isPrimitive()) { // only non-primitive types can be null
					StorageClass.theAutoIncrementedJavaTypes.add(clazz);
				}
			}
		}
	}

	/**
	 * All Java classes that are mapped to an SQL INTEGER are suitable for auto
	 * generated primary keys.
	 */
	public static final Set<Class<? extends Serializable>> AUTO_INCREMENTED_JAVA_TYPES = Collections.unmodifiableSet(theAutoIncrementedJavaTypes);

	private static final Map<Class<? extends Serializable>, StorageClass> theTypeMap = new HashMap<>();

	static {
		for (StorageClass type : StorageClass.values()) {
			type.types.forEach(t -> StorageClass.theTypeMap.put(t, type));
		}
	}

	/**
	 * The mapping between Java types and SQL types.
	 */
	public static final Map<Class<? extends Serializable>, StorageClass> TYPE_MAP = Collections.unmodifiableMap(theTypeMap);

	private static final Map<String, Set<Class<? extends Serializable>>> thePossibleClassMap = new HashMap<>();

	static {
		for (StorageClass type : StorageClass.values()) {
			StorageClass.thePossibleClassMap.put(type.name(), new HashSet<>(type.types));
		}
	}

	/**
	 * The reverse mapping between SQL types the set of corresponding Java types.
	 */
	public static final Map<String, Set<Class<? extends Serializable>>> POSSIBLE_CLASS_MAP = Collections.unmodifiableMap(thePossibleClassMap);

	/*
	 * ##########################################################################
	 * non static part
	 * ##########################################################################
	 */

	/**
	 * All mapped types.
	 */
	private final List<Class<? extends Serializable>> types;

	/**
	 * @param classes - the mapping.
	 */
	StorageClass(Class<? extends Serializable>... classes) {
		this.types = Arrays.asList(classes);
	}
}