package org.fuchss.sqlconnector.impl.object;

import java.util.HashMap;
import java.util.Map;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class SqlObjectFatoryImpl implements SqlObjectFactory {

	private static Map<SqlObject.Type, SqlObjectBuilder> typeToBuilderMap = new HashMap<>();
	static {
		typeToBuilderMap.put(SqlObject.Type.INTEGER, new SqlInteger.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.BOOL, new SqlBool.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.REAL, new SqlReal.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.DOUBLE, new SqlDouble.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.FLOAT, new SqlFloat.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.CHAR, new SqlChar.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.TEXT, new SqlText.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.VARCHAR, new SqlVarchar.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.NUMERIC, new SqlNumeric.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.DATE, new SqlDate.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.TIMESTAMP, new SqlTimestamp.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.BLOB, new SqlBlob.SqlBuilder());
		typeToBuilderMap.put(SqlObject.Type.JSON, new SqlJSON.SqlBuilder());
	}

	@Override
	public SqlObjectImpl mkSqlObject(SqlObject.Type type, Object obj) throws ConnectorException {

		return SqlObjectFatoryImpl.typeToBuilderMap.get(type).mkSqlObjectFromJava(obj);
	}

	@Override
	public SqlObjectImpl mkSqlObjectFromSQL(SqlObject.Type type, Object obj) throws ConnectorException {

		return SqlObjectFatoryImpl.typeToBuilderMap.get(type).mkSqlObjectFromSQL(obj);
	}

	@Override
	public SqlPrototype mkPrototype() {
		return new SqlPrototypeImpl();
	}

}
