package org.fuchss.sqlconnector.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.fuchss.sqlconnector.port.Configuration;
import org.fuchss.sqlconnector.port.Configuration.Flag;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlDatabaseFactory;

public class SqlDatabaseFactoryImpl implements SqlDatabaseFactory {

	private Map<Configuration, ConfigurationImpl> configMap = new HashMap<>();

	private Map<SqlDatabase, SqlDatabaseImpl> dbMap = new HashMap<>(); // x
	private Map<ConfigurationImpl, SqlDatabaseImpl> configDbMap = new HashMap<>();
	private Map<SqlDatabaseImpl, ConfigurationImpl> dbConfigMap = new HashMap<>(); // x

	private Map<ConfigurationImpl, Connection> connectionMap = new HashMap<>();

	@Override
	public SqlDatabase openDatabase(Configuration config) throws ConnectorException {
		SqlDatabaseImpl db = null;
		ConfigurationImpl configImpl = this.checkConfiguration(config);
		if (configImpl.isInUse())
			db = this.configDbMap.get(configImpl);
		else {
			Connection connection = this.mkConnection(configImpl);
			db = new SqlDatabaseImpl(connection);
			this.dbMap.put(db, db);
			this.connectionMap.put(configImpl, connection);
			this.dbConfigMap.put(db, configImpl);
			this.configDbMap.put(configImpl, db);
			configImpl.setInUse(true);
		}
		return db;
	}

	private ConfigurationImpl checkConfiguration(Configuration config) throws ConnectorException {
		ConfigurationImpl configImpl = this.configMap.get(config);
		if (configImpl == null) {
			SqlDatabaseFactoryException.Error.UnknownConfiguration.build(config.toString());
		}
		return configImpl;
	}

	private Connection mkConnection(ConfigurationImpl config) throws ConnectorException {
		this.buildFileIfNecessary(config);
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(config.getURL(), config.getUser(), config.getPasswd());
			connection.setAutoCommit(false);
			connection.setReadOnly(!config.allows(Flag.MODIFY));
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return connection;
	}

	@Override
	public Configuration createConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl();
		this.configMap.put(config, config);
		return config;
	}

	@Override
	public void closeDatabase(SqlDatabase db) throws ConnectorException {
		SqlDatabaseImpl dbImpl = this.dbMap.get(db);
		Objects.requireNonNull(dbImpl);
		ConfigurationImpl config = this.dbConfigMap.get(dbImpl);
		this.closeConnection(config);
		this.configDbMap.remove(config);
		this.dbMap.remove(dbImpl);
		this.dbConfigMap.remove(dbImpl);
		config.setInUse(false);
	}

	private void closeConnection(ConfigurationImpl config) throws ConnectorException {
		try {
			Connection connection = this.connectionMap.get(config);
			connection.close();
			this.connectionMap.remove(config);
		} catch (Exception exc) {
			ConnectorException.build(exc);
		}
	}

	private void buildFileIfNecessary(ConfigurationImpl config) throws ConnectorException {
		File file = new File(config.getURI());
		if (file.exists() && file.isFile()) {
			return;
		}
		if (file.exists()) {
			SqlDatabaseFactoryException.Error.UnknownURI.build(config.getURI());
		}
		if (config.allows(Configuration.Flag.CREATE)) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException exc) {
				ConnectorException.build(exc);
			}
		}
	}

	private static class SqlDatabaseFactoryException extends ConnectorException {

		private static final long serialVersionUID = 1L;

		private SqlDatabaseFactoryException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			UnknownConfiguration("Unknown configuration %s use createConfiguration, to create one."), //
			UnknownURI("The URI %s does not specify a normal file  Unknown FileTypeconfiguration %s use createConfiguration, to create one.");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ConnectorException {
				ConnectorException.build(new SqlDatabaseFactoryException(this, arg));
			}

		}
	}

}
