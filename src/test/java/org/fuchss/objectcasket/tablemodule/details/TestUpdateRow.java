package org.fuchss.objectcasket.tablemodule.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class TestUpdateRow extends PrepareModule {

	@Test
	void updateRow() throws CasketException, IOException {
		this.updateRow(DB.SQLITE);
		this.updateRow(DB.H2);

	}

	@Test
	void updateRowOneByOne() throws CasketException, IOException {
		this.updateRowOneByOne(DB.SQLITE);
		this.updateRowOneByOne(DB.H2);
	}

	private void updateRow(DB dialect) throws CasketException, IOException {

		Row[][] row = this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		Object[][] values = new Object[this.table.length][row.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				int i = 0;
				Map<String, Serializable> map = new HashMap<>();
				values[tIdx][rIdx] = map;
				for (String col : this.textColumns)
					map.put(col, "abc" + ((1000 * (tIdx + 1)) + (10 * rIdx) + i++));
			}
		}

		Object voucher = this.tabMod.beginTransaction();
		Assertions.assertNotNull(voucher);

		for (int tIdx = 0; tIdx < row.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				@SuppressWarnings("unchecked") Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				this.table[tIdx].updateRow(row[tIdx][rIdx], map, voucher);
			}
		}

		this.tabMod.endTransaction(voucher);
		this.delTable();

	}

	private void updateRowOneByOne(DB dialect) throws CasketException, IOException {

		Row[][] row = this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		Object[][] values = new Object[this.table.length][row.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				int i = 0;
				Map<String, Serializable> map = new HashMap<>();
				values[tIdx][rIdx] = map;
				for (String col : this.textColumns)
					map.put(col, "abc" + ((1000 * (tIdx + 1)) + (10 * rIdx) + i++));
			}
		}

		Object voucher = this.tabMod.beginTransaction();

		for (int tIdx = 0; tIdx < row.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				@SuppressWarnings("unchecked") Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				this.table[tIdx].updateRow(row[tIdx][rIdx], map, voucher);
			}
		}

		this.tabMod.endTransaction(voucher);
		this.delTable();

		Row[][] row2 = this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		values = new Object[this.table.length][row.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			for (int rIdx = 0; rIdx < row2.length; rIdx++) {
				int i = 0;
				Map<String, Serializable> map = new HashMap<>();
				values[tIdx][rIdx] = map;
				for (String col : this.textColumns)
					map.put(col, "abc" + ((1000 * (tIdx + 1)) + (10 * rIdx) + i++));
			}
		}

		Map<String, Serializable> singleMap = new HashMap<>();
		for (int tIdx = 0; tIdx < row2.length; tIdx++) {
			for (int rIdx = 0; rIdx < row2.length; rIdx++) {
				@SuppressWarnings("unchecked") Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				for (String col : map.keySet()) {
					singleMap.put(col, map.get(col));
					voucher = this.tabMod.beginTransaction();
					this.table[tIdx].updateRow(row2[tIdx][rIdx], singleMap, voucher);
					this.tabMod.endTransaction(voucher);
					singleMap.clear();
				}
			}
		}

		for (int tIdx = 0; tIdx < row.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				@SuppressWarnings("unchecked") Map<String, Serializable> map = (Map<String, Serializable>) values[tIdx][rIdx];
				for (String col : map.keySet()) {
					Assertions.assertEquals(row[tIdx][rIdx].getValue(col, this.columns.get(col)), row2[tIdx][rIdx].getValue(col, this.columns.get(col)));
				}
			}
		}

		this.delTable();

	}

	private static class TableA {

	}

	private static class TableB {

	}

	private static class TableC {

	}

}
