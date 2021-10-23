package org.fuchss.sqlconnector.port;

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

	<T> T get(Class<T> type);

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
			PK_SQL_TYPES.add(INTEGER);
			PK_SQL_TYPES.add(BOOL);
			PK_SQL_TYPES.add(DOUBLE);
			PK_SQL_TYPES.add(NUMERIC);
			PK_SQL_TYPES.add(FLOAT);
			PK_SQL_TYPES.add(CHAR);
			PK_SQL_TYPES.add(VARCHAR);
			PK_SQL_TYPES.add(TEXT);
			PK_SQL_TYPES.add(DATE);
			PK_SQL_TYPES.add(TIMESTAMP);
			PK_SQL_TYPES.add(REAL);
		}

		public static final Set<Class<?>> PK_JAVA_TYPES = new HashSet<>();
		static {
			PK_SQL_TYPES.forEach(t -> t.types.forEach(Type.PK_JAVA_TYPES::add));
		}

		public static final Set<SqlObject.Type> AUTOINCREMENT_SQL_TYPES = new HashSet<>();
		static {
			AUTOINCREMENT_SQL_TYPES.add(INTEGER);
		}

		public static final Set<Class<?>> AUTOINCREMENT_JAVA_TYPES = new HashSet<>();
		static {
			for (SqlObject.Type sqlType : AUTOINCREMENT_SQL_TYPES) {
				for (Class<?> clazz : sqlType.types) {
					if (!clazz.isPrimitive()) {
						AUTOINCREMENT_JAVA_TYPES.add(clazz);
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
			possibleClassMap.get(TEXT.name()).addAll(possibleClassMap.get(VARCHAR.name()));
			possibleClassMap.get(REAL.name()).addAll(possibleClassMap.get(DOUBLE.name()));
			possibleClassMap.get(REAL.name()).addAll(possibleClassMap.get(FLOAT.name()));
			possibleClassMap.get(NUMERIC.name()).addAll(possibleClassMap.get(INTEGER.name()));
			possibleClassMap.get(NUMERIC.name()).addAll(possibleClassMap.get(REAL.name()));
			possibleClassMap.get(TIMESTAMP.name()).addAll(possibleClassMap.get(DATE.name()));

		}

		public static Type getDefaultType(Class<?> clazz, String columnDefinition) {
			Type type = typeMap.get(clazz);
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
			Set<Class<?>> possibleClasses = possibleClassMap.get(typeName);
			return ((possibleClasses == null) || !possibleClasses.contains(clazz)) ? null : type;
		}

		public static Type getDefaultType(Class<?> clazz) {
			return Type.typeMap.get(clazz);
		}

		/*
		 * ##########################################################################
		 * non static part
		 */

		private final List<Class<?>> types;

		private Type(Class<?>... classes) {
			this.types = Arrays.asList(classes);
		}

	}

}
