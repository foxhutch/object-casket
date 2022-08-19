package org.fuchss.objectcasket.sqlconnector.impl.objects;

import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * The implementation of the {@link SqlArg}.
 */
@SuppressWarnings("java:S6206") // identity not equality is needed!
public class SqlArgImpl implements SqlArg {

	private final String tableName;
	private final String columnName;
	private final SqlColumnSignatureImpl proto;
	private final CMP cmp;

	/**
	 * The constructor.
	 *
	 * @param tableName
	 *            - the assigned table.
	 * @param columnName
	 *            - the assigned column.
	 * @param proto
	 *            - the signature of the column.
	 * @param cmp
	 *            - the comparator to use.
	 */
	public SqlArgImpl(String tableName, String columnName, SqlColumnSignatureImpl proto, CMP cmp) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.proto = proto;
		this.cmp = cmp;
	}

	@Override
	public String tableName() {
		return this.tableName;
	}

	@Override
	public String columnName() {
		return this.columnName;
	}

	@Override
	public StorageClass storageClass() {
		return this.proto.getType();
	}

	@Override
	public CMP cmp() {
		return this.cmp;
	}

	/**
	 * This operation returns the signature of the assign column.
	 *
	 * @return the signature of the assigned column.
	 */
	public SqlColumnSignatureImpl proto() {
		return this.proto;
	}

}
