package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the {@link SqlDialect} interface that works with SQLite
 * databases version 3.36.0.
 */
public class DialectSqlite implements SqlDialect {

	private static final Map<StorageClass, String> stClassMap = new EnumMap<>(StorageClass.class);

	static {
		DialectSqlite.stClassMap.put(StorageClass.INTEGER, "INTEGER");
		DialectSqlite.stClassMap.put(StorageClass.REAL, "REAL");
		DialectSqlite.stClassMap.put(StorageClass.TEXT, "TEXT");
		DialectSqlite.stClassMap.put(StorageClass.BLOB, "BLOB");
	}

	private static final Map<String, StorageClass> baseType2StClassMap = new HashMap<>();

	static {
		DialectSqlite.baseType2StClassMap.put("INTEGER", StorageClass.INTEGER);
		DialectSqlite.baseType2StClassMap.put("REAL", StorageClass.REAL);
		DialectSqlite.baseType2StClassMap.put("TEXT", StorageClass.TEXT);
		DialectSqlite.baseType2StClassMap.put("BLOB", StorageClass.BLOB);
	}

	private static final Map<CMP, String> cmpMap = new EnumMap<>(CMP.class);

	static {
		DialectSqlite.cmpMap.put(CMP.LESS, "<");
		DialectSqlite.cmpMap.put(CMP.GREATER, ">");
		DialectSqlite.cmpMap.put(CMP.EQUAL, "=");
		DialectSqlite.cmpMap.put(CMP.LESSEQ, "<=");
		DialectSqlite.cmpMap.put(CMP.GREATEREQ, ">=");
		DialectSqlite.cmpMap.put(CMP.UNEQUAL, "<>");
	}

	private static final Map<OP, String> opMap = new EnumMap<>(OP.class);

	static {
		DialectSqlite.opMap.put(OP.AND, " AND ");
		DialectSqlite.opMap.put(OP.OR, " OR ");
	}

	private static final Map<Flag, String> flagMap = new EnumMap<>(Flag.class);

	static {
		DialectSqlite.flagMap.put(Flag.PRIMARY_KEY, "PRIMARY KEY");
		DialectSqlite.flagMap.put(Flag.AUTOINCREMENT, "AUTOINCREMENT");
		DialectSqlite.flagMap.put(Flag.NOT_NULL, "NOT NULL");
	}

	@Override
	public String cmpString(CMP cmp) {
		return DialectSqlite.cmpMap.get(cmp);
	}

	@Override
	public String operatorString(OP op) {
		return DialectSqlite.opMap.get(op);
	}

	@Override
	public String storageClassString(StorageClass stClass) {
		return DialectSqlite.stClassMap.get(stClass);
	}

	@Override
	public String flagString(Flag flag) {
		return DialectSqlite.flagMap.get(flag);
	}

	@Override
	public StorageClass baseTypeStorageClass(String typeName) {
		return DialectSqlite.baseType2StClassMap.get(typeName);
	}

}