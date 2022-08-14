package org.fuchss.objectcasket.tablemodule.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;

class TransactionImpl {

	protected Map<TableImpl, Set<RowImpl>> created = new HashMap<>();
	protected Map<TableImpl, Set<RowImpl>> deleted = new HashMap<>();
	protected Map<TableImpl, Set<RowImpl>> changed = new HashMap<>();

	protected Object voucher;

	public TransactionImpl(Object obj) {
		this.voucher = obj;
	}

	protected void add2created(TableImpl tab, RowImpl row) {
		Set<RowImpl> createdRows = this.created.computeIfAbsent(tab, k -> new HashSet<>());
		createdRows.add(row);
	}

	protected void add2deleted(TableImpl tab, RowImpl row) {
		Set<RowImpl> createdRows = this.created.get(tab);
		Set<RowImpl> changedRows = this.changed.get(tab);
		Set<RowImpl> deletedRows = this.deleted.computeIfAbsent(tab, k -> new HashSet<>());
		if (createdRows != null)
			createdRows.remove(row);
		if (changedRows != null)
			changedRows.remove(row);
		deletedRows.add(row);
	}

	protected void add2changed(TableImpl tab, RowImpl row) {
		Set<RowImpl> createdRows = this.created.get(tab);
		if ((createdRows != null) && createdRows.contains(row))
			return;
		Set<RowImpl> changedRows = this.changed.computeIfAbsent(tab, k -> new HashSet<>());
		changedRows.add(row);
	}

	protected void rollbackCreated(Object voucher) throws CasketException {
		if (this.voucher != voucher)
			throw CasketError.UNKNOWN_TRANSACTION.build();
		this.created.forEach((tab, rows) -> tab.rollback(rows));
		this.voucher = null;
	}

	protected void done() {
		this.done(this.created);
		this.done(this.changed);
		this.done(this.deleted);
	}

	private void done(Map<TableImpl, Set<RowImpl>> map) {
		for (Set<RowImpl> rows : map.values()) {
			if (rows == null)
				continue;
			rows.forEach(RowImpl::done);
		}
		this.voucher = null;
	}

}
