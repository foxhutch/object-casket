package org.fuchss.tablemodule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuchss.sqlconnector.port.SqlArg;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.tablemodule.port.RowPrototype;
import org.fuchss.tablemodule.port.TableModuleException;

public class RowPrototypeImpl implements RowPrototype {

	private TableImpl table;

	private Map<String, SqlObject> columnNameValueMap = new HashMap<>();
	private Map<String, SqlArg.CMP> columnCmpMap = new HashMap<>();

	RowPrototypeImpl(TableImpl table) {
		this.table = table;
	}

	@Override
	public void set(String column, Object value, SqlArg.CMP cmp) throws TableModuleException {
		this.columnCmpMap.put(column, cmp);
		this.columnNameValueMap.put(column, this.table.createSqlObject(column, value, false));
	}

	public List<SqlArg> mkArgs() {
		List<SqlArg> args = new ArrayList<>();
		this.columnNameValueMap.forEach((column, sqlObject) -> args.add(new SqlArg(column, this.columnCmpMap.get(column), sqlObject)));
		return args;
	}

}
