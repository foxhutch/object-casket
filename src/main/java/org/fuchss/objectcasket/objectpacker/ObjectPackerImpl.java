package org.fuchss.objectcasket.objectpacker;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.impl.SessionManagerImpl;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.tablemodule.ModulePort;

class ObjectPackerImpl implements PackerPort, SessionManager {

	private final ModulePort modulePort = ModulePort.PORT;

	private SessionManagerImpl sessionManager;

	@Override
	public synchronized SessionManager sessionManager() {
		if (this.sessionManager == null)
			this.sessionManager = new SessionManagerImpl(this.modulePort.tableModuleFactory());
		return this;
	}

	@Override
	public synchronized Session session(Configuration config) throws CasketException {
		return this.sessionManager.session(config);
	}

	@Override
	public synchronized void terminate(Session session) throws CasketException, InterruptedException {

		this.sessionManager.terminate(session);

	}

	@Override
	public synchronized Configuration createConfiguration() {
		return this.sessionManager.createConfiguration();
	}

	@Override
	public synchronized void terminateAll(Configuration config) throws CasketException, InterruptedException {

		this.sessionManager.terminateAll(config);

	}

	@Override
	public synchronized void terminateAll() throws CasketException, InterruptedException {

		this.sessionManager.terminateAll();

	}

	@Override
	public synchronized Domain mkDomain(Configuration config) throws CasketException {

		return this.sessionManager.mkDomain(config);

	}

	@Override
	public synchronized Domain editDomain(Configuration config) throws CasketException {

		return this.sessionManager.editDomain(config);

	}

	@Override
	public synchronized void addEntity(Domain dom, Class<?>... clazz) throws CasketException, InterruptedException {

		this.sessionManager.addEntity(dom, clazz);

	}

	@Override
	public synchronized void finalizeDomain(Domain dom) throws CasketException, InterruptedException {

		this.sessionManager.finalizeDomain(dom);

	}
}
