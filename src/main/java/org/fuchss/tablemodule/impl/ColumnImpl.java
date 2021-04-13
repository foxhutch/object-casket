package org.fuchss.tablemodule.impl;

import java.util.Set;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject.Type;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.port.TableModuleException;

public class ColumnImpl<T> {

	private Set<SqlPrototype.Flag> flags;
	private String columnName;
	private Class<T> type;
	private Type sqlType;
	private T defaultValue;

	public ColumnImpl(String columnName, Class<T> type, Type sqlType, Set<SqlPrototype.Flag> flags, T defaultVal) {
		this.columnName = columnName;
		this.type = type;
		this.sqlType = (sqlType == null) ? Type.getDefaultType(type, (flags == null) ? false : flags.contains(SqlPrototype.Flag.PRIMARY_KEY)) : sqlType;
		this.flags = flags;
		this.defaultValue = defaultVal;
	}

	public SqlPrototype mkPrototype(SqlObjectFactory sqlObjectFactory, boolean isPk) throws TableModuleException {
		SqlPrototype prototype = sqlObjectFactory.mkPrototype();
		try {
			prototype.setType((this.sqlType == null) ? Type.getDefaultType(this.type, isPk) : this.sqlType);
			this.flags.forEach(prototype::setFlag);
			if (this.defaultValue != null) {
				prototype.setDefault(sqlObjectFactory.mkSqlObject(this.sqlType, this.defaultValue));
			}
		} catch (ConnectorException e) {
			TableModuleException.build(e);
		}
		return prototype;
	}

	public String columnName() {
		return this.columnName;
	}

}
