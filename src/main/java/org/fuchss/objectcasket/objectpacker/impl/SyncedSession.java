package org.fuchss.objectcasket.objectpacker.impl;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionObserver;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Semaphore;

abstract class SyncedSession extends CoreSession implements Runnable {

	private Thread thread;
	private final Semaphore needSync = new Semaphore(0); // only for signal

	private final Set<SessionObserver> observers = new HashSet<>();

	private final Set<Object> changedObjects = new HashSet<>();
	private final Set<Object> deletedObjects = new HashSet<>();
	private final Map<JoinTableBuilder<?, ?>, SuppliersLoadInfo> suppliersToLoad = new HashMap<>();

	protected SyncedSession(TableModule tabMod, ConfigurationImpl config) {
		super(tabMod, config);
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public synchronized boolean register(SessionObserver observer) {
		return this.observers.add(observer);
	}

	@Override
	public synchronized boolean deregister(SessionObserver observer) {
		return this.observers.remove(observer);
	}

	@Override
	public void run() {
		Thread mySelf = Thread.currentThread();
		try {
			while (mySelf == this.thread) {
				this.needSync.acquire();
				this.doUpdate();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void doUpdate() {
		try {
			this.beginTransaction();
			this.update();
			this.endTransaction();
		} catch (CasketException e) {
			// NOP
		}
	}

	private synchronized void update() throws CasketException {

		Map<JoinTableBuilder<?, ?>, SuppliersLoadInfo> loadMap = new HashMap<>(this.suppliersToLoad);
		for (Entry<JoinTableBuilder<?, ?>, SuppliersLoadInfo> entry : loadMap.entrySet()) {
			JoinTableBuilder<?, ?> jtBuilder = entry.getKey();
			Class<?> supplierClass = jtBuilder.getSupplierClass();

			SuppliersLoadInfo loadInfo = entry.getValue();

			Map<Serializable, Object> supplierMap = new HashMap<>();
			for (Serializable pk : loadInfo.pks()) {
				Set<Session.Exp> arg = new HashSet<>();
				arg.add(new Session.Exp(loadInfo.pkName(), "==", pk));
				Object supplier = this.getObjects(supplierClass, arg).iterator().next();
				supplierMap.put(pk, supplier);
			}
			jtBuilder.insertNewSuppliers(supplierMap);
		}

		Set<Object> objectsToDelete = new HashSet<>(this.deletedObjects);
		for (Object obj : objectsToDelete) {
			this.observers.forEach(obs -> obs.externDeleted(obj));
			this.deleteObject(obj);
		}
		Set<Object> objectsToSync = new HashSet<>(this.changedObjects);
		for (Object obj : objectsToSync) {
			this.syncObject(obj);
			this.observers.forEach(obs -> obs.externChanged(obj));
		}
		if (this.deletedObjects.isEmpty() && this.changedObjects.isEmpty())
			return;
		this.needSync.release();
	}

	private void deleteObject(Object obj) throws CasketException {
		this.deletedObjects.remove(obj);
		this.changedObjects.remove(obj);
		this.deleteByUpdate(obj);
	}

	private void syncObject(Object obj) throws CasketException {
		this.resync(obj);
		this.changedObjects.remove(obj);
	}

	synchronized void addToChanged(Object obj) {
		if (this.ignore)
			return;
		this.changedObjects.add(obj);
	}

	synchronized void addToDeleted(Object obj) {
		if (this.ignore)
			return;
		this.deletedObjects.add(obj);
	}

	synchronized <C, S> void loadNewAssignedObjects(JoinTableBuilder<C, S> joinTableBuilder, Set<Serializable> suppliersToLoad, String pkName) {
		if (this.ignore)
			return;
		this.suppliersToLoad.put(joinTableBuilder, new SuppliersLoadInfo(suppliersToLoad, pkName));
	}

	synchronized void updateDone() {
		if (this.ignore)
			return;
		if ((this.deletedObjects.isEmpty() && this.changedObjects.isEmpty() && this.suppliersToLoad.isEmpty()) || (this.needSync.availablePermits() > 0))
			return;
		this.needSync.release();
	}

	void halt() throws InterruptedException {
		Thread theThread = this.thread;
		if (theThread == null)
			return;
		this.thread = null;
		theThread.interrupt();
		theThread.join();
	}

	record SuppliersLoadInfo(Set<Serializable> pks, String pkName) {
	}

}