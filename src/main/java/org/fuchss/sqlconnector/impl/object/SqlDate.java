package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlDate extends SqlObjectImpl {

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		CAST.put(Date.class, n -> (n == null) ? null : new Date((Long) n));
		CAST.put(Long.class, n -> n);
		CAST.put(Long.TYPE, n -> (n == null) ? 0 : n);
	}

	private static final Map<SqlObject.Type, Integer> TARGET = new HashMap<>();
	static {
		TARGET.put(SqlObject.Type.DATE, java.sql.Types.DATE);
		TARGET.put(SqlObject.Type.TIMESTAMP, java.sql.Types.TIMESTAMP);
	}

	private static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

	protected Long val;
	protected DateFormat defaultFormatter;

	SqlDate(Long obj, SqlObject.Type type, String formatStr) throws ConnectorException {
		super(type);
		this.defaultFormatter = new SimpleDateFormat(formatStr);
		this.val = obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type) {
		Function<Object, Object> cast = CAST.get(type);
		if (cast == null)
			return null;
		return (T) CAST.get(type).apply(this.val);
	}

	@Override
	public String toString() {
		return (this.val == null) ? null : this.defaultFormatter.format(new Date(this.val));
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Long y = null;
		if (obj instanceof Date)
			y = ((Date) obj).getTime();
		if (obj instanceof Long)
			y = (Long) obj;
		if (y == null)
			ObjectException.Error.Incompatible.build();
		return (this.val == null) ? -1 : this.val.compareTo(y);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, TARGET.get(this.sqlType));
			} else
				preparedStatement.setObject(pos, this.val, TARGET.get(this.sqlType));
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null)
				return new SqlDate(null, SqlObject.Type.DATE, SqlDate.DATE_FORMAT_STR);
			if (obj instanceof Date)
				return new SqlDate(((Date) obj).getTime(), SqlObject.Type.DATE, SqlDate.DATE_FORMAT_STR);
			if (obj instanceof Long)
				return new SqlDate((Long) obj, SqlObject.Type.DATE, SqlDate.DATE_FORMAT_STR);
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

}