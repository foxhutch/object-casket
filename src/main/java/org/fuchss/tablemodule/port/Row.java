package org.fuchss.tablemodule.port;

public interface Row {

	<T> T read(Transaction transaction, String column, Class<T> type) throws TableModuleException;

	<T> T getPK(Transaction transaction, Class<T> type) throws TableModuleException;

	void write(Transaction transaction, String column, Object value) throws TableModuleException;

}
