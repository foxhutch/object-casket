package org.fuchss.objectcasket.objectpacker.impl;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.IntolerantHashMap;
import org.fuchss.objectcasket.common.IntolerantMap;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;

/**
 * The implementation of the {@link SessionManager}.
 *
 *
 */
public class SessionManagerImpl implements SessionManager {

	private TableModuleFactory modFac;

	private IntolerantMap<ConfigurationImpl, Set<SessionImpl>> sessionMap = new IntolerantHashMap<>();

	private IntolerantMap<DomainImpl, ConfigurationImpl> domainConfigMap = new IntolerantHashMap<>();

	private IntolerantMap<DomainImpl, SessionImpl> domainBuilderMap = new IntolerantHashMap<>();

	/**
	 * The constructor.
	 *
	 * @param tableModuleFactory
	 *            - the assigned {@link TableModuleFactory}.
	 */
	public SessionManagerImpl(TableModuleFactory tableModuleFactory) {
		this.modFac = tableModuleFactory;
	}

	@Override
	public Configuration createConfiguration() {
		ConfigurationImpl config = new ConfigurationImpl(this.modFac.createConfiguration());
		this.sessionMap.put(config, new HashSet<>());
		return config;
	}

	@Override
	public Domain mkDomain(Configuration config) throws CasketException {
		return this.mkOrEditDomain(false, config);
	}

	@Override
	public Domain editDomain(Configuration config) throws CasketException {
		return this.mkOrEditDomain(true, config);
	}

	private Domain mkOrEditDomain(boolean edit, Configuration config) throws CasketException {
		ConfigurationImpl configImpl = this.sessionMap.keyExists(config);
		if (!this.sessionMap.getIfExists(configImpl).isEmpty() || this.domainConfigMap.containsValue(configImpl)) {
			throw CasketError.OTHER_SESSION_EXISTS.build();
		}
		SessionImpl domainBuilder = (edit) //
				? SessionImpl.editDomainBuilder(this.modFac, configImpl) //
				: SessionImpl.mkDomainBuilder(this.modFac, configImpl);
		DomainImpl dom = new DomainImpl();
		this.domainBuilderMap.putIfNew(dom, domainBuilder);
		this.domainConfigMap.putIfNew(dom, configImpl);
		return dom;
	}

	@Override
	public void addEntity(Domain dom, Class<?>... clazz) throws CasketException, InterruptedException {
		SessionImpl domainBuilder = this.domainBuilderMap.getIfExists(dom);
		try {
			domainBuilder.declareClass(clazz);
		} catch (Exception exc) {
			this.finalizeDomain(dom);
			throw CasketException.build(exc);
		}
	}

	@Override
	public void finalizeDomain(Domain domain) throws CasketException, InterruptedException {
		SessionImpl domainBuilder = this.domainBuilderMap.getIfExists(domain);
		this.modFac.closeModule(domainBuilder.tableModule);
		this.domainBuilderMap.remove(domain);
		this.domainConfigMap.remove(domain);
		domainBuilder.halt();

	}

	@Override
	public Session session(Configuration config) throws CasketException {
		Set<SessionImpl> sessions = this.sessionMap.getIfExists(config);
		if (this.domainConfigMap.containsValue(config)) {
			throw CasketError.DOMAIN_BUILDING_IN_PROGRESS.build();
		}
		if (sessions.isEmpty() || config.containsAll(Configuration.Flag.SESSIONS)) {
			SessionImpl session = SessionImpl.createSession(this.modFac, (ConfigurationImpl) config);
			sessions.add(session);
			return session;
		}
		return sessions.iterator().next();
	}

	@Override
	public void terminate(Session session) throws CasketException, InterruptedException {
		SessionImpl sessionImpl = this.getSession(session);
		Set<SessionImpl> managedSessionsByConfig = this.sessionMap.getIfExists(sessionImpl.config);
		this.modFac.closeModule(sessionImpl.tableModule);
		managedSessionsByConfig.remove(sessionImpl);
		sessionImpl.halt();
	}

	@Override
	public void terminateAll(Configuration config) throws CasketException, InterruptedException {
		Set<SessionImpl> sessions = this.sessionMap.getIfExists(config);
		for (SessionImpl session : sessions)
			session.halt();
		this.modFac.closeAllModules(((ConfigurationImpl) config).getConfig());
		sessions.clear();
	}

	@Override
	public void terminateAll() throws CasketException, InterruptedException {
		for (ConfigurationImpl config : this.sessionMap.keySet())
			this.terminateAll(config);
	}

	private SessionImpl getSession(Session session) throws CasketException {
		if ((session instanceof SessionImpl sessionImpl) && this.sessionMap.getIfExists(sessionImpl.config).contains(session))
			return sessionImpl;
		throw CasketError.UNKNOWN_SESSION.build();
	}

	private static class DomainImpl implements Domain {
	}

}
