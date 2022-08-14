package org.fuchss.objectcasket.sqlconnector.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

class TestUsingBlobs {

	private static final String PKname = "PK";
	private static final String BlobCol = "blob";
	private static final String TableName = "Table";

	private SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
	private SqlDatabaseFactory dbFac = SqlPort.SQL_PORT.sqlDatabaseFactory();

	private SqlDatabase db = null;
	private TableAssignment table = null;

	private SqlObject pk = null;
	private int[] pks = new int[3];

	@Test
	void selectBlob() throws CasketException, IOException {
		this.selectBlob(DB.SQLITE);
		this.selectBlob(DB.H2);
	}

	@Test
	void selectBlob2() throws CasketException, IOException {
		this.selectBlob2(DB.SQLITE);
		this.selectBlob2(DB.H2);

	}

	private void selectBlob(DB dialect) throws CasketException, IOException {
		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, dialect);
			this.initTable(config);

			Set<SqlArg> argSet = new HashSet<>();
			SqlArg arg = this.db.mkSqlArg(this.table, PKname, CMP.EQUAL);
			argSet.add(arg);
			PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);

			Map<SqlArg, SqlObject> args = new HashMap<>();
			args.put(arg, this.factory.mkSqlObject(StorageClass.INTEGER, this.pks[0]));

			Object voucher = this.db.beginTransaction(true);
			List<Map<String, SqlObject>> result = this.db.select(select, args, voucher);
			this.db.endTransaction(voucher);

			Assertions.assertNotNull(result);
			Assertions.assertEquals(1, result.size());
			int[] check = new int[] { 1, 2, 3 };
			Assertions.assertArrayEquals(result.get(0).get(BlobCol).get(int[].class), check);

		} finally {
			this.dbFac.closeDatabase(this.db);
			Utility.deleteFile(dbFile);
		}

	}

	private void selectBlob2(DB dialect) throws CasketException, IOException {
		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, dialect);
			this.initTable(config);

			Set<SqlArg> argSet = new HashSet<>();
			SqlArg arg = this.db.mkSqlArg(this.table, PKname, CMP.UNEQUAL);
			argSet.add(arg);
			PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);

			Map<SqlArg, SqlObject> args = new HashMap<>();
			args.put(arg, this.factory.mkSqlObject(StorageClass.INTEGER, this.pks[0]));

			Object voucher = this.db.beginTransaction(false);
			List<Map<String, SqlObject>> result = this.db.select(select, args, voucher);
			this.db.endTransaction(voucher);

			Assertions.assertNotNull(result);
			Assertions.assertEquals(2, result.size());
			int[][] check = new int[][] { { 4, 5, 6 }, { 7, 8, 9 } };

			Assertions.assertArrayEquals(result.get(0).get(BlobCol).get(int[].class), check[0]);
			Assertions.assertArrayEquals(result.get(1).get(BlobCol).get(int[].class), check[1]);

		} finally {
			this.dbFac.closeDatabase(this.db);
			Utility.deleteFile(dbFile);
		}

	}

	private void initTable(DBConfiguration config) throws CasketException, IOException {

		this.db = this.dbFac.openDatabase(config);

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
		pkCol.setFlag(Flag.PRIMARY_KEY);
		pkCol.setFlag(Flag.AUTOINCREMENT);
		columns.put(PKname, pkCol);

		SqlColumnSignature blobCol = this.factory.mkColumnSignature(StorageClass.BLOB, int[].class, null);
		columns.put(BlobCol, blobCol);

		this.table = this.db.createTable(TableName, columns);

		PreCompiledStatement preStat = this.db.mkNewRowStmt(this.table);

		Map<String, SqlObject> values = new HashMap<>();

		Object voucher = this.db.beginTransaction(true);
		int[] blob = new int[] { 1, 2, 3 };
		values.put(BlobCol, this.factory.mkSqlObject(StorageClass.BLOB, blob));
		this.pk = this.db.newRow(preStat, values, voucher).get(PKname);
		this.pks[0] = this.pk.get(Integer.TYPE);

		values.clear();
		blob = new int[] { 4, 5, 6 };
		values.put(BlobCol, this.factory.mkSqlObject(StorageClass.BLOB, blob));
		this.pks[1] = this.db.newRow(preStat, values, voucher).get(PKname).get(Integer.TYPE);

		values.clear();
		blob = new int[] { 7, 8, 9 };
		values.put(BlobCol, this.factory.mkSqlObject(StorageClass.BLOB, blob));
		this.pks[2] = this.db.newRow(preStat, values, voucher).get(PKname).get(Integer.TYPE);

		this.db.endTransaction(voucher);

	}

}
