package org.fuchss.objectcasket.objectpacker.impl;

import org.fuchss.objectcasket.common.*;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

import java.util.Set;

abstract class CoreSession implements Session {
	protected IntolerantMap<Class<?>, ObjectBuilder<?>> objectFactoryMap = new IntolerantHashMap<>();

	protected TableModule tableModule;
	protected ConfigurationImpl config;
	protected Object transaction;
	protected boolean ignore = false;

	protected CoreSession(TableModule tabMod, ConfigurationImpl config) {
		this.tableModule = tabMod;
		this.config = config;
	}

	@Override
	public void beginTransaction() throws CasketException {
		if (this.transaction != null)
			throw CasketError.TRANSACTION_RUNNING.build();
		this.transaction = this.tableModule.beginTransaction();
	}

	@Override
	public synchronized void endTransaction() throws CasketException {
		if (this.transaction == null)
			throw CasketError.MISSING_TRANSACTION.build();
		try {
			this.ignore = true;
			this.tableModule.endTransaction(this.transaction);
		} finally {
			this.transaction = null;
			this.ignore = false;
		}
	}

	@Override
	public synchronized <T> Set<T> getAllObjects(Class<T> clazz) throws CasketException {
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(clazz);
		try {
			boolean local = this.localTransaction();
			Set<T> resultSet = objFactory.getAllObjects(this.transaction);
			if (local)
				this.endTransaction();
			return resultSet;
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized <T> Set<T> getObjects(Class<T> clazz, Set<Session.Exp> args) throws CasketException {
		Util.objectsNotNull(args);
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(clazz);
		try {
			boolean local = this.localTransaction();
			Set<T> resultSet = objFactory.getObjects(args, this.transaction);
			if (local)
				this.endTransaction();
			return resultSet;
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized <T> void persist(T obj) throws CasketException {
		Util.objectsNotNull(obj);
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(obj.getClass());
		try {
			boolean local = this.localTransaction();
			objFactory.persist(obj, this.transaction);
			if (local)
				this.endTransaction();
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized <T> void delete(T obj) throws CasketException {
		Util.objectsNotNull(obj);
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(obj.getClass());
		if (objFactory.hasClients(obj) || objFactory.isClient(obj))
			throw CasketError.OBJECT_IN_USE.build();
		try {
			boolean local = this.localTransaction();
			objFactory.delete(obj, this.transaction);
			if (local)
				this.endTransaction();
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	protected synchronized <T> void deleteByUpdate(T obj) throws CasketException {
		Util.objectsNotNull(obj);
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(obj.getClass());
		objFactory.deleteByUpdate(obj);
	}

	@Override
	public synchronized void resync() throws CasketException {
		try {
			boolean local = this.localTransaction();
			for (ObjectBuilder<?> objFac : this.objectFactoryMap.values())
				objFac.resync(this.transaction);
			if (local)
				this.endTransaction();
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized <T> void resync(T obj) throws CasketException {
		Util.objectsNotNull(obj);
		@SuppressWarnings("unchecked") ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(obj.getClass());
		try {
			boolean local = this.localTransaction();
			objFactory.resync(obj, this.transaction);
			if (local)
				this.endTransaction();
		} catch (Exception exc) {
			this.rollback();
			throw CasketException.build(exc);
		}
	}

	private boolean localTransaction() throws CasketException {
		boolean local = this.transaction == null;
		if (local)
			this.beginTransaction();
		return local;
	}

	private void rollback() {
		try {
			this.tableModule.rollback(this.transaction);
		} catch (Exception exc) {
			// NOP
		}
		this.transaction = null;
	}

}