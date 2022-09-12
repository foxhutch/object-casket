package org.fuchss.objectcasket.sqlconnector.port;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;

/**
 * An implementation of the {@link SqlDialect} interface that works with H2
 * databases version 2.1.214.
 */
public class DialectH2 implements SqlDialect {

	private static final Map<StorageClass, String> stClassMap = new EnumMap<>(StorageClass.class);

	static {
		DialectH2.stClassMap.put(StorageClass.LONG, "BIGINT");
		DialectH2.stClassMap.put(StorageClass.DOUBLE, "DOUBLE PRECISION");
		DialectH2.stClassMap.put(StorageClass.TEXT, "VARCHAR(1000000000)");
		DialectH2.stClassMap.put(StorageClass.BLOB, "VARBINARY(1000000000)");
	}

	private static final Map<String, StorageClass> baseType2StClassMap = new HashMap<>();

	static {
		DialectH2.baseType2StClassMap.put("BIGINT", StorageClass.LONG);
		DialectH2.baseType2StClassMap.put("DOUBLE PRECISION", StorageClass.DOUBLE);
		DialectH2.baseType2StClassMap.put("CHARACTER VARYING", StorageClass.TEXT);
		DialectH2.baseType2StClassMap.put("BINARY VARYING", StorageClass.BLOB);
	}

	private static final Map<CMP, String> cmpMap = new EnumMap<>(CMP.class);

	static {
		DialectH2.cmpMap.put(CMP.LESS, "<");
		DialectH2.cmpMap.put(CMP.GREATER, ">");
		DialectH2.cmpMap.put(CMP.EQUAL, "=");
		DialectH2.cmpMap.put(CMP.LESSEQ, "<=");
		DialectH2.cmpMap.put(CMP.GREATEREQ, ">=");
		DialectH2.cmpMap.put(CMP.UNEQUAL, "<>");
	}

	private static final Map<OP, String> opMap = new EnumMap<>(OP.class);

	static {
		DialectH2.opMap.put(OP.AND, " AND ");
		DialectH2.opMap.put(OP.OR, " OR ");
	}

	private static final Map<Flag, String> flagMap = new EnumMap<>(Flag.class);

	static {
		DialectH2.flagMap.put(Flag.PRIMARY_KEY, "PRIMARY KEY");
		DialectH2.flagMap.put(Flag.AUTOINCREMENT, "AUTO_INCREMENT");
		DialectH2.flagMap.put(Flag.NOT_NULL, "NOT NULL");
	}

	@Override
	public String cmpString(CMP cmp) {
		return DialectH2.cmpMap.get(cmp);
	}

	@Override
	public String operatorString(OP op) {
		return DialectH2.opMap.get(op);
	}

	@Override
	public String storageClassString(StorageClass stClass) {
		return DialectH2.stClassMap.get(stClass);
	}

	@Override
	public String flagString(Flag flag) {
		return DialectH2.flagMap.get(flag);
	}

	@Override
	public StorageClass baseTypeStorageClass(String typeName) {
		return DialectH2.baseType2StClassMap.get(typeName);
	}

}
