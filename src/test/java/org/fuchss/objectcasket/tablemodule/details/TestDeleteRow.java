package org.fuchss.objectcasket.tablemodule.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TestDeleteRow extends PrepareModule {

	@Test
	void updateRow() throws CasketException, IOException {
		this.updateRow(DB.SQLITE);
		this.updateRow(DB.H2);
	}

	private void updateRow(DB dialect) throws CasketException, IOException {

		Row[][] row = this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		Object voucher = this.tabMod.beginTransaction();

		for (int tIdx = 0; tIdx < row.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {
				Assertions.assertFalse(row[tIdx][rIdx].isDirty());
				Assertions.assertNotNull(row[tIdx][rIdx].getPk(this.pkType));
				for (String col : this.textColumns)
					Assertions.assertNotNull(row[tIdx][rIdx].getValue(col, String.class));

				this.table[tIdx].deleteRow(row[tIdx][rIdx], voucher);

				Assertions.assertTrue(row[tIdx][rIdx].isDirty());
				Assertions.assertNull(row[tIdx][rIdx].getPk(this.pkType));
				for (String col : this.textColumns)
					Assertions.assertNull(row[tIdx][rIdx].getValue(col, String.class));
				Assertions.assertTrue(this.table[tIdx].allRows(voucher).contains(row[tIdx][rIdx]));
			}
		}
		this.tabMod.endTransaction(voucher);

		voucher = this.tabMod.beginTransaction();
		for (int tIdx = 0; tIdx < row.length; tIdx++)
			Assertions.assertTrue(this.table[tIdx].allRows(voucher).isEmpty());

		this.tabMod.endTransaction(voucher);

		this.delTable();

	}

	private static class TableA {

	}

	private static class TableB {

	}

	private static class TableC {

	}

}
