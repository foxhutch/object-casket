package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlReal extends SqlObjectImpl {

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {

			if (obj == null)
				return new SqlDouble(null, SqlObject.Type.REAL);
			if ((obj instanceof Float) || (obj instanceof Double)) {
				return new SqlDouble(((Number) obj).doubleValue(), SqlObject.Type.REAL);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}
	}

	protected SqlReal(Type sqlType) throws ConnectorException {
		super(sqlType);
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public <T> T get(Class<T> type) {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

}