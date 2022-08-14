package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.DatabaseObserver;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;

abstract class AcidDatabase implements SqlDatabase {

	protected Connection connection;
	protected SqlObjectFatoryImpl objectFactory;
	protected SqlCmd sqlCmd;

	protected TransactionImpl transaction;
	protected Object voucher;

	private Semaphore beginTransaction = new Semaphore(1);
	private Semaphore endTransaction = new Semaphore(0);

	protected Map<TableAssignment, TableAssignmentImpl> assignedTables = new HashMap<>();

	protected Set<DatabaseObserver> allObs = new HashSet<>();
	protected Map<DatabaseObserver, Set<TableAssignmentImpl>> observedTables = new HashMap<>();

	protected abstract SqlDatabase getSqlDatabase();

	protected AcidDatabase(Connection connection, SqlObjectFatoryImpl objectFactory, SqlCmd sqlCmd) {
		this.connection = connection;
		this.objectFactory = objectFactory;
		this.sqlCmd = sqlCmd;
	}

	protected final void checkVoucherAndAcquier(Object obj) throws CasketException {
		this.transactionAcquire();
		if ((this.voucher != obj) || !obj.getClass().getName().startsWith(AcidDatabase.class.getName() + "$")) {
			this.endTransaction.release();
			throw CasketError.WRONG_TRANSACTION.build();
		}
	}

	protected void transactionAcquire() throws CasketException {
		if (!this.endTransaction.tryAcquire())
			throw CasketError.MISSING_TRANSACTION.build();
	}

	protected void preventTransaction() throws CasketException {
		if (!this.beginTransaction.tryAcquire())
			throw CasketError.TRANSACTION_RUNNING.build();
	}

	protected void permitTransaction() {
		this.beginTransaction.release();
	}

	protected void proceedTransaction() {
		this.endTransaction.release();
	}

	@Override
	public Object beginTransaction(boolean shouldWait) {
		if (shouldWait)
			this.beginTransaction.acquireUninterruptibly();
		else if (!this.beginTransaction.tryAcquire())
			return null;
		assert (this.transaction == null);
		assert (this.voucher == null);

		this.transaction = new TransactionImpl();
		this.voucher = new Object() {
		};
		this.endTransaction.release();
		return this.voucher;

	}

	@Override
	public void endTransaction(Object obj) throws CasketException {
		this.checkVoucherAndAcquier(obj);

		try {
			this.connection.commit();
			this.inform();
		} catch (SQLException e1) {
			try {
				this.connection.rollback();
				throw CasketException.build(e1);
			} catch (SQLException e2) {
				throw CasketException.build(e2);
			}
		} finally {
			this.voucher = null;
			this.transaction = null;
			this.beginTransaction.release();
		}

	}

	@Override
	public void rollback(Object obj) throws CasketException {
		this.transactionAcquire();
		if (this.voucher != obj) {
			this.endTransaction.release();
			throw CasketError.WRONG_TRANSACTION.build();
		}
		this.voucher = null;
		this.transaction = null;
		try {
			this.connection.rollback();
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.beginTransaction.release();
		}
	}

	protected void close() throws CasketException {
		if (!this.beginTransaction.tryAcquire()) {
			throw CasketError.TRANSACTION_RUNNING.build();
		}
		try {
			this.connection.close();
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}

		this.allObs.clear();
		this.assignedTables.clear();
		this.observedTables.clear();
	}

	private synchronized void inform() {
		Map<TableAssignmentImpl, Set<SqlObject>> deleted = new HashMap<>();
		Map<TableAssignmentImpl, Set<SqlObject>> changed = new HashMap<>();
		Map<TableAssignmentImpl, Set<SqlObject>> created = new HashMap<>();

		for (TableAssignmentImpl tabAssignment : this.assignedTables.values()) {
			deleted.put(tabAssignment, this.transaction.getDeletedPKs(tabAssignment));
			created.put(tabAssignment, this.transaction.getCreatedPKs(tabAssignment));
			changed.put(tabAssignment, this.transaction.getChangedPKs(tabAssignment));
		}

		for (DatabaseObserver obs : this.allObs) {
			for (TableAssignmentImpl tabAssignment : this.observedTables.get(obs)) {
				obs.update(tabAssignment, new ArrayList<>(changed.get(tabAssignment)), new ArrayList<>(deleted.get(tabAssignment)), new ArrayList<>(created.get(tabAssignment)));
			}
		}
	}

	@Override
	public synchronized void attach(DatabaseObserver obs, TableAssignment tabAssignment) throws CasketException {
		Util.objectsNotNull(obs, tabAssignment);
		this.allObs.add(obs);
		TableAssignmentImpl tabAssignImpl = this.assignedTables.get(tabAssignment);
		if (tabAssignImpl == null)
			throw CasketError.UNKNOWN_ASSIGNMENT.build();

		Set<TableAssignmentImpl> assignments = this.observedTables.computeIfAbsent(obs, k -> new HashSet<>());
		assignments.add(tabAssignImpl);
	}

	@Override
	public synchronized void detach(DatabaseObserver obs, TableAssignment tabAssignment) {
		Util.objectsNotNull(obs, tabAssignment);
		TableAssignmentImpl tabAssignImpl = this.assignedTables.get(tabAssignment);
		if ((tabAssignImpl == null) || !this.allObs.contains(obs))
			return;
		Set<TableAssignmentImpl> assignments = this.observedTables.get(obs);
		assignments.remove(tabAssignment);
		if (assignments.isEmpty()) {
			this.observedTables.remove(obs);
			this.allObs.remove(obs);
		}
	}

}
