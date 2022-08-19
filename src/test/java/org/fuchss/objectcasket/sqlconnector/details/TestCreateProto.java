package org.fuchss.objectcasket.sqlconnector.details;

import java.io.Serializable;
import java.util.Date;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketError.CE1;
import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DialectSqlite;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateProto {

	private SqlDialect dialect = new DialectSqlite();

	@Test
	void createPKs() throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

		// INTEGER AUTO
		this.createCorrectPK(factory, StorageClass.INTEGER, Long.class, null, "INTEGER", true);
		this.createCorrectPK(factory, StorageClass.INTEGER, Integer.class, null, "INTEGER", true);
		this.createCorrectPK(factory, StorageClass.INTEGER, Short.class, null, "INTEGER", true);
		this.createCorrectPK(factory, StorageClass.INTEGER, Byte.class, null, "INTEGER", true);

		// INTEGER
		this.createCorrectPK(factory, StorageClass.INTEGER, Long.class, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Long.TYPE, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Integer.class, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Integer.TYPE, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Short.class, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Short.TYPE, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Byte.class, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Byte.TYPE, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Boolean.class, null, "INTEGER", false);
		this.createCorrectPK(factory, StorageClass.INTEGER, Boolean.TYPE, null, "INTEGER", false);

		// DATE
		this.createCorrectPK(factory, StorageClass.INTEGER, Date.class, null, "INTEGER", false);

		// REAL
		this.createCorrectPK(factory, StorageClass.REAL, Double.class, null, "REAL", false);
		this.createCorrectPK(factory, StorageClass.REAL, Double.TYPE, null, "REAL", false);
		this.createCorrectPK(factory, StorageClass.REAL, Float.class, null, "REAL", false);
		this.createCorrectPK(factory, StorageClass.REAL, Float.TYPE, null, "REAL", false);

		// TEXT
		this.createCorrectPK(factory, StorageClass.TEXT, Character.class, null, "TEXT", false);
		this.createCorrectPK(factory, StorageClass.TEXT, Character.TYPE, null, "TEXT", false);
		this.createCorrectPK(factory, StorageClass.TEXT, String.class, null, "TEXT", false);
	}

	@Test
	void createWrongPK() throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

		this.createWrongPK(factory, StorageClass.INTEGER, Integer.TYPE, null, "Test", CE1.MISPLACED_AUTO_INCREMENT, true);
	}

	@Test
	void createWrongProtoType() throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

		this.createPrototype(factory, StorageClass.INTEGER, String.class, "X", CE3.INCOMPATIBLE_SQL_TYPE, false);
		this.createPrototype(factory, StorageClass.INTEGER, String.class, null, CE3.INVALID_PROTOTYPE, false);

		this.createPrototype(factory, StorageClass.REAL, Boolean.class, Boolean.valueOf(false), CE3.INCOMPATIBLE_SQL_TYPE, false);
		this.createPrototype(factory, StorageClass.REAL, Boolean.class, null, CE3.INVALID_PROTOTYPE, false);

		this.createPrototype(factory, StorageClass.TEXT, Boolean.class, Boolean.valueOf(false), CE3.INCOMPATIBLE_SQL_TYPE, false);
		this.createPrototype(factory, StorageClass.TEXT, Boolean.class, null, CE3.INVALID_PROTOTYPE, false);
	}

	@Test
	void createBlobs() throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

		this.createPrototype(factory, StorageClass.BLOB, String.class, "A", null, true);

		this.createPrototype(factory, StorageClass.BLOB, String[].class, new String[] { "A", "B" }, null, true);

	}

	private <T extends Serializable> void createCorrectPK(SqlObjectFactory factory, StorageClass type, Class<T> clazz, T defaultValue, String name, boolean auto) throws CasketException {
		SqlColumnSignature prototype = factory.mkColumnSignature(type, clazz, defaultValue);
		Assertions.assertNotNull(prototype);

		Exception exc = null;
		try {
			prototype.setFlag(Flag.PRIMARY_KEY);
			if (auto) {
				prototype.setFlag(Flag.AUTOINCREMENT);
				prototype.setFlag(Flag.AUTOINCREMENT);
				prototype.resetFlag(Flag.AUTOINCREMENT);
				prototype.resetFlag(Flag.AUTOINCREMENT);
				prototype.setFlag(Flag.AUTOINCREMENT);
			}
		} catch (Exception e) {
			exc = e;
		}
		Assertions.assertNull(exc);

		Assertions.assertEquals(name, this.dialect.storageClassString(prototype.getType()));
		Assertions.assertTrue(prototype.isPrimaryKey());
		if (auto)
			Assertions.assertTrue(prototype.isAutoIncrementedPrimaryKey());
	}

	private <T extends Serializable> void createWrongPK(SqlObjectFactory factory, StorageClass type, Class<T> clazz, T defaultValue, String name, CasketError error, boolean auto) throws CasketException {
		SqlColumnSignature prototype = factory.mkColumnSignature(type, clazz, defaultValue);
		Assertions.assertNotNull(prototype);

		CasketException exc = null;
		try {
			prototype.setFlag(Flag.PRIMARY_KEY);
			if (auto)
				prototype.setFlag(Flag.AUTOINCREMENT);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNotNull(exc);
		Assertions.assertEquals(error, exc.error());
	}

	private <T extends Serializable> void createPrototype(SqlObjectFactory factory, StorageClass type, Class<T> clazz, T defaultValue, CasketError error, boolean noError) {
		try {
			factory.mkColumnSignature(type, clazz, defaultValue);
			Assertions.assertTrue(noError);
		} catch (CasketException exception) {
			Assertions.assertEquals(error, exception.error());
		}
	}

}
