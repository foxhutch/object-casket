package org.fuchss.tablemodule.port;

import java.util.List;

public interface Table {

	RowPrototype mkRowPrototype();

	List<Row> allRows() throws TableModuleException;

	List<Row> allRowsByPrototype(RowPrototype prototype) throws TableModuleException;

	Row mkRow(Transaction transaction) throws TableModuleException;

	void delete(Transaction transaction, Row row) throws TableModuleException;

	String getPkName();

}
