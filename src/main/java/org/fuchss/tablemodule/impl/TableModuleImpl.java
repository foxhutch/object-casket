package org.fuchss.tablemodule.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.port.Table;
import org.fuchss.tablemodule.port.TableModule;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.TablePrototype;
import org.fuchss.tablemodule.port.Transaction;

public class TableModuleImpl implements TableModule {

	private SqlDatabase database;
	private SqlObjectFactory sqlObjectFactory;

	private Map<TablePrototype, TablePrototypeImpl> tablePrototypeMap = new HashMap<>();
	private Map<String, TablePrototypeImpl> tablePrototypeNameMap = new HashMap<>();

	private Map<TablePrototype, Table> tableMap = new HashMap<>();
	private Map<String, Table> tableNameMap = new HashMap<>();

	private TransactionImpl transaction;
	private Semaphore mkTransaction = new Semaphore(1);
	private Semaphore commitTransaction = new Semaphore(0);

	public TableModuleImpl(SqlDatabase database, SqlObjectFactory sqlObjectFactory) {
		this.database = database;
		this.sqlObjectFactory = sqlObjectFactory;
	}

	@Override
	public TablePrototypeImpl mkTablePrototype(String tableName) throws TableModuleException {
		if (!this.database.wellformedTableName(tableName)) {
			TableFactoryException.Error.MalformedTableName.build(tableName, Arrays.toString(this.database.acceptedCharactersForTableName()));
		}

		TablePrototypeImpl tablePrototype;
		if ((tablePrototype = this.tablePrototypeNameMap.get(tableName)) == null) {
			this.tablePrototypeNameMap.put(tableName, tablePrototype = new TablePrototypeImpl(tableName));
			this.tablePrototypeMap.put(tablePrototype, tablePrototype);
		}
		return tablePrototype;
	}

	@Override
	public Table assignOrCreateTable(TablePrototype tablePrototype, Boolean assign) throws TableModuleException {
		TablePrototypeImpl tablePrototypeImpl = this.checkPrototype(tablePrototype);

		Map<String, SqlPrototype> prototypes = null;
		int sequenzNumber = 0;
		try {
			prototypes = this.mkColumnPrototypes(tablePrototypeImpl);
			this.database.beginTransaction();
			if (assign == null) {
				assign = this.database.allTables().contains(tablePrototypeImpl.getTableName());
			}
			if (assign) {
				this.database.assignTable(tablePrototypeImpl.getTableName(), prototypes);
			} else {
				this.database.createTable(tablePrototypeImpl.getTableName(), prototypes);
			}
			sequenzNumber = this.getSequenzNumber(tablePrototypeImpl.getTableName());
			this.database.endTransaction();
		} catch (ConnectorException | SQLException e) {
			TableModuleException.build(e);
		}
		return this.mkTable(tablePrototypeImpl, prototypes, sequenzNumber);
	}

	private int getSequenzNumber(String tableName) throws ConnectorException, SQLException {
		ResultSet rs = this.database.getMaxPK(tableName, this.tablePrototypeNameMap.get(tableName).getPrimeryKey());

		try {
			while (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} finally {
			rs.close();
		}
	}

	private Map<String, SqlPrototype> mkColumnPrototypes(TablePrototypeImpl tablePrototypeImpl) throws TableModuleException {
		Map<String, SqlPrototype> prototypes = new HashMap<>();
		for (String name : tablePrototypeImpl.getColumnNames()) {
			prototypes.put(name, tablePrototypeImpl.getColumnForName(name).mkPrototype(this.sqlObjectFactory));
		}
		return prototypes;

	}

	private TableImpl mkTable(TablePrototypeImpl tablePrototypeImpl, Map<String, SqlPrototype> prototypes, Integer sequenzNumber) throws TableModuleException {
		TableImpl table = new TableImpl(this.database, this.sqlObjectFactory);
		table.setName(tablePrototypeImpl.getTableName());
		table.setProtoTypes(prototypes, tablePrototypeImpl.getPrimeryKey());
		table.setSequenzNumber(sequenzNumber);
		this.tableMap.put(tablePrototypeImpl, table);
		this.tableNameMap.put(tablePrototypeImpl.getTableName(), table);
		return table;

	}

	private TablePrototypeImpl checkPrototype(TablePrototype tablePrototype) throws TableModuleException {
		TablePrototypeImpl tablePrototypeImpl = this.tablePrototypeMap.get(tablePrototype);
		if (tablePrototypeImpl == null) {
			TableFactoryException.Error.UnknownTable.build(tablePrototype.toString());
		}
		if (tablePrototypeImpl.getPrimeryKey() == null) {
			TableFactoryException.Error.NoPrimaryKey.build();
		}
		if (this.tableMap.containsKey(tablePrototypeImpl)) {
			TableFactoryException.Error.NoModification.build();
		}
		return tablePrototypeImpl;
	}

	@Override
	public Table getTable(String tableName) throws TableModuleException {
		return this.tableNameMap.get(tableName);
	}

	@Override
	public Transaction beginTransaction() {
		try {
			this.mkTransaction.acquireUninterruptibly();
			return this.transaction = new TransactionImpl(this.database);
		} finally {
			this.commitTransaction.release();
		}

	}

	@Override
	public void commit(Transaction transaction) throws TableModuleException {
		TransactionImpl trans = this.checkTransactionForCommitOrRollback(transaction);
		try {
			if (!trans.commit()) {
				TableFactoryException.Error.CommitFailed.build(trans.getException().toString());
			}
		} finally {
			this.transaction.unlock();
			this.mkTransaction.release();
		}
	}

	@Override
	public void rollback(Transaction transaction) throws TableModuleException {
		TransactionImpl trans = null;
		try {
			trans = this.checkTransactionForCommitOrRollback(transaction);
		} catch (TableModuleException e) {
		}
		if (trans == null)
			return;
		trans.rollback();
		this.transaction.unlock();
		this.mkTransaction.release();
	}

	private TransactionImpl checkTransactionForCommitOrRollback(Transaction transaction) throws TableModuleException {
		TransactionImpl trans = (TransactionImpl) transaction;
		if (!this.commitTransaction.tryAcquire()) {
			TableFactoryException.Error.NoTransactionToCommit.build();
		}
		if (trans != this.transaction) {
			TableFactoryException.Error.WrongTransactionToCommit.build();
		}
		if (!this.transaction.lockIfExists()) {
			TableFactoryException.Error.WrongTransactionToCommit.build();
		}
		return trans;
	}

	private static class TableFactoryException extends TableModuleException {

		private static final long serialVersionUID = 1L;

		private TableFactoryException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			MalformedTableName("Non proper table name %s. expected characters are: %s"), //
			UnknownTable("Unknown table %s."), //
			NoPrimaryKey("To install a table one needs a primary key."), //
			NoModification("Modification of existing tables is not supported yet."), //
			CommitFailed("Commit failed do rollback caused by %s"), //
			WrongTransactionToCommit("Unknown or already finished transaction, commit an othert one."), //
			NoTransactionToCommit("No transaction to commit");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws TableModuleException {
				TableModuleException.build(new TableFactoryException(this, arg));
			}

		}

	}

}
