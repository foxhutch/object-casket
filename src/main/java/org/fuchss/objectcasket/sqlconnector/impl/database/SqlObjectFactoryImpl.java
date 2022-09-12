package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlBlob;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlLong;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlDouble;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlText;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * The implementation of the {@link SqlObjectFactory}.
 */
public class SqlObjectFactoryImpl implements SqlObjectFactory {

	private static final Map<StorageClass, Function<Object, SqlObj>> fromJava = new EnumMap<>(StorageClass.class);

	static {
		SqlObjectFactoryImpl.fromJava.put(StorageClass.LONG, SqlLong::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.DOUBLE, SqlDouble::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.TEXT, SqlText::mkSqlObjectFromJava);
		SqlObjectFactoryImpl.fromJava.put(StorageClass.BLOB, SqlBlob::mkSqlObjectFromJava);
	}

	private static final Map<StorageClass, Function<Object, SqlObj>> fromSql = new EnumMap<>(StorageClass.class);

	static {
		SqlObjectFactoryImpl.fromSql.put(StorageClass.LONG, SqlLong::mkSqlObjectFromSQL);
		SqlObjectFactoryImpl.fromSql.put(StorageClass.DOUBLE, SqlDouble::mkSqlObjectFromSQL);
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
			throw CE3.INCOMPATIBLE_SQL_TYPE.defaultBuild(type, obj, obj.getClass());
		return result;
	}

	SqlObj mkSqlObjectFromSQL(StorageClass type, Object obj) throws CasketException {
		Function<Object, SqlObj> fctFromSql = SqlObjectFactoryImpl.fromSql.get(type);
		SqlObj result = fctFromSql.apply(obj);
		if (result == null)
			throw CE3.INCOMPATIBLE_SQL_TYPE.defaultBuild(type, obj, obj.getClass());
		return result;

	}

	@Override
	public SqlObject duplicate(SqlObject sqlObj) {
		return ((SqlObj) sqlObj).duplicate();
	}

}
