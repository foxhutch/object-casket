package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlChar extends SqlObjectImpl {

	private static final Map<Class<?>, Function<Object, Object>> CAST = new HashMap<>();
	static {
		SqlChar.CAST.put(Character.class, n -> n);
		SqlChar.CAST.put(String.class, n -> n == null ? null : n.toString());
		SqlChar.CAST.put(Character.TYPE, n -> (n == null) ? ((char) 0) : n);
	}

	protected Character val;

	SqlChar(Character obj) throws ConnectorException {
		super(SqlObject.Type.CHAR);
		this.val = obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type, Field target) {
		Function<Object, Object> cast = SqlChar.CAST.get(type);
		if (cast == null) {
			return null;
		}
		return (T) SqlChar.CAST.get(type).apply(this.val);
	}

	@Override
	public String toString() {
		return (this.val == null) ? null : ("" + this.val);
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {
		Character y = null;
		if (obj instanceof String) {
			y = ((String) obj).charAt(0);
		}
		if (obj instanceof Character) {
			y = (Character) obj;
		}
		if (y == null) {
			ObjectException.Error.Incompatible.build();
		}
		return (this.val == null) ? -1 : this.val.compareTo(y);
	}

	static class SqlBuilder extends SqlObjectBuilderImpl {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlChar(null);
			}
			if (obj instanceof String) {
				return new SqlChar(((String) obj).charAt(0));
			}
			if (obj instanceof Character) {
				return new SqlChar((Character) obj);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.CHAR);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.CHAR);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}