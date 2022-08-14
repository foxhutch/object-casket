package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlBlob;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlInteger;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlReal;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlText;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * The implementation of the {@link SqlObjectFactory}.
 *
 */
public class SqlObjectFatoryImpl implements SqlObjectFactory {

	private static Map<StorageClass, Function<Object, SqlObj>> fromJava = new EnumMap<>(StorageClass.class);
	static {
		SqlObjectFatoryImpl.fromJava.put(StorageClass.INTEGER, SqlInteger::mkSqlObjectFromJava);
		SqlObjectFatoryImpl.fromJava.put(StorageClass.REAL, SqlReal::mkSqlObjectFromJava);
		SqlObjectFatoryImpl.fromJava.put(StorageClass.TEXT, SqlText::mkSqlObjectFromJava);
		SqlObjectFatoryImpl.fromJava.put(StorageClass.BLOB, SqlBlob::mkSqlObjectFromJava);
	}

	private static Map<StorageClass, Function<Object, SqlObj>> fromSql = new EnumMap<>(StorageClass.class);
	static {
		SqlObjectFatoryImpl.fromSql.put(StorageClass.INTEGER, SqlInteger::mkSqlObjectFromSQL);
		SqlObjectFatoryImpl.fromSql.put(StorageClass.REAL, SqlReal::mkSqlObjectFromSQL);
		SqlObjectFatoryImpl.fromSql.put(StorageClass.TEXT, SqlText::mkSqlObjectFromSQL);
		SqlObjectFatoryImpl.fromSql.put(StorageClass.BLOB, SqlBlob::mkSqlObjectFromSQL);
	}

	@Override
	public <T extends Serializable> SqlColumnSignature mkColumnSignature(StorageClass type, Class<T> javaType, T defaultValue) throws CasketException {
		SqlObj obj = this.mkSqlObject(type, defaultValue);
		return new SqlColumnSignatureImpl(type, javaType, obj);
	}

	@Override
	public <T extends Serializable> SqlObj mkSqlObject(StorageClass type, T obj) throws CasketException {
		Function<Object, SqlObj> fctFromJava = SqlObjectFatoryImpl.fromJava.get(type);
		SqlObj result = fctFromJava.apply(obj);
		if (result == null)
			throw CasketError.INCOMPATIBLE_TYPES.build();
		return result;
	}

	SqlObj mkSqlObjectFromSQL(StorageClass type, Object obj) throws CasketException {
		Function<Object, SqlObj> fctFromSql = SqlObjectFatoryImpl.fromSql.get(type);
		SqlObj result = fctFromSql.apply(obj);
		if (result == null)
			throw CasketError.INCOMPATIBLE_TYPES.build();
		return result;

	}

	@Override
	public SqlObject duplicate(SqlObject sqlObj) {
		return ((SqlObj) sqlObj).duplicate();
	}

}
