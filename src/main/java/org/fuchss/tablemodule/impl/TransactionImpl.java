package org.fuchss.tablemodule.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.tablemodule.port.Transaction;

public class TransactionImpl implements Transaction {

	private SqlDatabase database;
	private boolean exist = true;

	private Semaphore lock = new Semaphore(1);

	private Exception exception;

	private List<RowImpl> writeList = new ArrayList<>();
	private List<RowImpl> createList = new ArrayList<>();
	private List<RowImpl> deleteList = new ArrayList<>();
	private Map<RowImpl, TableImpl> writeListMap = new HashMap<>();
	private Map<RowImpl, TableImpl> createListMap = new HashMap<>();
	private Map<RowImpl, TableImpl> deleteListMap = new HashMap<>();

	public TransactionImpl(SqlDatabase database) {
		this.database = database;
	}

	public Exception getException() {
		return this.exception;
	}

	public boolean lockIfExists() {
		boolean result;
		this.lock.acquireUninterruptibly();
		if (!(result = this.exist)) {
			this.lock.release();
		}
		return result;
	}

	public void unlock() {
		this.lock.tryAcquire();
		this.lock.release();
	}

	public void addToWriteSet(RowImpl row, TableImpl table) {
		if (this.createListMap.containsKey(row)) {
			return;
		}
		if (this.writeListMap.put(row, table) == null) {
			this.writeList.add(row);
		}
	}

	public void addToCreateSet(RowImpl row, TableImpl table) {
		if (this.createListMap.put(row, table) == null) {
			this.createList.add(row);
		}
	}

	public void addToDeleteSet(RowImpl row, TableImpl table) {
		if (this.deleteListMap.put(row, table) == null) {
			this.deleteList.add(row);
		}
	}

	public boolean commit() {
		try {
			this.database.beginTransaction();
			this.saveOrUpdate();
			this.delete();
			this.applyChanges();
			this.database.endTransaction();
			return true;
		} catch (ConnectorException | SQLException exc) {
			this.exception = exc;
			try {
				this.database.rollback();
			} catch (ConnectorException e) {
			} finally {
				this.rollback();
			}
		}
		return false;
	}

	private void saveOrUpdate() throws ConnectorException, SQLException {
		for (RowImpl row : this.createList) {
			if (this.deleteListMap.containsKey(row)) {
				continue;
			}
			this.createListMap.get(row).saveOrUpdate(row);
		}
		for (RowImpl row : this.writeList) {
			if (this.deleteListMap.containsKey(row)) {
				continue;
			}
			this.writeListMap.get(row).saveOrUpdate(row);
		}
	}

	private void delete() throws ConnectorException {
		for (RowImpl row : this.deleteList) {
			this.deleteListMap.get(row).remove(row);
		}

	}

	private void applyChanges() {
		for (RowImpl row : this.createList) {
			if (this.deleteListMap.containsKey(row)) {
				continue;
			}
			row.applyCreate();
		}
		for (RowImpl row : this.writeList) {
			if (this.deleteListMap.containsKey(row)) {
				continue;
			}
			row.applyWrite();
		}
		for (RowImpl row : this.deleteList) {
			row.applyDelete();
		}
	}

	public void rollback() {
		for (RowImpl row : this.createList) {
			row.rollbackCreate();
		}
		for (RowImpl row : this.writeList) {
			row.rollback();
		}
		for (RowImpl row : this.deleteList) {
			row.rollback();
		}
	}

}
