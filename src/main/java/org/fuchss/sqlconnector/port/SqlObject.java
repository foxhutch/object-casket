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

	void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException;

	<T> T get(Class<T> type) throws UnsupportedOperationException;

	Object get();

	int compareTo(Object val) throws ConnectorException; // x.compareTo(y)x<y =>
															// -1 x>y => +1 /
															// x=y => 0

	enum Type {

		INTEGER(DynamicCast.INTEGER, Long.class, Integer.class, Short.class, Byte.class, Long.TYPE, Integer.TYPE, Short.TYPE, Byte.TYPE), //
		BOOL(DynamicCast.BOOL, Boolean.class, Boolean.TYPE), //
		DOUBLE(DynamicCast.DOUBLE, Double.class, Double.TYPE), //
		FLOAT(DynamicCast.FLOAT, Float.class, Float.TYPE), //
		CHAR(DynamicCast.CHAR, Character.class, Character.TYPE), //
		VARCHAR(DynamicCast.VARCHAR, String.class), //
		DATE(DynamicCast.DATE, Date.class), //
		TEXT(DynamicCast.TEXT), //
		REAL(DynamicCast.REAL), //
		NUMERIC(DynamicCast.NUMERIC), //
		TIMESTAMP(DynamicCast.TIMESTAMP);

		public static final Set<SqlObject.Type> PK_SQL_TYPES = new HashSet<>() {
			private static final long serialVersionUID = 1L;
			{
				this.add(SqlObject.Type.INTEGER);
				this.add(SqlObject.Type.BOOL);
				this.add(SqlObject.Type.DOUBLE);
				this.add(SqlObject.Type.NUMERIC);
				this.add(SqlObject.Type.FLOAT);
				this.add(SqlObject.Type.CHAR);
				this.add(SqlObject.Type.VARCHAR);
				this.add(SqlObject.Type.TEXT);
				this.add(SqlObject.Type.DATE);
				this.add(SqlObject.Type.TIMESTAMP);

				// unused types
				// this.add(SqlObject.Type.REAL);

			}
		};

		public static final Set<Class<?>> PK_JAVA_TYPES = new HashSet<>() {
			private static final long serialVersionUID = 1L;
			{
				PK_SQL_TYPES.forEach(t -> t.types.forEach(this::add));
			}
		};

		public static final Set<SqlObject.Type> AUTOINCREMENT_SQL_TYPES = new HashSet<>() {
			private static final long serialVersionUID = 1L;
			{
				this.add(SqlObject.Type.INTEGER);
			}
		};

		public static final Set<Class<?>> AUTOINCREMENT_JAVA_TYPES = new HashSet<>() {
			private static final long serialVersionUID = 1L;
			{
				for (SqlObject.Type sqlType : AUTOINCREMENT_SQL_TYPES) {
					for (Class<?> clazz : sqlType.types)
						if (!clazz.isPrimitive())
							this.add(clazz);
				}
			}
		};

		private static Map<Class<?>, Type> typeMap = new HashMap<>();

		private final List<Class<?>> types;
		private final Map<Class<?>, Function<Object, Object>> casts;

		private Type(Map<Class<?>, Function<Object, Object>> casts, Class<?>... classes) {
			this.types = Arrays.asList(classes);
			this.casts = casts;
		}

		public List<? extends Class<?>> mapedTypes() {
			return this.types;
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

			private static final Map<Class<?>, Function<Object, Object>> INTEGER = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Long.class, n -> n == null ? null : ((Number) n).longValue());
					this.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());
					this.put(Long.TYPE, n -> n == null ? null : ((Number) n).longValue());
					this.put(Integer.TYPE, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.TYPE, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.TYPE, n -> n == null ? null : ((Number) n).byteValue());
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> BOOL = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Boolean.class, n -> n);
					this.put(Boolean.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> DOUBLE = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Double.class, n -> n);
					this.put(Double.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> FLOAT = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Float.class, n -> n);
					this.put(Float.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> CHAR = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Character.class, n -> n);
					this.put(Character.TYPE, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> VARCHAR = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(String.class, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> DATE = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Date.class, n -> n);
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> NUMERIC = new HashMap<>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(Long.class, n -> n == null ? null : ((Number) n).longValue());
					this.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());
					this.put(Long.TYPE, n -> n == null ? null : ((Number) n).longValue());
					this.put(Integer.TYPE, n -> n == null ? null : ((Number) n).intValue());
					this.put(Short.TYPE, n -> n == null ? null : ((Number) n).shortValue());
					this.put(Byte.TYPE, n -> n == null ? null : ((Number) n).byteValue());
					this.put(Double.class, n -> n == null ? null : ((Number) n).doubleValue());
					this.put(Double.TYPE, n -> n == null ? null : ((Number) n).doubleValue());
					this.put(Float.class, n -> n == null ? null : ((Number) n).floatValue());
					this.put(Float.TYPE, n -> n == null ? null : ((Number) n).floatValue());
				}
			};

			private static final Map<Class<?>, Function<Object, Object>> REAL = DynamicCast.DOUBLE;
			private static final Map<Class<?>, Function<Object, Object>> TEXT = DynamicCast.VARCHAR;
			private static final Map<Class<?>, Function<Object, Object>> TIMESTAMP = DynamicCast.DATE;

		}

	}

}
