package org.fuchss.objectcasket.testutils;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Driver;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Utility {

	public static enum DB {
		SQLITE, H2;
	}

	private static Map<DB, Class<? extends Driver>> _dialectDriverMap = new HashMap<>();

	static {
		_dialectDriverMap.put(DB.H2, org.h2.Driver.class);
		_dialectDriverMap.put(DB.SQLITE, org.sqlite.JDBC.class);
	}

	private static Map<DB, String> _dialectUrlPrefixMap = new HashMap<>();

	static {
		_dialectUrlPrefixMap.put(DB.H2, "jdbc:h2:");
		_dialectUrlPrefixMap.put(DB.SQLITE, org.sqlite.JDBC.PREFIX);
	}

	private static Map<DB, SqlDialect> _dialectMap = new HashMap<>();

	static {
		_dialectMap.put(DB.H2, new DialectH2());
		_dialectMap.put(DB.SQLITE, new DialectSqlite());
	}

	public static final Map<DB, Class<? extends Driver>> dialectDriverMap = Collections.unmodifiableMap(_dialectDriverMap);
	public static final Map<DB, String> dialectUrlPrefixMap = Collections.unmodifiableMap(_dialectUrlPrefixMap);
	public static final Map<DB, SqlDialect> dialectMap = Collections.unmodifiableMap(_dialectMap);

	public static final String DB_NAME = "TestsDB";
	public static final String DB_SUFIX = ".db";

	public static final String TABLE_NAME = "TestTab";
	public static final String PK_NAME = "ColPK";

	public static boolean waitForChange() throws InterruptedException {
		Semaphore waitUntilChanged = new Semaphore(0);
		return !waitUntilChanged.tryAcquire(100, TimeUnit.MILLISECONDS);
	}

	public static File createFile(Object test) throws IOException {
		File dbFile = null;
		try {
			dbFile = File.createTempFile(Utility.DB_NAME + test.getClass().getSimpleName(), Utility.DB_SUFIX);
			return dbFile;
		} finally {
			Files.deleteIfExists(dbFile.toPath());
		}
	}

	public static void deleteFile(File dbFile) throws IOException {
		Files.deleteIfExists(dbFile.toPath());
	}

	public static DBConfiguration createDBConfig(File dbFile, DB db) throws IOException, CasketException {

		Class<? extends Driver> driver = dialectDriverMap.getOrDefault(db, org.sqlite.JDBC.class);
		String urlPrefix = Utility.dialectUrlPrefixMap.getOrDefault(db, org.sqlite.JDBC.PREFIX);
		SqlDialect dialect = Utility.dialectMap.getOrDefault(db, new DialectSqlite());

		DBConfiguration config = SqlPort.SQL_PORT.sqlDatabaseFactory().createConfiguration();
		config.setDriver(driver, urlPrefix, dialect);

		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(DBConfiguration.Flag.MODIFY, DBConfiguration.Flag.CREATE);
		return config;
	}

	public static Map<String, SqlColumnSignature> createColumns(String pkColumnName, byte uniqueInitialValue) throws CasketException {

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
		SqlColumnSignature proto = factory.mkColumnSignature(StorageClass.INTEGER, Integer.class, null);
		proto.setFlag(SqlColumnSignature.Flag.PRIMARY_KEY);
		proto.setFlag(SqlColumnSignature.Flag.AUTOINCREMENT);
		columns.put(pkColumnName, proto);

		byte i = 0;
		for (StorageClass type : StorageClass.values()) {
			for (Class<? extends Serializable> clazz : StorageClass.POSSIBLE_CLASS_MAP.get(type.name())) {
				proto = Utility.mkAndInitProtoType(type, clazz, null);
				columns.put(type + "_" + i, proto);
				i++;
			}
		}
		for (StorageClass type : StorageClass.values()) {
			for (Class<? extends Serializable> clazz : StorageClass.POSSIBLE_CLASS_MAP.get(type.name())) {
				proto = Utility.mkAndInitProtoType(type, clazz, (byte) (uniqueInitialValue + i));
				columns.put(type + "_" + i, proto);
				i++;
			}
		}
		return columns;
	}

	public static SqlColumnSignature mkAndInitProtoType(StorageClass type, Class<? extends Serializable> clazz, Byte value) throws CasketException {
		SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();
		if (value == null)
			return factory.mkColumnSignature(type, clazz, null);
		if ((Long.class == clazz))
			return factory.mkColumnSignature(type, Long.class, Long.valueOf(value));
		if ((Long.TYPE == clazz))
			return factory.mkColumnSignature(type, Long.TYPE, Long.valueOf(value));
		if ((Integer.class == clazz))
			return factory.mkColumnSignature(type, Integer.class, Integer.valueOf(value));
		if ((Integer.TYPE == clazz))
			return factory.mkColumnSignature(type, Integer.TYPE, Integer.valueOf(value));
		if ((Short.class == clazz))
			return factory.mkColumnSignature(type, Short.class, Short.valueOf(value));
		if ((Short.TYPE == clazz))
			return factory.mkColumnSignature(type, Short.TYPE, Short.valueOf(value));
		if ((Byte.class == clazz))
			return factory.mkColumnSignature(type, Byte.class, Byte.valueOf(value));
		if ((Byte.TYPE == clazz))
			return factory.mkColumnSignature(type, Byte.TYPE, Byte.valueOf(value));
		if ((Boolean.class == clazz))
			return factory.mkColumnSignature(type, Boolean.class, (value == 0));
		if ((Boolean.TYPE == clazz))
			return factory.mkColumnSignature(type, Boolean.TYPE, (value != 0));
		if (Date.class == clazz)
			return factory.mkColumnSignature(type, Date.class, new Date(value));
		if ((Float.class == clazz))
			return factory.mkColumnSignature(type, Float.class, Float.valueOf(((Number) value).floatValue()));
		if ((Float.TYPE == clazz))
			return factory.mkColumnSignature(type, Float.TYPE, Float.valueOf(((Number) value).floatValue()));
		if ((Double.class == clazz))
			return factory.mkColumnSignature(type, Double.class, Double.valueOf(((Number) value).doubleValue()));
		if ((Double.TYPE == clazz))
			return factory.mkColumnSignature(type, Double.TYPE, Double.valueOf(((Number) value).doubleValue()));
		if ((Character.class == clazz))
			return factory.mkColumnSignature(type, Character.class, ("" + value).charAt(0));
		if ((Character.TYPE == clazz))
			return factory.mkColumnSignature(type, Character.TYPE, ("" + value).charAt(0));
		if (String.class == clazz)
			return factory.mkColumnSignature(type, String.class, ("" + value));

		return factory.mkColumnSignature(type, clazz, null);
	}

	;

}
