package org.fuchss.objectcasket.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.Configuration.Flag;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.objectcasket.port.SessionManager;
import org.fuchss.sqlconnector.SqlConnectionFactory;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.tablemodule.TableModuleFactory;
import org.fuchss.tablemodule.port.TableModuleBuilder;

public class SessionManagerImpl implements SessionManager {
	private SqlDatabaseFactory sqlDatabaseFactory;
	private SqlObjectFactory sqlObjectFactory;
	private TableModuleBuilder tableModuleBuilder;

	private Map<SqlDatabase, Set<SessionImpl>> sessionMap = new HashMap<>();
	private Map<Session, SqlDatabase> databaseMap = new HashMap<>();

	public SessionManagerImpl(SqlConnectionFactory sqlConnectionFactory, TableModuleFactory tableModuleFactory) {
		this.sqlDatabaseFactory = sqlConnectionFactory.sqlPort().sqlDatabaseFactory();
		this.sqlObjectFactory = sqlConnectionFactory.sqlPort().sqlObjectFactory();
		this.tableModuleBuilder = tableModuleFactory.modulePort().tableModuleBuilder();

	}

	@Override
	public Session session(Configuration config) throws ObjectCasketException {
		try {
			SqlDatabase database = this.sqlDatabaseFactory.openDatabase(((ConfigurationAdapter) config).getConfigruation());
			Set<SessionImpl> sessions = this.sessionMap.get(database);
			if (sessions == null)
				this.sessionMap.put(database, sessions = new HashSet<>());
			SessionImpl session = null;
			if (sessions.isEmpty() || config.containsAll(Flag.SESSIONS)) {
				sessions.add(session = new SessionImpl(this.tableModuleBuilder.tableModule(database, this.sqlObjectFactory)));
				this.databaseMap.put(session, database);
			} else
				session = sessions.isEmpty() ? null : sessions.iterator().next();
			return session;
		} catch (ConnectorException exc) {
			ObjectCasketException.build(exc);
		}
		return null;
	}

	@Override
	public void terminate(Session session) throws ObjectCasketException {
		SqlDatabase database = this.databaseMap.get(session);
		if (database == null) {
			SessionManagerException.Error.UnknownSession.build();
		}
		Set<SessionImpl> sessions = this.sessionMap.get(database);
		try {
			if (sessions.contains(session) && (sessions.size() == 1))
				this.sqlDatabaseFactory.closeDatabase(database);
		} catch (ConnectorException exc) {
			ObjectCasketException.build(exc);
		}
		sessions.remove(session);
		this.databaseMap.remove(session);
		((SessionImpl) session).terminate();
	}

	private static class SessionManagerException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private SessionManagerException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			UnknownSession("Unknown session.");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new SessionManagerException(this, arg));
			}

		}
	}

}
