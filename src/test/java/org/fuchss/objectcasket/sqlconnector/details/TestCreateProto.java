package org.fuchss.objectcasket.sqlconnector.details;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;

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

		this.createWrongPK(factory, StorageClass.INTEGER, Integer.TYPE, null, "Test", CasketError.MISPLACED_AUTO_INCREMENT.build(), true);
	}

	@Test
	void createWrongProtoType() throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

		this.createPrototype(factory, StorageClass.INTEGER, String.class, "X", CasketError.INCOMPATIBLE_TYPES.build(), false);
		this.createPrototype(factory, StorageClass.INTEGER, String.class, null, CasketError.INVALID_PROTOTYPE.build(), false);

		this.createPrototype(factory, StorageClass.REAL, Boolean.class, Boolean.valueOf(false), CasketError.INCOMPATIBLE_TYPES.build(), false);
		this.createPrototype(factory, StorageClass.REAL, Boolean.class, null, CasketError.INVALID_PROTOTYPE.build(), false);

		this.createPrototype(factory, StorageClass.TEXT, Boolean.class, Boolean.valueOf(false), CasketError.INCOMPATIBLE_TYPES.build(), false);
		this.createPrototype(factory, StorageClass.TEXT, Boolean.class, null, CasketError.INVALID_PROTOTYPE.build(), false);
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

	private <T extends Serializable> void createWrongPK(SqlObjectFactory factory, StorageClass type, Class<T> clazz, T defaultValue, String name, CasketException error, boolean auto)
			throws CasketException {
		SqlColumnSignature prototype = factory.mkColumnSignature(type, clazz, defaultValue);
		Assertions.assertNotNull(prototype);

		Exception exc = null;
		try {
			prototype.setFlag(Flag.PRIMARY_KEY);
			if (auto)
				prototype.setFlag(Flag.AUTOINCREMENT);
		} catch (Exception e) {
			exc = e;
		}
		Assertions.assertNotNull(exc);
		Assertions.assertEquals(exc.getMessage(), error.getMessage());
	}

	private <T extends Serializable> void createPrototype(SqlObjectFactory factory, StorageClass type, Class<T> clazz, T defaultValue, CasketException error, boolean noError) {
		try {
			factory.mkColumnSignature(type, clazz, defaultValue);
			Assertions.assertTrue(noError);
		} catch (CasketException exception) {
			Assertions.assertEquals(exception.getMessage(), error.getMessage());
		}
	}

}
