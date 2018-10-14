package org.fuchss.sqlconnector.port;

public interface SqlObjectFactory {

	SqlObject mkSqlObject(SqlObject.Type type, Object obj) throws ConnectorException;

	SqlPrototype mkPrototype();
}
