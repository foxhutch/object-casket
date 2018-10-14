package org.fuchss.sqlconnector.impl.object;

import org.fuchss.sqlconnector.port.ConnectorException;

public interface SqlObjectBuilder {
	SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException;
}
