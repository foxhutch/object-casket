package org.fuchss.tablemodule.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.Transaction;

public class RowImpl implements Row {

	private TableImpl table;

	private Map<String, SqlObject> valueMap = new HashMap<>();
	private Map<String, SqlObject> transactionalValueMap = new HashMap<>();

	private SqlObject pk;
	private String pkName;
	private boolean deleted = false;

	RowImpl(TableImpl table, String pkName) {
		this.table = table;
		this.pkName = pkName;
	}

	void setDeleted(boolean del) {
		this.deleted = del;
		this.valueMap = null;
		this.transactionalValueMap = null;
	}

	@Override
	public void write(Transaction transaction, String column, Object value) throws TableModuleException {
		this.checkTransactionAndLockIfPossible(transaction);
		try {
			this.set(column, value);
			((TransactionImpl) transaction).addToWriteSet(this, this.table);
		} finally {
			((TransactionImpl) transaction).unlock();
		}
	}

	@Override
	public <T> T read(Transaction transaction, String column, Class<T> type, Field target) throws TableModuleException {
		this.checkTransactionAndLockIfPossible(transaction);
		try {
			return this.get(column, type, target);
		} finally {
			((TransactionImpl) transaction).unlock();
		}
	}

	@Override
	public <T> T getPK(Transaction transaction, Class<T> type) throws TableModuleException {
		this.checkTransactionAndLockIfPossible(transaction);
		try {
			return this.getPK(type);
		} finally {
			((TransactionImpl) transaction).unlock();
		}
	}

	void set(String column, Object obj) throws TableModuleException {
		if (this.transactionalValueMap == null) {
			RowException.Error.RowDeleted.build();
		}
		if ((column == this.pkName) && (this.pk != null)) {
			RowException.Error.DoNotChangePK.build();
		}
		this.transactionalValueMap.put(column, this.table.createSqlObject(column, obj, false));
	}

	private <T> T getPK(Class<T> type) throws TableModuleException {
		String column = this.table.getPkName();
		return this.get(column, type, null);
	}

	private <T> T get(String column, Class<T> type, Field target) throws TableModuleException {
		SqlObject obj = this.getObject(column);
		try {
			if (obj != null) {
				return obj.get(type, target);
			}
		} catch (Exception e) {
			TableModuleException.build(e);
		}
		return null;
	}

	void prepareDelet() {
		this.transactionalValueMap = null;
	}

	SqlObject getObject(String column) throws TableModuleException {
		if (this.transactionalValueMap == null) {
			RowException.Error.RowDeleted.build();
		}
		SqlObject obj = (obj = this.transactionalValueMap.get(column)) != null ? obj : this.valueMap.get(column);
		return obj;
	}

	Map<String, SqlObject> newValues() {
		return this.transactionalValueMap;
	}

	void setUnsave(String column, Object obj, boolean fromSql) {
		try {
			this.transactionalValueMap.put(column, this.table.createSqlObject(column, obj, fromSql)); // FromSQL
		} catch (TableModuleException e) {
			e.printStackTrace();
		}
	}

	SqlObject getUnsave(String column) {
		SqlObject obj = (obj = this.transactionalValueMap.get(column)) != null ? obj : this.valueMap.get(column);
		return obj;
	}

	void applyCreate() {
		this.transactionalValueMap.forEach((k, v) -> this.valueMap.put(k, v));
		this.pk = this.transactionalValueMap.get(this.pkName);
		this.table.createRow(this, this.pk);
		this.transactionalValueMap = new HashMap<>();
	}

	void applyWrite() {
		this.transactionalValueMap.forEach((k, v) -> this.valueMap.put(k, v));
		this.transactionalValueMap = new HashMap<>();
	}

	void applyDelete() {
		this.table.clearRow(this, this.pk);
	}

	void rollbackCreate() {
		this.table.undoMkRow(this, this.transactionalValueMap.get(this.pkName));
		this.transactionalValueMap = new HashMap<>();
	}

	void rollback() {
		this.transactionalValueMap = new HashMap<>();
	}

	private void checkTransactionAndLockIfPossible(Transaction transaction) throws TableModuleException {
		if (this.deleted) {
			RowException.Error.RowDeleted.build();
		}
		if (!((TransactionImpl) transaction).lockIfExists()) {
			RowException.Error.WrongTransaction.build();
		}
		return;
	}

	private static class RowException extends TableModuleException {

		private static final long serialVersionUID = 1L;

		private RowException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			RowDeleted("The row no longer exists."), //
			WrongTransaction("Unknown or already finished transaction, use an othert one."), //
			DoNotChangePK("Can't change pk for existing rows."); //

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws TableModuleException {
				TableModuleException.build(new RowException(this, arg));
			}

		}
	}

}
