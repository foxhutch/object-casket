package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.util.Date;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlTimestamp extends SqlObjectImpl {

	private static final String TIMESTAMP_FORMAT_STR = "yyyy-MM-dd HH:mm:ss:SSS";

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null)
				return new SqlDate(null, SqlObject.Type.TIMESTAMP, TIMESTAMP_FORMAT_STR);
			if (obj instanceof Date)
				return new SqlDate(((Date) obj).getTime(), SqlObject.Type.TIMESTAMP, TIMESTAMP_FORMAT_STR);
			if (obj instanceof Long)
				return new SqlDate((Long) obj, SqlObject.Type.TIMESTAMP, TIMESTAMP_FORMAT_STR);
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

	protected SqlTimestamp(Type sqlType) throws ConnectorException {
		super(sqlType);
		throw new UnsupportedOperationException("Use SqlDate implicitly");
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDate implicitly");
	}

	@Override
	public <T> T get(Class<T> type) {
		throw new UnsupportedOperationException("Use SqlDate implicitly");
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDate implicitly");
	}

}