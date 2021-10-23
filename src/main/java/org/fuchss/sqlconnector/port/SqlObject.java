package org.fuchss.sqlconnector.port;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SqlObject {

	void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException;

	<T> T get(Class<T> type, Field target);

	// Object get();

	int compareTo(Object val) throws ConnectorException; // x.compareTo(y)x<y =>
															// -1 x>y => +1 /
															// x=y => 0

	enum Type {
		INTEGER(Long.class, Long.TYPE, //
				Integer.class, Integer.TYPE, //
				Short.class, Short.TYPE, //
				Byte.class, Byte.TYPE), //
		BOOL(Boolean.class, Boolean.TYPE), //
		DOUBLE(Double.class, Double.TYPE), //
		FLOAT(Float.class, Float.TYPE), //
		CHAR(Character.class, Character.TYPE), //
		VARCHAR(String.class), //
		DATE(Date.class), //
		// The following types aren't default types.
		BLOB(), //
		TEXT(), //
		REAL(), //
		NUMERIC(), //
		TIMESTAMP(), //
		JSON(); //

		public static final Set<SqlObject.Type> PK_SQL_TYPES = new HashSet<>();
		static {
			Type.PK_SQL_TYPES.add(INTEGER);
			Type.PK_SQL_TYPES.add(BOOL);
			Type.PK_SQL_TYPES.add(DOUBLE);
			Type.PK_SQL_TYPES.add(NUMERIC);
			Type.PK_SQL_TYPES.add(FLOAT);
			Type.PK_SQL_TYPES.add(CHAR);
			Type.PK_SQL_TYPES.add(VARCHAR);
			Type.PK_SQL_TYPES.add(TEXT);
			Type.PK_SQL_TYPES.add(DATE);
			Type.PK_SQL_TYPES.add(TIMESTAMP);
			Type.PK_SQL_TYPES.add(REAL);
		}

		public static final Set<Class<?>> PK_JAVA_TYPES = new HashSet<>();
		static {
			Type.PK_SQL_TYPES.forEach(t -> t.types.forEach(Type.PK_JAVA_TYPES::add));
		}

		public static final Set<SqlObject.Type> AUTOINCREMENT_SQL_TYPES = new HashSet<>();
		static {
			Type.AUTOINCREMENT_SQL_TYPES.add(INTEGER);
		}

		public static final Set<Class<?>> AUTOINCREMENT_JAVA_TYPES = new HashSet<>();
		static {
			for (SqlObject.Type sqlType : Type.AUTOINCREMENT_SQL_TYPES) {
				for (Class<?> clazz : sqlType.types) {
					if (!clazz.isPrimitive()) {
						Type.AUTOINCREMENT_JAVA_TYPES.add(clazz);
					}
				}
			}
		}

		private static Map<Class<?>, Type> typeMap = new HashMap<>();
		static {
			for (Type type : Type.values()) {
				type.types.forEach(t -> Type.typeMap.put(t, type));
			}
		}

		private static Map<String, Set<Class<?>>> possibleClassMap = new HashMap<>();
		static {
			for (Type type : Type.values()) {
				Type.possibleClassMap.put(type.name(), new HashSet<>(type.types));
			}
			Type.possibleClassMap.get(TEXT.name()).addAll(Type.possibleClassMap.get(VARCHAR.name()));
			Type.possibleClassMap.get(REAL.name()).addAll(Type.possibleClassMap.get(DOUBLE.name()));
			Type.possibleClassMap.get(REAL.name()).addAll(Type.possibleClassMap.get(FLOAT.name()));
			Type.possibleClassMap.get(NUMERIC.name()).addAll(Type.possibleClassMap.get(INTEGER.name()));
			Type.possibleClassMap.get(NUMERIC.name()).addAll(Type.possibleClassMap.get(REAL.name()));
			Type.possibleClassMap.get(TIMESTAMP.name()).addAll(Type.possibleClassMap.get(DATE.name()));

		}

		public static Type getDefaultType(Class<?> clazz, String columnDefinition) {
			Type type = Type.typeMap.get(clazz);
			if ((columnDefinition == null) || columnDefinition.isEmpty()) {
				return type;
			}
			String typeName = columnDefinition.strip().toUpperCase();
			if (BLOB.name().equals(typeName)) {
				return BLOB;
			}
			if (JSON.name().equals(typeName)) {
				return JSON;
			}
			Set<Class<?>> possibleClasses = Type.possibleClassMap.get(typeName);
			return ((possibleClasses == null) || !possibleClasses.contains(clazz)) ? null : type;
		}

		public static Type getDefaultType(Class<?> clazz) {
			return Type.typeMap.get(clazz);
		}

		/*
		 * #####################################################################
		 * ##### non static part
		 */

		private final List<Class<?>> types;

		private Type(Class<?>... classes) {
			this.types = Arrays.asList(classes);
		}

	}

}
