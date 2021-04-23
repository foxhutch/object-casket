package org.fuchss.sqlconnector;

import org.fuchss.sqlconnector.impl.SqlDatabaseFactoryImpl;
import org.fuchss.sqlconnector.impl.object.SqlObjectFatoryImpl;
import org.fuchss.sqlconnector.port.Configuration;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPort;
import org.fuchss.sqlconnector.port.SqlPrototype;

class SqlConnectionFactoryImpl implements SqlConnectionFactory, SqlPort, SqlDatabaseFactory, SqlObjectFactory {

	private SqlObjectFatoryImpl sqlObjectFactory;

	private SqlDatabaseFactoryImpl sqlDatabaseFactory;

	@Override
	public SqlPort sqlPort() {
		return this;
	}

	@Override
	public SqlDatabaseFactory sqlDatabaseFactory() {
		if (this.sqlDatabaseFactory == null) {
			this.sqlDatabaseFactory = new SqlDatabaseFactoryImpl();
		}
		return this;
	}

	@Override
	public SqlObjectFactory sqlObjectFactory() {
		if (this.sqlObjectFactory == null) {
			this.sqlObjectFactory = new SqlObjectFatoryImpl();
		}
		return this;
	}

	@Override
	public SqlDatabase openDatabase(Configuration config) throws ConnectorException {
		return this.sqlDatabaseFactory.openDatabase(config);
	}

	@Override
	public void closeDatabase(SqlDatabase db) throws ConnectorException {
		this.sqlDatabaseFactory.closeDatabase(db);
	}

	@Override
	public Configuration createConfiguration() {
		return this.sqlDatabaseFactory.createConfiguration();
	}

	@Override
	public SqlPrototype mkPrototype() {
		return this.sqlObjectFactory.mkPrototype();
	}

	@Override
	public SqlObject mkSqlObject(SqlObject.Type type, Object obj) throws ConnectorException {
		return this.sqlObjectFactory.mkSqlObject(type, obj);
	}

	@Override
	public SqlObject mkSqlObjectFromSQL(SqlObject.Type type, Object obj) throws ConnectorException {
		return this.sqlObjectFactory.mkSqlObjectFromSQL(type, obj);
	}

}
