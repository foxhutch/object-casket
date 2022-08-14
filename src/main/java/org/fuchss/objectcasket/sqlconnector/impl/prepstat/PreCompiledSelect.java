package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;

/**
 * The {@link PreCompiledStatement} to select rows inside the database table.
 *
 */
public class PreCompiledSelect extends PreCompiledArgStmt {

	/**
	 * The constructor.
	 *
	 * @param prepStat
	 *            - the used {@link PreparedStatement} to select rows.
	 * @param tableName
	 *            - the assigned table.
	 * @param argList
	 *            - all arguments to use.
	 * @param colSigMap
	 *            - the signatures.
	 * @throws CasketException
	 *             on error.
	 */

	public PreCompiledSelect(PreparedStatement prepStat, String tableName, List<SqlArg> argList, Map<String, SqlColumnSignatureImpl> colSigMap) throws CasketException {
		super(prepStat, tableName, argList, colSigMap);

	}

	@Override
	protected ResultSet execute() throws CasketException {
		try {
			int idx = 1;
			for (SqlArg arg : this.args) {
				this.columnArgs.get(arg).getValue().prepareStatement(idx++, this.prepStat);
			}
			return this.prepStat.executeQuery();
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.columnArgs.values().forEach(SqlColumnSignatureImpl::clear);
		}
	}

}