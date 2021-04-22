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

	//
	// This objects store the mapping info: //
	// 1. A Set of proper set of storage classes to store the data for the Sql data
	// type //
	// 2. A dynamic cast to achieve the Java attribute class: SqlObjectToJava //
	// 3. A dynamic cast to achieve one of the appropriate storage classes for
	// Sql-Values: SqlToSqlObject //
	//

	enum Type {
		INTEGER(Long.class, Integer.class, Short.class, Byte.class, Long.TYPE, Integer.TYPE, Short.TYPE, Byte.TYPE), //
		BOOL(Boolean.class, Boolean.TYPE), //
		DOUBLE(Double.class, Double.TYPE), //
		FLOAT(Float.class, Float.TYPE), //
		CHAR(Character.class, Character.TYPE), //
		VARCHAR(String.class), //
		DATE(Date.class), //
		// The following types aren't default types.
		TEXT(), //
		REAL(), //
		NUMERIC(), //
		TIMESTAMP(); //

		public static final Set<SqlObject.Type> PK_SQL_TYPES = new HashSet<>();
		static {
			PK_SQL_TYPES.add(SqlObject.Type.INTEGER);
			PK_SQL_TYPES.add(SqlObject.Type.BOOL);
			PK_SQL_TYPES.add(SqlObject.Type.DOUBLE);
			PK_SQL_TYPES.add(SqlObject.Type.NUMERIC);
			PK_SQL_TYPES.add(SqlObject.Type.FLOAT);
			PK_SQL_TYPES.add(SqlObject.Type.CHAR);
			PK_SQL_TYPES.add(SqlObject.Type.VARCHAR);
			PK_SQL_TYPES.add(SqlObject.Type.TEXT);
			PK_SQL_TYPES.add(SqlObject.Type.DATE);
			PK_SQL_TYPES.add(SqlObject.Type.TIMESTAMP);
			PK_SQL_TYPES.add(SqlObject.Type.REAL);
		}

		public static final Set<Class<?>> PK_JAVA_TYPES = new HashSet<>();
		static {
			PK_SQL_TYPES.forEach(t -> t.types.forEach(PK_JAVA_TYPES::add));
		}

		public static final Set<SqlObject.Type> AUTOINCREMENT_SQL_TYPES = new HashSet<>();
		static {
			AUTOINCREMENT_SQL_TYPES.add(SqlObject.Type.INTEGER);
		}

		public static final Set<Class<?>> AUTOINCREMENT_JAVA_TYPES = new HashSet<>();
		static {
			for (SqlObject.Type sqlType : AUTOINCREMENT_SQL_TYPES) {
				for (Class<?> clazz : sqlType.types)
					if (!clazz.isPrimitive())
						AUTOINCREMENT_JAVA_TYPES.add(clazz);
			}
		}

		private static Map<Class<?>, Type> typeMap = new HashMap<>();
		static {
			for (Type type : Type.values())
				type.types.forEach(t -> Type.typeMap.put(t, type));
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

		public List<? extends Class<?>> mapedTypes() {
			return this.types;
		}

		public boolean isAssignable(String typeName) {
			if (this.name().equals(typeName)) {
				return true;
			}
			return typeName.equals("DATETIME") ? (this.name().equals("DATE") || this.name().equals("TIMESTAMP")) : false;
		}

	}

}
