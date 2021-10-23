package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlText extends SqlObjectImpl {

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlVarchar(null, SqlObject.Type.TEXT);
			}
			if (obj instanceof String) {
				return new SqlVarchar((String) obj, SqlObject.Type.TEXT);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

	protected SqlText(Type sqlType) throws ConnectorException {
		super(sqlType);
		throw new UnsupportedOperationException("Use SqlVarchar implicitly");
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlVarchar implicitly");

	}

	@Override
	public <T> T get(Class<T> type, Field target) {
		throw new UnsupportedOperationException("Use SqlVarchar implicitly");
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlVarchar implicitly");
	}

}
