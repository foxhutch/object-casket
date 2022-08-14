package org.fuchss.objectcasket.sqlconnector;

import java.io.Serializable;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.database.SqlDatabaseFactoryImpl;
import org.fuchss.objectcasket.sqlconnector.impl.database.SqlObjectFatoryImpl;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

class SqlConnectorImpl implements SqlPort, SqlDatabaseFactory, SqlObjectFactory {

	private SqlDatabaseFactoryImpl sqlDatabaseFactory;
	private SqlObjectFatoryImpl sqlObjectFactory;

	@Override
	public synchronized SqlDatabaseFactory sqlDatabaseFactory() {
		this.initSqlObjectFactory();
		if (this.sqlDatabaseFactory == null) {
			this.sqlDatabaseFactory = new SqlDatabaseFactoryImpl(this.sqlObjectFactory);
		}
		return this;
	}

	@Override
	public synchronized SqlObjectFactory sqlObjectFactory() {
		this.initSqlObjectFactory();
		return this;
	}

	private void initSqlObjectFactory() {
		if (this.sqlObjectFactory == null) {
			this.sqlObjectFactory = new SqlObjectFatoryImpl();
		}
	}

	@Override
	public synchronized SqlDatabase openDatabase(DBConfiguration config) throws CasketException {
		return this.sqlDatabaseFactory.openDatabase(config);
	}

	@Override
	public synchronized void closeDatabase(SqlDatabase db) throws CasketException {
		this.sqlDatabaseFactory.closeDatabase(db);
	}

	@Override
	public synchronized DBConfiguration createConfiguration() {
		return this.sqlDatabaseFactory.createConfiguration();
	}

	@Override
	public synchronized <T extends Serializable> SqlColumnSignature mkColumnSignature(StorageClass type, Class<T> javaType, T defaultValue) throws CasketException {
		return this.sqlObjectFactory.mkColumnSignature(type, javaType, defaultValue);
	}

	@Override
	public <T extends Serializable> SqlObject mkSqlObject(StorageClass type, T obj) throws CasketException {
		return this.sqlObjectFactory.mkSqlObject(type, obj);
	}

	@Override
	public synchronized SqlObject duplicate(SqlObject sqlObj) throws CasketException {
		return this.sqlObjectFactory.duplicate(sqlObj);
	}

}
