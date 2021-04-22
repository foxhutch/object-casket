package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlBool extends SqlObjectImpl {

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		CAST.put(Boolean.class, n -> n == null ? null : ((Boolean) n));
		CAST.put(Integer.class, n -> n == null ? null : ((Boolean) n) ? 1 : 0);
		CAST.put(Boolean.TYPE, n -> (n == null) ? false : ((Boolean) n));
		CAST.put(Integer.TYPE, n -> (n == null) ? false : ((Boolean) n) ? 1 : 0);
	}

	protected Boolean val;

	SqlBool(Boolean obj) throws ConnectorException {
		super(SqlObject.Type.BOOL);
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
		return (this.val == null) ? null : (this.val ? "1" : "0");
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Boolean y = null;
		if (obj instanceof Integer)
			y = !(((Integer) obj) == 0);
		if (obj instanceof Boolean)
			y = (Boolean) obj;
		if (y == null)
			ObjectException.Error.Incompatible.build();
		return (this.val == null) ? -1 : this.val.compareTo(y);
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null)
				return new SqlBool(null);
			if (obj instanceof Integer)
				return new SqlBool(!(((Integer) obj) == 0));
			if (obj instanceof Boolean)
				return new SqlBool((Boolean) obj);
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.BOOLEAN);
			} else
				preparedStatement.setObject(pos, this.val, java.sql.Types.BOOLEAN);
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}
