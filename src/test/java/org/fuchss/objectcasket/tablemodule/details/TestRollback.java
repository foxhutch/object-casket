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

class TestRollback extends PrepareModule {

	@Test
	void rollbackRow() throws CasketException, IOException {
		this.rollbackRow(DB.SQLITE);
		this.rollbackRow(DB.H2);
	}

	@Test
	void rollbackRow2() throws CasketException, IOException {
		this.rollbackRow2(DB.SQLITE);
		this.rollbackRow2(DB.H2);
	}

	private void rollbackRow(DB dialect) throws CasketException, IOException {
		this.initModule(dialect, true, TableA.class, TableB.class, TableC.class);
		Object voucher = this.tabMod.beginTransaction();

		Map<String, Serializable> values = new HashMap<>();

		int i = 0;
		for (String col : this.textColumns) {
			values.put(col, "abc" + i++);
		}

		Row[] row = new Row[this.table.length];
		for (int idx = 0; idx < row.length; idx++) {
			row[idx] = this.table[idx].createRow(values, voucher);
			Assertions.assertNotNull(row[idx]);
			Assertions.assertNotNull(row[idx].getPk(this.pkType));
		}

		this.tabMod.rollback(voucher);

		for (int idx = 0; idx < row.length; idx++) {
			Assertions.assertNotNull(row[idx]);
			Assertions.assertNull(row[idx].getPk(this.pkType));
		}

		this.delTable();
	}

	private void rollbackRow2(DB dialect) throws CasketException, IOException {
		this.initModule(dialect, false, TableA.class, TableB.class, TableC.class);
		Object voucher = this.tabMod.beginTransaction();

		Map<String, Serializable> values = new HashMap<>();

		int i = 0;
		for (String col : this.textColumns) {
			values.put(col, "abc" + i++);
		}

		Row[] row = new Row[this.table.length];
		for (int idx = 0; idx < row.length; idx++) {
			values.put(this.pkColumn, 2 + idx);
			row[idx] = this.table[idx].createRow(values, voucher);
			Assertions.assertNotNull(row[idx]);
			Assertions.assertNotNull(row[idx].getPk(this.pkType));
		}

		this.tabMod.rollback(voucher);

		for (int idx = 0; idx < row.length; idx++) {
			Assertions.assertNotNull(row[idx]);
			Assertions.assertNotNull(row[idx].getPk(this.pkType));
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
