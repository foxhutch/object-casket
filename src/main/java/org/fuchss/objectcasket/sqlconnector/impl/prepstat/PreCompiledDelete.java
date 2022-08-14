package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;

/**
 * The {@link PreCompiledStatement} to delete rows inside the database table.
 *
 */
public class PreCompiledDelete extends PreCompiledArgStmt {

	/**
	 * The necessary {@link PreparedStatement} to select the rows which should be
	 * deleted.
	 */
	protected PreparedStatement prepSelectStat;

	/**
	 * The constructor.
	 *
	 * @param prepStat
	 *            - the used {@link PreparedStatement} to delete rows.
	 * @param prepSelectStat
	 *            - the necessary {@link PreparedStatement} to select the rows which
	 *            should be deleted.
	 * @param tableName
	 *            - the assigned table.
	 * @param argList
	 *            - all arguments to use.
	 * @param colSigMap
	 *            - the signatures.
	 * @throws CasketException
	 *             on error.
	 */
	public PreCompiledDelete(PreparedStatement prepStat, PreparedStatement prepSelectStat, String tableName, List<SqlArg> argList, Map<String, SqlColumnSignatureImpl> colSigMap) throws CasketException {
		super(prepStat, tableName, argList, colSigMap);
		this.prepSelectStat = prepSelectStat;
	}

	@Override
	protected ResultSet execute() throws CasketException {
		try {
			int idx = 1;
			for (SqlArg arg : this.args) {
				this.columnArgs.get(arg).getValue().prepareStatement(idx++, this.prepSelectStat);
			}
			ResultSet result = this.prepSelectStat.executeQuery();

			idx = 1;
			for (SqlArg arg : this.args) {
				this.columnArgs.get(arg).getValue().prepareStatement(idx++, this.prepStat);
			}
			this.prepStat.executeUpdate();
			return result;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.columnArgs.values().forEach(SqlColumnSignatureImpl::clear);
		}
	}

	@Override
	public void close() throws CasketException {
		try {
			this.prepSelectStat.close();
			super.close();
		} catch (SQLException e) {
			throw CasketError.ALREADY_CLOSED.build();
		}
	}

}
