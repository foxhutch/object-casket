package org.fuchss.sqlconnector.port;

public interface SqlPort {

	SqlObjectFactory sqlObjectFactory();

	SqlDatabaseFactory sqlDatabaseFactory();

}
