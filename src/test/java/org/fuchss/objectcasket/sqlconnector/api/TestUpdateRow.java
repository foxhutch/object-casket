package org.fuchss.objectcasket.sqlconnector.api;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TestUpdateRow extends PrepareTable {

	@Test
	void createRow() throws CasketException, IOException {
		this.createRow(DB.SQLITE);
		this.createRow(DB.H2);
	}

	private void createRow(DB db) throws CasketException, IOException {
		this.initTable(db);

		Map<String, SqlObject> values = new HashMap<>();
		values.put(PrepareTable.TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "xyz"));

		PreCompiledStatement preStat = this.db.mkUpdateRowStmt(this.table, values.keySet());
		Object voucher = this.db.beginTransaction(true);
		boolean ok = false;
		try {
			this.db.updateRow(preStat, this.pk, values, null);
		} catch (Exception e) {
			ok = true;
		}
		Assertions.assertTrue(ok);
		ok = false;
		try {
			this.db.updateRow(preStat, this.pk, values, new Object());
		} catch (Exception e) {
			ok = true;
		}
		Assertions.assertTrue(ok);
		this.db.updateRow(preStat, this.pk, values, voucher);

		this.db.endTransaction(voucher);

		this.delTable();
	}

}
