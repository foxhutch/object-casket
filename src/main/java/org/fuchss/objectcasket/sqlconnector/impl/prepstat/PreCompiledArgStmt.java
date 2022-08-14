package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

abstract class PreCompiledArgStmt extends PreCompiledStmtImpl {

	protected PreCompiledArgStmt(PreparedStatement prepStat, String tableName, List<SqlArg> argList, Map<String, SqlColumnSignatureImpl> colSigMap) throws CasketException {
		super(prepStat, tableName, new ArrayList<>(colSigMap.keySet()), colSigMap);
		this.args = argList;
		for (SqlArg arg : this.args)
			this.columnArgs.put(arg, new SqlColumnSignatureImpl(colSigMap.get(arg.columnName())));
		this.pkColumnName = this.getPkName();
	}

	/**
	 * The list of arguments.
	 */
	protected List<SqlArg> args;

	/**
	 * Arguments and signatures.
	 */
	protected Map<SqlArg, SqlColumnSignatureImpl> columnArgs = new HashMap<>();

	/**
	 * This operation is used to execute a pre-compiled statement which needs
	 * arguments.
	 *
	 * @param values - {@link SqlArg arguments} and their values.
	 * @return the {@link ResultSet}.
	 * @throws CasketException on error.
	 */
	public ResultSet setValuesAndExecute(Map<SqlArg, SqlObject> values) throws CasketException {
		this.setArgs(values);
		return this.execute();
	}

	/**
	 * This operation is used to execute a pre-compiled statement without arguments.
	 *
	 * @return the {@link ResultSet}.
	 * @throws CasketException on error.
	 */
	protected abstract ResultSet execute() throws CasketException;

	void setArgs(Map<SqlArg, SqlObject> values) throws CasketException {
		this.checkArgs(values);
		this.columnArgs.values().forEach(SqlColumnSignatureImpl::clear);
		this.exc = null;
		values.forEach((arg, obj) -> this.updateProtoType(this.columnArgs.get(arg), obj));
		if (this.exc != null) { // exc maybe set during upDateProtoType
			this.columnArgs.values().forEach(SqlColumnSignatureImpl::clear);
			throw this.exc;
		}
	}

	private void checkArgs(Map<SqlArg, SqlObject> values) throws CasketException {
		Set<SqlArg> missingArgs = new HashSet<>(values.keySet());
		Set<SqlArg> neededArgs = new HashSet<>(this.args);
		missingArgs.removeIf(arg -> this.args.contains(arg));
		neededArgs.removeIf(values::containsKey);
		if (missingArgs.isEmpty() && neededArgs.isEmpty())
			return;
		throw CasketError.INVALID_ARGUMENTS.build();
	}

}
