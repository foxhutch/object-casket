package org.fuchss.objectcasket.sqlconnector.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDeleteRow extends PrepareTable {

	@Test
	void deletetRow() throws CasketException, IOException {
		this.deletetRow(DB.SQLITE);
		this.deletetRow(DB.H2);
	}

	private void deletetRow(DB db) throws CasketException, IOException {
		this.initTable(db);

		SqlObject pk = null;
		List<Map<String, SqlObject>> result = null;
		Set<SqlArg> argSet = new HashSet<>();

		SqlArg arg = this.db.mkSqlArg(this.table, TextCol, CMP.EQUAL);
		argSet.add(arg);
		PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);
		PreCompiledStatement delete = this.db.mkDeleteStmt(this.table, argSet, OP.AND);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(arg, this.factory.mkSqlObject(StorageClass.TEXT, "123"));

		Object voucher = this.db.beginTransaction(true);
		result = this.db.select(select, args, null);
		this.db.endTransaction(voucher);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		pk = result.get(0).get(PKname);

		voucher = this.db.beginTransaction(true);

		CasketException exc = null;
		List<SqlObject> pks = null;
		try {
			pks = this.db.delete(delete, args, null);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CasketError.WRONG_TRANSACTION, exc.error());
		Assertions.assertNull(pks);

		exc = null;
		try {
			pks = this.db.delete(delete, args, new Object());
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CasketError.WRONG_TRANSACTION, exc.error());
		Assertions.assertNull(pks);

		pks = this.db.delete(delete, args, voucher);

		result = this.db.select(select, args, voucher);

		this.db.endTransaction(voucher);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(0, result.size());
		Assertions.assertNotNull(pks);
		Assertions.assertEquals(1, pks.size());
		Assertions.assertEquals(pk.get(Integer.class), pks.get(0).get(Integer.class));

		select.close();
		delete.close();
		this.delTable();

	}

}