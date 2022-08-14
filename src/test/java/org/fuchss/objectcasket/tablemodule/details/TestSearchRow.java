package org.fuchss.objectcasket.tablemodule.details;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.Table.TabCMP;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestSearchRow extends PrepareModule {

	@Test
	void searchRow() throws CasketException, IOException {
		this.searchRow(DB.SQLITE);
		this.searchRow(DB.H2);
	}

	@Test
	void searchRow2() throws CasketException, IOException {
		this.searchRow2(DB.SQLITE);
		this.searchRow2(DB.H2);
	}

	@Test
	void allRow() throws CasketException, IOException {
		this.allRow(DB.SQLITE);
		this.allRow(DB.H2);
	}

	@Test
	void allRow2() throws CasketException, IOException {
		this.allRow2(DB.SQLITE);
		this.allRow2(DB.H2);
	}

	private void searchRow(DB dialect) throws CasketException, IOException {

		this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		@SuppressWarnings("unchecked")
		Set<Table.Exp>[] args = new HashSet[this.table.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			args[tIdx] = new HashSet<>();
			args[tIdx].add(new Table.Exp(this.textColumns[tIdx], TabCMP.UNEQUAL, "xyz"));
		}

		Object voucher = this.tabMod.beginTransaction();
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			List<Row> rows = this.table[tIdx].searchRows(args[tIdx], voucher);
			for (Row r : rows) {
				Object pkObj = r.getValue(this.pkColumn, this.pkType);
				Assertions.assertNotNull(pkObj);
				Assertions.assertEquals(pkObj, r.getPk(this.pkType));
			}
		}
		this.tabMod.endTransaction(voucher);

		this.delTable();

	}

	private void searchRow2(DB dialect) throws CasketException, IOException {

		this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		this.reopenModule(TableA.class, TableB.class, TableC.class);

		@SuppressWarnings("unchecked")
		Set<Table.Exp>[] args = new HashSet[this.table.length];
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			args[tIdx] = new HashSet<>();
			args[tIdx].add(new Table.Exp(this.textColumns[tIdx], TabCMP.UNEQUAL, "xyz"));
		}

		Object voucher = this.tabMod.beginTransaction();
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			List<Row> rows = this.table[tIdx].searchRows(args[tIdx], voucher);
			for (Row r : rows) {
				Assertions.assertNotNull(r.getPk(this.pkType));
			}
		}
		this.tabMod.endTransaction(voucher);

		this.delTable();

	}

	private void allRow(DB dialect) throws CasketException, IOException {

		this.initTables(dialect, TableA.class, TableB.class, TableC.class);

		Object voucher = this.tabMod.beginTransaction();
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			List<Row> rows = this.table[tIdx].allRows(voucher);
			for (Row r : rows) {
				Assertions.assertNotNull(r.getPk(this.pkType));
			}
		}
		this.tabMod.endTransaction(voucher);

		this.delTable();

	}

	private void allRow2(DB dialect) throws CasketException, IOException {

		this.initTables(dialect, TableA.class, TableB.class, TableC.class);
		this.reopenModule(TableA.class, TableB.class, TableC.class);

		Object voucher = this.tabMod.beginTransaction();
		for (int tIdx = 0; tIdx < this.table.length; tIdx++) {
			List<Row> rows = this.table[tIdx].allRows(voucher);
			for (Row r : rows) {
				Assertions.assertNotNull(r.getPk(this.pkType));
			}
		}
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
