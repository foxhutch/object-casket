package org.fuchss.objectcasket.sqlconnector.api;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TestAlterTable extends PrepareTable {

	@Test
	void deletetRow() throws CasketException, IOException {
		this.deletetRow(DB.SQLITE);
		this.deletetRow(DB.H2);
	}

	@Test
	void dropTable() throws CasketException, IOException {
		this.dropTable(DB.SQLITE);
		this.dropTable(DB.H2);
	}

	private void deletetRow(DB db) throws CasketException, IOException {
		this.initTable(db);
		this.dbFac.closeDatabase(this.db);
		this.db = this.dbFac.openDatabase(this.config);

		SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
		SqlColumnSignature textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, null);

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		columns.put(PrepareTable.PKname, pkCol);
		columns.put("NewColumn1", textCol);
		columns.put("NewColumn2", textCol);

		boolean error = false;
		TableAssignment tab = null;
		try {
			tab = this.db.adjustTable(PrepareTable.TableName, columns);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(tab);

		pkCol.setFlag(Flag.PRIMARY_KEY);
		pkCol.setFlag(Flag.AUTOINCREMENT);
		// pkCol.setFlag(Flag.NOT_NULL);

		tab = this.db.adjustTable(PrepareTable.TableName, columns);

		Assertions.assertNotNull(tab);

		this.dbFac.closeDatabase(this.db);
		this.db = this.dbFac.openDatabase(this.config);

		columns.clear();

		columns.put(PrepareTable.PKname, pkCol);

		tab = null;
		try {
			tab = this.db.adjustTable(PrepareTable.TableName, columns);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(tab);

		textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, "abc");
		columns.put("NewColumn1", textCol);

		try {
			tab = this.db.adjustTable(PrepareTable.TableName, columns);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(tab);

		textCol = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, null);
		columns.put("NewColumn1", textCol);

		tab = this.db.adjustTable(PrepareTable.TableName, columns);

		Assertions.assertNotNull(tab);

		this.delTable();
	}

	private void dropTable(DB db) throws CasketException, IOException {
		this.initTable(db);
		this.dbFac.closeDatabase(this.db);
		this.db = this.dbFac.openDatabase(this.config);

		SqlColumnSignature pkCol = this.factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
		pkCol.setFlag(Flag.PRIMARY_KEY);
		pkCol.setFlag(Flag.AUTOINCREMENT);
		// pkCol.setFlag(Flag.NOT_NULL);

		Map<String, SqlColumnSignature> columns = new HashMap<>();
		columns.put(PrepareTable.PKname, pkCol);

		TableAssignment tab = null;
		tab = this.db.createView(PrepareTable.TableName, columns);
		Assertions.assertNotNull(tab);

		this.dbFac.closeDatabase(this.db);
		this.db = this.dbFac.openDatabase(this.config);

		this.db.dropTable(PrepareTable.TableName);

		boolean error = false;
		tab = null;
		try {
			tab = this.db.createView(PrepareTable.TableName, columns);
		} catch (CasketException e) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(tab);

		this.delTable();
	}

}
