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

class TestCreateRow extends PrepareTable {

	@Test
	void createRow() throws CasketException, IOException {
		this.createRow(DB.SQLITE);
		this.createRow(DB.H2);
	}

	private void createRow(DB db) throws CasketException, IOException {
		this.initTable(db);

		Map<String, SqlObject> values = new HashMap<>();
		values.put(PrepareTable.TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "123456789"));

		PreCompiledStatement preStat = this.db.mkNewRowStmt(this.table);
		Object voucher = this.db.beginTransaction(true);

		Map<String, SqlObject> pk = null;
		try {
			pk = this.db.newRow(preStat, values, null);
		} catch (Exception e) {
		}
		Assertions.assertNull(pk);

		try {
			pk = this.db.newRow(preStat, values, new Object());
		} catch (Exception e) {
		}
		Assertions.assertNull(pk);

		pk = this.db.newRow(preStat, values, voucher);

		this.db.endTransaction(voucher);

		Assertions.assertNotNull(pk);
		Assertions.assertNotNull(pk.get(PrepareTable.PKname));

		this.delTable();
	}

}
