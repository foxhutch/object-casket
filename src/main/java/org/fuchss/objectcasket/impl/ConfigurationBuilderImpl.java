package org.fuchss.objectcasket.impl;

import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ConfigurationBuilder;
import org.fuchss.sqlconnector.SqlConnectionFactory;
import org.fuchss.sqlconnector.port.SqlDatabaseFactory;

public class ConfigurationBuilderImpl implements ConfigurationBuilder {
	private SqlDatabaseFactory SqlDatabaseFactory;

	public ConfigurationBuilderImpl(SqlConnectionFactory sqlConnectionFactory) {
		this.SqlDatabaseFactory = sqlConnectionFactory.sqlPort().sqlDatabaseFactory();
	}

	@Override
	public Configuration createConfiguration() {
		return new ConfigurationAdapter(this.SqlDatabaseFactory.createConfiguration());
	}

}
