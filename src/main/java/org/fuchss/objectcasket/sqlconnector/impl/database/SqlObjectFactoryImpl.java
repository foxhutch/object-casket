package org.fuchss.objectcasket.sqlconnector.impl.database;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.*;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The implementation of the {@link SqlObjectFactory}.
 */
public class SqlObjectFactoryImpl implements SqlObjectFactory {

	private static final Map<StorageClass, Function<Object, SqlObj>> fromJava = new EnumMap<>(StorageClass.class);

	static {
		SqlObjectFactoryImpl.fromJava.put(StorageClass.INTEGER, SqlInteger::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.REAL, SqlReal::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.TEXT, SqlText::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.BLOB, SqlBlob::mkSqlObjectFromJava);
	}

	private static final Map<StorageClass, Function<Object, SqlObj>> fromSql = new EnumMap<>(StorageClass.class);

	static {
		SqlObjectFactoryImpl.fromSql.put(StorageClass.INTEGER, SqlInteger::mkSqlObjectFromSQL);
		SqlObjectFactoryImpl.fromSql.put(StorageClass.REAL, SqlReal::mkSqlObjectFromSQL);
		SqlObjectFactoryImpl.fromSql.put(StorageClass.TEXT, SqlText::mkSqlObjectFromSQL);
		SqlObjectFactoryImpl.fromSql.put(StorageClass.BLOB, SqlBlob::mkSqlObjectFromSQL);
	}

	@Override
	public <T extends Serializable> SqlColumnSignature mkColumnSignature(StorageClass type, Class<T> javaType, T defaultValue) throws CasketException {
		SqlObj obj = this.mkSqlObject(type, defaultValue);
		return new SqlColumnSignatureImpl(type, javaType, obj);
	}

	@Override
	public <T extends Serializable> SqlObj mkSqlObject(StorageClass type, T obj) throws CasketException {
		Function<Object, SqlObj> fctFromJava = SqlObjectFactoryImpl.fromJava.get(type);
		SqlObj result = fctFromJava.apply(obj);
		if (result == null)
			throw CasketError.INCOMPATIBLE_TYPES.build();
		return result;
	}

	SqlObj mkSqlObjectFromSQL(StorageClass type, Object obj) throws CasketException {
		Function<Object, SqlObj> fctFromSql = SqlObjectFactoryImpl.fromSql.get(type);
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
