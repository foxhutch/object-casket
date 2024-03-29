package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlLong;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect.MetaTag;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;

/**
 * The {@link PreCompiledStatement} to create a new row inside the database
 * table.
 */
public class PreCompiledCreate extends PreCompiledStmtImpl {

	private final boolean isAutoIncrement;
	private final SqlDialect dialect;

	/**
	 * The constructor.
	 *
	 * @param prepStat
	 *            - the used {@link PreparedStatement}.
	 * @param table
	 *            - the assigned table.
	 * @param columnNames
	 *            - all columns in the right order.
	 * @param protoMap
	 *            - the signatures.
	 * @param isAutoIncrement
	 *            - indicates whether the primary key will be generated.
	 * @param theDialect
	 *            - the SQL dialect to retrieve dialect specific meta informations.
	 *
	 * @throws CasketException
	 *             on error.
	 */
	public PreCompiledCreate(PreparedStatement prepStat, String table, List<String> columnNames, Map<String, SqlColumnSignatureImpl> protoMap, boolean isAutoIncrement, SqlDialect theDialect) throws CasketException {
		super(prepStat, table, columnNames, protoMap);
		this.dialect = theDialect;
		this.pkColumnName = this.getPkName();
		this.isAutoIncrement = isAutoIncrement;
	}

	/**
	 * This operation is used to execute a pre-compiled statement which needs
	 * values.
	 *
	 * @param values
	 *            - the values to use.
	 * @throws CasketException
	 *             on error.
	 */
	public void setValuesAndExecute(Map<String, SqlObject> values) throws CasketException {
		super.setValues(values);
		SqlObj generatedKey = this.execute();
		if (generatedKey != null)
			values.put(this.pkColumnName, generatedKey);
	}

	private SqlObj execute() throws CasketException {
		try {
			int idx = 1;
			for (String column : this.columnNames) {
				if (column.equals(this.pkColumnName) && this.isAutoIncrement)
					continue;
				this.columns.get(column).getValue().prepareStatement(idx++, this.prepStat);
			}
			this.prepStat.executeUpdate();
			if (this.isAutoIncrement)
				return SqlLong.mkSqlObjectFromJava(this.lastGeneratedKey());
			return null;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.columns.values().forEach(SqlColumnSignatureImpl::clear);
		}
	}

	private Long lastGeneratedKey() throws SQLException {
		String genKeyQuery = this.dialect.getMetaQuery(MetaTag.LAST_INSERT_ID);
		if (genKeyQuery == null)
			/* try JDBC getGeneratedKeys */
			try (ResultSet generatedKeys = this.prepStat.getGeneratedKeys()) {
				return generatedKeys.next() ? Long.valueOf(generatedKeys.getInt(1)) : 0L;
			}
		try (PreparedStatement generatedKeysStatement = this.prepStat.getConnection().prepareStatement(this.dialect.getMetaQuery(MetaTag.LAST_INSERT_ID)); //
				ResultSet generatedKeys = generatedKeysStatement.executeQuery();) {
			return generatedKeys.next() ? Long.valueOf(generatedKeys.getInt(1)) : 0L;
		}
	}

}
