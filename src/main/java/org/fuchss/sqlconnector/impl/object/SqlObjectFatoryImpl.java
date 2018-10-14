package org.fuchss.sqlconnector.impl.object;

import java.util.HashMap;
import java.util.Map;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class SqlObjectFatoryImpl implements SqlObjectFactory {

	private static Map<SqlObject.Type, SqlObjectBuilder> typeToBuilderMap = new HashMap<SqlObject.Type, SqlObjectBuilder>() {
		private static final long serialVersionUID = 1L;
		{
			this.put(SqlObject.Type.INTEGER, new SqlInteger.SqlBuilder());
			this.put(SqlObject.Type.BOOL, new SqlBool.SqlBuilder());
			this.put(SqlObject.Type.REAL, new SqlReal.SqlBuilder());
			this.put(SqlObject.Type.DOUBLE, new SqlDouble.SqlBuilder());
			this.put(SqlObject.Type.FLOAT, new SqlFloat.SqlBuilder());
			this.put(SqlObject.Type.CHAR, new SqlChar.SqlBuilder());
			this.put(SqlObject.Type.TEXT, new SqlText.SqlBuilder());
			this.put(SqlObject.Type.VARCHAR, new SqlVarchar.SqlBuilder());
			this.put(SqlObject.Type.NUMERIC, new SqlNumeric.SqlBuilder());
			this.put(SqlObject.Type.DATE, new SqlDate.SqlBuilder());
			this.put(SqlObject.Type.TIMESTAMP, new SqlTimestamp.SqlBuilder());

		}
	};

	@Override
	public SqlObjectImpl mkSqlObject(SqlObject.Type type, Object obj) throws ConnectorException {

		return SqlObjectFatoryImpl.typeToBuilderMap.get(type).mkSqlObject(obj);
	}

	@Override
	public SqlPrototype mkPrototype() {
		return new SqlPrototypeImpl();
	}

}
