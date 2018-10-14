package org.fuchss.tablemodule.port;

import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObjectFactory;

public interface TableModuleBuilder {

	TableModule tableModule(SqlDatabase database, SqlObjectFactory objectFactory);

}
