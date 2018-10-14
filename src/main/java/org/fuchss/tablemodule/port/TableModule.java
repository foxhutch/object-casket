package org.fuchss.tablemodule.port;

public interface TableModule {

	TablePrototype mkTablePrototype(String tableName) throws TableModuleException;

	Table assignOrCreateTable(TablePrototype table, Boolean assign) throws TableModuleException;

	Table getTable(String tableName) throws TableModuleException;

	Transaction beginTransaction();

	void rollback(Transaction transaction) throws TableModuleException;

	void commit(Transaction transaction) throws TableModuleException;

}
