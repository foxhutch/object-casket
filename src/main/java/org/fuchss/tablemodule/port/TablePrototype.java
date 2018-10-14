package org.fuchss.tablemodule.port;

import java.util.Set;

import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype.Flag;

public interface TablePrototype {

	<T> void addColumn(String columnName, Class<T> type, SqlObject.Type sqlType, Set<Flag> flags, T defaultVal) throws TableModuleException;

	void removeColumn(String columnName) throws TableModuleException;

	Set<String> getColumnNames();

}
