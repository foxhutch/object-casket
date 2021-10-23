package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlDouble extends SqlObjectImpl {

	protected Double val;

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		SqlDouble.CAST.put(Double.class, n -> n);
		SqlDouble.CAST.put(Float.class, n -> (n == null) ? null : ((Number) n).floatValue());
		SqlDouble.CAST.put(Double.TYPE, n -> (n == null) ? ((double) 0.0) : n);
		SqlDouble.CAST.put(Float.TYPE, n -> (n == null) ? ((float) 0.0) : ((Number) n).floatValue());
	}

	private static final Map<SqlObject.Type, Integer> TARGET = new HashMap<>();
	static {
		SqlDouble.TARGET.put(SqlObject.Type.DOUBLE, java.sql.Types.DOUBLE);
		SqlDouble.TARGET.put(SqlObject.Type.FLOAT, java.sql.Types.FLOAT);
		SqlDouble.TARGET.put(SqlObject.Type.REAL, java.sql.Types.REAL);
	}

	SqlDouble(Double obj, SqlObject.Type type) throws ConnectorException {
		super(type);
		this.val = obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type, Field target) {
		Function<Object, Object> cast = SqlDouble.CAST.get(type);
		if (cast == null) {
			return null;
		}
		return (T) SqlDouble.CAST.get(type).apply(this.val);
	}

	@Override
	public String toString() {
		return this.val == null ? null : ("" + this.val);
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Double y = null;
		if ((obj instanceof Double) || (obj instanceof Float)) {
			y = ((Number) obj).doubleValue();
		}
		if (y == null) {
			ObjectException.Error.Incompatible.build();
		}
		return this.val == null ? -1 : this.val.compareTo(y);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, SqlDouble.TARGET.get(this.sqlType));
			} else {
				preparedStatement.setObject(pos, this.val, SqlDouble.TARGET.get(this.sqlType));
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {

			if (obj == null) {
				return new SqlDouble(null, SqlObject.Type.DOUBLE);
			}
			if (obj instanceof Double) {
				return new SqlDouble((Double) obj, SqlObject.Type.DOUBLE);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

}