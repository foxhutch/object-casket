package org.fuchss.tablemodule.port;

import org.fuchss.sqlconnector.port.SqlArg;

public interface RowPrototype {

	void set(String column, Object value, SqlArg.CMP cmp) throws TableModuleException;
}
