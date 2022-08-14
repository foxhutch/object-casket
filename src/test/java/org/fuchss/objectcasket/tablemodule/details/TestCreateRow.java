package org.fuchss.objectcasket.tablemodule.details;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateRow extends PrepareModule {

	@Test
	void createRow() throws CasketException, IOException {
		this.createRow(DB.SQLITE);
		this.createRow(DB.H2);
	}

	@Test
	void createWrongRow() throws IOException, CasketException {
		this.createWrongRow(DB.SQLITE);
		this.createWrongRow(DB.H2);
	}

	void createRow(DB dialect) throws CasketException, IOException {
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
			for (String col : this.textColumns)
				Assertions.assertEquals(values.get(col), row[idx].getValue(col, String.class));
		}

		for (int idx = 0; idx < row.length; idx++) {
			Assertions.assertTrue(row[idx].isDirty());
		}

		this.tabMod.endTransaction(voucher);

		for (int idx = 0; idx < row.length; idx++) {
			Assertions.assertFalse(row[idx].isDirty());
		}

		this.delTable();
	}

	void createWrongRow(DB dialect) throws IOException, CasketException {
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
			for (String col : this.textColumns)
				Assertions.assertEquals(values.get(col), row[idx].getValue(col, String.class));
		}

		Serializable oldVal = values.put(this.textColumns[0], new LinkedList<Integer>());
		boolean error = false;
		Row rowX = null;
		try {
			rowX = this.table[0].createRow(values, voucher);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(rowX);

		values.put(this.textColumns[0], oldVal);

		for (int idx = 0; idx < row.length; idx++) {
			Assertions.assertTrue(row[idx].isDirty());
			Assertions.assertNull(row[idx].getPk(this.pkType));
			for (String col : this.textColumns) {
				Object x = values.get(col);
				Assertions.assertEquals(x, row[idx].getValue(col, String.class));
			}
		}

		error = false;
		try {
			this.tabMod.endTransaction(voucher);
		} catch (CasketException exc) {
			error = true;
		}
		Assertions.assertTrue(error);

		this.delTable();
	}

	private static class TableA {

	}

	private static class TableB {

	}

	private static class TableC {

	}

}
