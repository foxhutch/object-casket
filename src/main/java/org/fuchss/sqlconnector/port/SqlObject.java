package org.fuchss.sqlconnector.port;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface SqlObject {

	String toSqlString();

	void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException;

	<T> T get(Class<T> type) throws UnsupportedOperationException;

	Object get();

	int compareTo(Object val) throws ConnectorException; // x.compareTo(y)x<y =>
															// -1 x>y => +1 /
															// x=y => 0

	enum Type {

		INTEGER(DynamicCast.INTEGER, Long.class, Integer.class, Short.class, Byte.class, Long.TYPE, Integer.TYPE, Short.TYPE, Byte.TYPE), //
		BOOL(DynamicCast.BOOL, Boolean.class, Boolean.TYPE), //
		REAL(DynamicCast.REAL), //
		DOUBLE(DynamicCast.DOUBLE, Double.class, Double.TYPE), //
		FLOAT(DynamicCast.FLOAT, Float.class, Float.TYPE), //
		CHAR(DynamicCast.CHAR, Character.class, Character.TYPE), //
		TEXT(DynamicCast.TEXT), //
		VARCHAR(DynamicCast.VARCHAR, String.class), //
		NUMERIC(DynamicCast.NUMERIC), //
		DATE(DynamicCast.DATE, Date.class), //
		TIMESTAMP(DynamicCast.TIMESTAMP);

		public static final Set<SqlObject.Type> PK_TYPES = new HashSet<SqlObject.Type>() {
			private static final long serialVersionUID = 1L;
			{
				this.add(SqlObject.Type.INTEGER);
				this.add(SqlObject.Type.TEXT);
			}
		};

		public static final Set<SqlObject.Type> AUTOINCREMENT_TYPES = new HashSet<SqlObject.Type>() {
			private static final long serialVersionUID = 1L;
			{
				this.add(SqlObject.Type.INTEGER);
			}
		};

		private static Map<Class<?>, Type> typeMap = new HashMap<>();

		private final List<Class<?>> types;
		private final Map<Class<?>, Function<Object, Object>> casts;

		private Type(Map<Class<?>, Function<Object, Object>> casts, Class<?>... classes) {
			this.types = Arrays.asList(classes);
			this.casts = casts;

		}

		static {
			for (Type type : Type.values()) {
				type.types.forEach(t -> Type.typeMap.put(t, type));
			}
		}

		public static Type getDefaultType(Class<?> clazz) {
			return Type.typeMap.get(clazz);
		}

		@SuppressWarnings("unchecked")
		public <T> T get(SqlObject obj, Class<T> clazz) {
			return (T) this.casts.get(clazz).apply(obj.get());
		}

		public boolean isAssignable(String typeName) {
			if (this.name().equals(typeName)) {
				return true;
			}
			return typeName.equals("DATETIME") ? (this.name().equals("DATE") || this.name().equals("TIMESTAMP")) : false;
		}

		private static class DynamicCast {

			private static final Map<Class<?>, Function<Object, Object>> INTEGER = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Long.class, n -> n);
					this.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());
					this.put(Long.TYPE, n -> n);
					this.put(Integer.TYPE, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.TYPE, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.TYPE, n -> n == null ? null : ((Number) n).byteValue());
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> BOOL = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Boolean.class, n -> n);
					this.put(Boolean.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> DOUBLE = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Double.class, n -> n);
					this.put(Double.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> FLOAT = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Float.class, n -> n);
					this.put(Float.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> CHAR = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Character.class, n -> n);
					this.put(Character.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> VARCHAR = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(String.class, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> DATE = new HashMap<Class<?>, Function<Object, Object>>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Date.class, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> REAL = DynamicCast.DOUBLE;
			private static final Map<Class<?>, Function<Object, Object>> TEXT = DynamicCast.VARCHAR;
			private static final Map<Class<?>, Function<Object, Object>> NUMERIC = DynamicCast.DOUBLE;
			private static final Map<Class<?>, Function<Object, Object>> TIMESTAMP = DynamicCast.DATE;

		}

	}

}
