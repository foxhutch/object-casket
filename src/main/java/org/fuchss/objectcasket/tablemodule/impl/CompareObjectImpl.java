package org.fuchss.objectcasket.tablemodule.impl;

import java.util.EnumMap;
import java.util.Map;

import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.tablemodule.port.Table;

class CompareObjectImpl {

	static Map<Table.TabCMP, SqlArg.CMP> map = new EnumMap<>(Table.TabCMP.class);
	static {
		map.put(Table.TabCMP.LESS, SqlArg.CMP.LESS);
		map.put(Table.TabCMP.GREATER, SqlArg.CMP.GREATER);
		map.put(Table.TabCMP.EQUAL, SqlArg.CMP.EQUAL);
		map.put(Table.TabCMP.LESSEQ, SqlArg.CMP.LESSEQ);
		map.put(Table.TabCMP.GREATEREQ, SqlArg.CMP.GREATEREQ);
		map.put(Table.TabCMP.UNEQUAL, SqlArg.CMP.UNEQUAL);
	}

	SqlObject sqlObj;
	SqlArg sqlArg;

	TableImpl table;
	String column;

	CompareObjectImpl(TableImpl table, String column, SqlArg sqlArg, SqlObject sqlObj) {
		this.table = table;
		this.column = column;
		this.sqlArg = sqlArg;
		this.sqlObj = sqlObj;
	}

}
