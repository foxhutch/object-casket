package org.fuchss.sqlconnector.impl.object;

import org.fuchss.sqlconnector.port.ConnectorException;

public interface SqlObjectBuilder {
	SqlObjectImpl mkSqlObjectFromJava(Object obj) throws ConnectorException;

	SqlObjectImpl mkSqlObjectFromSQL(Object obj) throws ConnectorException;
}

abstract class SqlObjectBuilderImpl implements SqlObjectBuilder {

	protected abstract SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException;

	@Override
	public SqlObjectImpl mkSqlObjectFromJava(Object obj) throws ConnectorException {
		return this.mkSqlObject(obj);
	};

	@Override
	public SqlObjectImpl mkSqlObjectFromSQL(Object obj) throws ConnectorException {
		return this.mkSqlObject(obj);
	};

}