package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlInteger extends SqlObjectImpl {

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		SqlInteger.CAST.put(Long.class, n -> n);
		SqlInteger.CAST.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
		SqlInteger.CAST.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
		SqlInteger.CAST.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());
		SqlInteger.CAST.put(Long.TYPE, n -> n == null ? ((long) 0) : ((Number) n).longValue());
		SqlInteger.CAST.put(Integer.TYPE, n -> n == null ? ((int) 0) : ((Number) n).intValue());
		SqlInteger.CAST.put(Short.TYPE, n -> n == null ? ((short) 0) : ((Number) n).shortValue());
		SqlInteger.CAST.put(Byte.TYPE, n -> n == null ? ((byte) 0) : ((Number) n).byteValue());
	}

	protected Long val;

	SqlInteger(Long obj) throws ConnectorException {
		super(SqlObject.Type.INTEGER);
		this.val = obj;
	}

	@Override
	public String toString() {
		return (this.val == null) ? null : "" + this.val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type, Field target) {
		Function<Object, Object> cast = SqlInteger.CAST.get(type);
		if (cast == null) {
			return null;
		}
		return (T) SqlInteger.CAST.get(type).apply(this.val);
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Long y = null;
		if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte)) {
			y = ((Number) obj).longValue();
		}
		if (y == null) {
			ObjectException.Error.Incompatible.build();
		}
		return (this.val == null) ? -1 : this.val.compareTo(y);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.INTEGER);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlInteger(null);
			}
			if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte)) {
				return new SqlInteger(((Number) obj).longValue());
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

}
