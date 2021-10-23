package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlFloat extends SqlObjectImpl {

	static class SqlBuilder extends SqlObjectBuilderImpl {
		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlDouble(null, SqlObject.Type.FLOAT);
			}
			if ((obj instanceof Float) || (obj instanceof Double)) {
				Float f = ((Number) obj).floatValue();
				return new SqlDouble(((Number) f).doubleValue(), SqlObject.Type.FLOAT);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}
	}

	protected SqlFloat(Type sqlType) throws ConnectorException {
		super(sqlType);
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public <T> T get(Class<T> type, Field target) {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Use SqlDouble implicitly");
	}

}