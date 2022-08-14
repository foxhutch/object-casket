package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;

/**
 * The implementation of the {@link SqlDatabaseFactory}.
 *
 */
public class SqlDatabaseFactoryImpl implements SqlDatabaseFactory {

	private Map<SqlDatabase, SqlDatabaseImpl> dbMap = new HashMap<>();

	private Map<ConfigurationImpl, SqlDatabaseImpl> configDbMap = new HashMap<>();
	private Map<SqlDatabaseImpl, ConfigurationImpl> dbConfigMap = new HashMap<>(); // x

	private SqlObjectFatoryImpl sqlObjectFactory;

	/**
	 * The constructor.
	 *
	 * @param sqlObjectFactory
	 *            - the {@link SqlObjectFatoryImpl object factory} to use.
	 *
	 */
	public SqlDatabaseFactoryImpl(SqlObjectFatoryImpl sqlObjectFactory) {
		this.sqlObjectFactory = sqlObjectFactory;
	}

	@Override
	public DBConfiguration createConfiguration() {
		return new ConfigurationImpl();
	}

	@Override
	public SqlDatabase openDatabase(DBConfiguration config) throws CasketException {
		SqlDatabaseImpl db = null;
		ConfigurationImpl configImpl = this.getConfig(config);
		if (!configImpl.isComplete())
			throw CasketError.INCOMPLETE_CONFIGURATION.build();
		if (configImpl.isInUse())
			db = this.configDbMap.get(configImpl);
		else {
			db = new SqlDatabaseImpl(this.mkConnection(configImpl), configImpl.allows(DBConfiguration.Flag.CREATE), this.sqlObjectFactory, configImpl.getSqlCmd());
			this.dbMap.put(db, db);
			this.dbConfigMap.put(db, configImpl);
			this.configDbMap.put(configImpl, db);
			configImpl.setInUse(true);
		}
		return db;
	}

	@Override
	public void closeDatabase(SqlDatabase db) throws CasketException {
		SqlDatabaseImpl dbImpl = this.dbMap.get(db);
		if (dbImpl == null)
			throw CasketError.UNKNOWN_DATABASE.build();
		dbImpl.close();
		ConfigurationImpl configImpl = this.dbConfigMap.get(dbImpl);
		this.dbMap.remove(db, dbImpl);
		this.dbConfigMap.remove(dbImpl, configImpl);
		this.configDbMap.remove(configImpl, dbImpl);
		configImpl.setInUse(false);
	}

	@SuppressWarnings("java:S2095")
	private Connection mkConnection(ConfigurationImpl config) throws CasketException {
		this.buildFileIfNecessary(config);
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(config.getURL(), config.getUser(), config.getPasswd()); // closed by the database object
			connection.setAutoCommit(false);
			connection.setReadOnly(!config.allows(DBConfiguration.Flag.MODIFY));
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
		return connection;
	}

	private void buildFileIfNecessary(ConfigurationImpl config) throws CasketException {
		File file = new File(config.getURI());
		if (this.fileExists(file))
			return;
		if (config.allows(DBConfiguration.Flag.CREATE)) {
			try {
				file.getParentFile().mkdirs();
				if (!file.createNewFile())
					throw CasketError.CREATION_OR_MODIFICATION_FAILED.build();
			} catch (IOException exc) {
				throw CasketException.build(exc);
			}
		}
	}

	private boolean fileExists(File file) throws CasketException {
		if (file.exists() && file.isFile()) {
			return true;
		}
		if (file.exists()) {
			throw CasketError.UNKNOWN_URI.build();
		}
		return false;
	}

	private ConfigurationImpl getConfig(DBConfiguration config) throws CasketException {
		if (config instanceof ConfigurationImpl configImpl)
			return configImpl;
		throw CasketError.UNKNOWN_CONFIGURATION.build();
	}

}
