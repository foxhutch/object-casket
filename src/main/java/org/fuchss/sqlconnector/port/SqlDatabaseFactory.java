package org.fuchss.sqlconnector.port;

public interface SqlDatabaseFactory {

	SqlDatabase openDatabase(Configuration config) throws ConnectorException;

	void closeDatabase(SqlDatabase db) throws ConnectorException;

	Configuration createConfiguration();

}
