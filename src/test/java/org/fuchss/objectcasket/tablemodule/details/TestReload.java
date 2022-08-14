package org.fuchss.objectcasket.tablemodule.details;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestReload extends PrepareModule {

	Row[][] row;
	Object[][] val;

	@Test
	void reloadRow() throws CasketException, IOException {
		this.reloadRow(DB.SQLITE);
		this.reloadRow(DB.H2);
	}

	private void reloadRow(DB dialect) throws CasketException, IOException {

		this.init(dialect);

		Object[][] values = new Object[this.table.length][this.row.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				int i = 0;
				Map<String, Serializable> map = new HashMap<>();
				values[tIdx][rIdx] = map;
				for (String col : this.textColumns)
					map.put(col, "xyz" + ((1000 * (tIdx + 1)) + (10 * rIdx) + i++));
			}
		}

		Object voucher = this.tabMod.beginTransaction();

		for (int tIdx = 0; tIdx < this.row.length; tIdx++) {
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				@SuppressWarnings("unchecked")
				Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				this.table[tIdx].updateRow(this.row[tIdx][rIdx], map, voucher);
			}
		}

		for (int tIdx = 0; tIdx < this.row.length; tIdx++)
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				@SuppressWarnings("unchecked")
				Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				for (String col : this.textColumns)
					Assertions.assertEquals(map.get(col), this.row[tIdx][rIdx].getValue(col, String.class));
			}

		this.tabMod.rollback(voucher);

		for (int tIdx = 0; tIdx < this.row.length; tIdx++)
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				@SuppressWarnings("unchecked")
				Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				for (String col : this.textColumns)
					Assertions.assertEquals(map.get(col), this.row[tIdx][rIdx].getValue(col, String.class));
			}

		voucher = this.tabMod.beginTransaction();

		for (int tIdx = 0; tIdx < this.row.length; tIdx++) {
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				this.table[tIdx].reloadRow(this.row[tIdx][rIdx], voucher);
			}
		}

		this.tabMod.endTransaction(voucher);

		for (int tIdx = 0; tIdx < this.row.length; tIdx++)
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				@SuppressWarnings("unchecked")
				Map<String, Serializable> map = (Map<String, Serializable>) this.val[tIdx][rIdx];
				for (String col : this.textColumns)
					Assertions.assertEquals(map.get(col), this.row[tIdx][rIdx].getValue(col, String.class));
			}

		this.delTable();

	}

	private void init(DB dialect) throws CasketException, IOException {
		this.row = this.initTables(dialect, TableA.class, TableB.class, TableC.class);
		this.val = new Object[this.table.length][this.row.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				int i = 0;
				Map<String, Object> map = new HashMap<>();
				this.val[tIdx][rIdx] = map;
				for (String col : this.textColumns)
					map.put(col, "abc" + ((1000 * (tIdx + 1)) + (10 * rIdx) + i++));
			}
		}

		Object voucher = this.tabMod.beginTransaction();

		for (int tIdx = 0; tIdx < this.row.length; tIdx++) {
			for (int rIdx = 0; rIdx < this.row.length; rIdx++) {
				@SuppressWarnings("unchecked")
				Map<String, Serializable> map = (Map<String, Serializable>) this.val[tIdx][rIdx];
				this.table[tIdx].updateRow(this.row[tIdx][rIdx], map, voucher);
			}
		}

		this.tabMod.endTransaction(voucher);

	}

	private static class TableA {

	}

	private static class TableB {

	}

	private static class TableC {

	}

}
