package org.fuchss.sqlconnector.port;

import org.fuchss.sqlconnector.port.SqlObject.Type;

public interface SqlObjectFactory {

	SqlPrototype mkPrototype();

	SqlObject mkSqlObject(SqlObject.Type type, Object obj) throws ConnectorException;

	SqlObject mkSqlObjectFromSQL(Type type, Object obj) throws ConnectorException;

}
