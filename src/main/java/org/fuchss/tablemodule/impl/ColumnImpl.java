package org.fuchss.tablemodule.impl;

import java.util.Set;

import org.fuchss.sqlconnector.port.SqlObject.Type;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.port.TableModuleException;

public class ColumnImpl<T> {

	private Set<SqlPrototype.Flag> flags;
	private String columnName;
	private Class<T> type;
	private Type sqlType;

	public ColumnImpl(String columnName, Class<T> type, Type sqlType, Set<SqlPrototype.Flag> flags) {
		this.columnName = columnName;
		this.type = type;
		this.sqlType = (sqlType == null) ? Type.getDefaultType(type) : sqlType;
		this.flags = flags;
	}

	public SqlPrototype mkPrototype(SqlObjectFactory sqlObjectFactory) throws TableModuleException {
		SqlPrototype prototype = sqlObjectFactory.mkPrototype();
		prototype.setType(((this.sqlType == null) ? Type.getDefaultType(this.type) : this.sqlType), this.type);
		this.flags.forEach(prototype::setFlag);
		return prototype;
	}

	public String columnName() {
		return this.columnName;
	}

}
