package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The {@link PreCompiledStatement} to update a row inside the database table.
 */
public class PreCompiledUpdate extends PreCompiledStmtImpl {

	/**
	 * The constructor.
	 *
	 * @param prepStat    - the used {@link PreparedStatement} to update a row.
	 * @param table       - the assigned table.
	 * @param pkName      - the name of the primary key column.
	 * @param columnNames - all columns in the right order.
	 * @param protoMap    - the signatures.
	 */
	public PreCompiledUpdate(PreparedStatement prepStat, String table, String pkName, List<String> columnNames, Map<String, SqlColumnSignatureImpl> protoMap) {
		super(prepStat, table, columnNames, protoMap);
		this.columns.put(pkName, new SqlColumnSignatureImpl(protoMap.get(pkName)));
		this.pkColumnName = pkName;
	}

	/**
	 * This operation applies the statement with the provided values.
	 *
	 * @param values - the values to use.
	 * @param pk     - the value of the primary key to identify the row.
	 * @throws CasketException on error.
	 */
	public void setValuesAndExecute(Map<String, SqlObject> values, SqlObject pk) throws CasketException {
		Util.objectsNotNull(pk);
		super.setValues(values);
		this.updateProtoType(this.columns.get(this.pkColumnName), pk); // check if pk-object is correct
		this.execute();
	}

	private void execute() throws CasketException {
		try {
			int idx = 1;
			for (String column : this.columnNames) {
				this.columns.get(column).getValue().prepareStatement(idx++, this.prepStat);
			}
			this.columns.get(this.pkColumnName).getValue().prepareStatement(idx, this.prepStat);
			this.prepStat.executeUpdate();
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.columns.values().forEach(SqlColumnSignatureImpl::clear);
		}

	}

}
