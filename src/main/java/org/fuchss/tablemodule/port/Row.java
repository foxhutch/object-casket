package org.fuchss.tablemodule.port;

import java.lang.reflect.Field;

public interface Row {

	default <T> T read(Transaction transaction, String column, Class<T> type) throws TableModuleException {
		return this.read(transaction, column, type, null);
	}

	<T> T read(Transaction transaction, String column, Class<T> type, Field target) throws TableModuleException;

	<T> T getPK(Transaction transaction, Class<T> type) throws TableModuleException;

	void write(Transaction transaction, String column, Object value) throws TableModuleException;

}
