package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlNumeric extends SqlObjectImpl {

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		CAST.put(Long.class, n -> n == null ? null : ((Number) n).longValue());
		CAST.put(Integer.class, n -> n == null ? null : ((Number) n).intValue());
		CAST.put(Short.class, n -> n == null ? null : ((Number) n).shortValue());
		CAST.put(Byte.class, n -> n == null ? null : ((Number) n).byteValue());

		CAST.put(Double.class, n -> n == null ? null : ((Number) n).doubleValue());
		CAST.put(Float.class, n -> n == null ? null : ((Number) n).floatValue());

		CAST.put(Long.TYPE, n -> n == null ? ((long) 0) : ((Number) n).longValue());
		CAST.put(Integer.TYPE, n -> n == null ? ((int) 0) : ((Number) n).intValue());
		CAST.put(Short.TYPE, n -> n == null ? ((short) 0) : ((Number) n).shortValue());
		CAST.put(Byte.TYPE, n -> n == null ? ((byte) 0) : ((Number) n).byteValue());

		CAST.put(Double.TYPE, n -> n == null ? ((double) 0.0) : ((Number) n).doubleValue());
		CAST.put(Float.TYPE, n -> n == null ? ((float) 0.0) : ((Number) n).floatValue());
	}

	protected Long longVal;
	protected Double doubleVal;
	boolean isDouble;

	SqlNumeric() throws ConnectorException {
		super(SqlObject.Type.NUMERIC);
	}

	SqlNumeric(Double obj) throws ConnectorException {
		super(SqlObject.Type.NUMERIC);
		this.doubleVal = obj;
		this.isDouble = true;
	}

	SqlNumeric(Long obj) throws ConnectorException {
		super(SqlObject.Type.NUMERIC);
		this.longVal = obj;
		this.isDouble = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type) {
		Function<Object, Object> cast = CAST.get(type);
		if (cast == null)
			return null;
		if (this.isDouble)
			return (T) cast.apply(this.doubleVal);
		return (T) cast.apply(this.longVal);
	}

	@Override
	public String toString() {
		return ((this.longVal == null) && (this.doubleVal == null)) ? null : ((this.isDouble) ? ("" + this.doubleVal) : ("" + this.longVal));
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Long yLong = null;
		Double yDouble = null;
		if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte))
			yLong = ((Number) obj).longValue();
		if ((obj instanceof Double) || (obj instanceof Float))
			yDouble = ((Number) obj).doubleValue();
		if ((yLong == null) && (yDouble == null))
			ObjectException.Error.Incompatible.build();
		if (((this.longVal == null) && (this.doubleVal == null)))
			return -1;
		if (this.isDouble)
			return this.doubleVal.compareTo(yDouble == null ? ((Number) yLong).doubleValue() : yDouble);
		return this.longVal.compareTo(yLong == null ? ((Number) yDouble).longValue() : yLong);
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null)
				return new SqlNumeric();
			if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte))
				return new SqlNumeric(((Number) obj).longValue());
			if ((obj instanceof Double) || (obj instanceof Float))
				return new SqlNumeric(((Number) obj).doubleValue());
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if ((this.longVal == null) && (this.doubleVal == null)) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
				return;
			}
			if (this.isDouble)
				preparedStatement.setObject(pos, this.doubleVal, java.sql.Types.NUMERIC);
			else
				preparedStatement.setObject(pos, this.longVal, java.sql.Types.NUMERIC);
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}