package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlVarchar extends SqlObjectImpl {

	protected String val;

	SqlVarchar(String obj, SqlObject.Type type) throws ConnectorException {
		super(type);
		this.val = obj;
	}

	@Override
	public <T> T get(Class<T> type) {
		if (type == String.class)
			return type.cast(this.val);
		return null;
	}

	@Override
	public String toString() {
		return this.val;
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		String y = null;
		if (obj instanceof String)
			y = (String) obj;
		if (y == null)
			ObjectException.Error.Incompatible.build();
		return (this.val == null) ? -1 : this.val.compareTo(y);

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.VARCHAR);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null)
				return new SqlVarchar(null, SqlObject.Type.VARCHAR);
			if (obj instanceof String)
				return new SqlVarchar((String) obj, SqlObject.Type.VARCHAR);
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

}