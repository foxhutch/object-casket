package org.fuchss.objectcasket;

import org.fuchss.objectcasket.impl.ConfigurationBuilderImpl;
import org.fuchss.objectcasket.impl.SessionManagerImpl;
import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ConfigurationBuilder;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.ObjectCasketPort;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.port.SessionManager;
import org.fuchss.sqlconnector.SqlConnectionFactory;
import org.fuchss.tablemodule.TableModuleFactory;

class ObjectCasketFactoryImpl implements ObjectCasketFactory, ObjectCasketPort, ConfigurationBuilder, SessionManager {

	TableModuleFactory tableModuleFactory = TableModuleFactory.FACTORY;
	SqlConnectionFactory sqlConnectionFactory = SqlConnectionFactory.FACTORY;

	ConfigurationBuilderImpl configurationBuilder;

	SessionManagerImpl sessionManager;

	@Override
	public ObjectCasketPort ObjectCasketPort() {
		return this;
	}

	@Override
	public ConfigurationBuilder configurationBuilder() {
		if (this.configurationBuilder == null) {
			this.configurationBuilder = new ConfigurationBuilderImpl(this.sqlConnectionFactory);
		}
		return this;

	}

	@Override
	public SessionManager sessionManager() {
		if (this.sessionManager == null) {
			this.sessionManager = new SessionManagerImpl(this.sqlConnectionFactory, this.tableModuleFactory);
		}
		return this;
	}

	@Override
	public Session session(Configuration config) throws ObjectCasketException {
		return this.sessionManager.session(config);
	}

	@Override
	public void terminate(Session session) throws ObjectCasketException {
		this.sessionManager.terminate(session);
	}

	@Override
	public Configuration createConfiguration() {
		return this.configurationBuilder.createConfiguration();
	}
}
