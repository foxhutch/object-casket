package org.fuchss.sqlconnector.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.sqlconnector.impl.object.SqlPrototypeImpl;
import org.fuchss.sqlconnector.impl.object.SqlPrototypeValidator;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlPrototypeValidatorImpl implements SqlPrototypeValidator {

	public static final Map<String, SqlObject.Type> sqliteStringToTypeMap = new HashMap<>();
	static {
		sqliteStringToTypeMap.put("INTEGER", SqlObject.Type.INTEGER);
		sqliteStringToTypeMap.put("BOOL", SqlObject.Type.BOOL);
		sqliteStringToTypeMap.put("REAL", SqlObject.Type.REAL);
		sqliteStringToTypeMap.put("DOUBLE", SqlObject.Type.DOUBLE);
		sqliteStringToTypeMap.put("FLOAT", SqlObject.Type.FLOAT);
		sqliteStringToTypeMap.put("CHAR", SqlObject.Type.CHAR);
		sqliteStringToTypeMap.put("TEXT", SqlObject.Type.TEXT);
		sqliteStringToTypeMap.put("VARCHAR", SqlObject.Type.VARCHAR);
		sqliteStringToTypeMap.put("NUMERIC", SqlObject.Type.NUMERIC);
		sqliteStringToTypeMap.put("DATE", SqlObject.Type.DATE);
		sqliteStringToTypeMap.put("TIMESTAMP", SqlObject.Type.TIMESTAMP);
		sqliteStringToTypeMap.put("DATETIME", SqlObject.Type.TIMESTAMP);
	}

	private ResultSet resultSet;
	private boolean isPk;
	private boolean isNotNull;
	private SqlObject.Type type;
	private SqlPrototypeImpl sqlPrototypeImpl;
	private Exception exception = null;
	private boolean isValid = true;

	SqlPrototypeValidatorImpl(ResultSet resultSet) throws SQLException {
		this.resultSet = resultSet;
		this.isNotNull = resultSet.getBoolean(4);
		this.isPk = resultSet.getBoolean(6);
		String typeString = resultSet.getString(3);
		this.type = SqlPrototypeValidatorImpl.sqliteStringToTypeMap.get(typeString);
	}

	@Override
	public void setPrototypeAndValidate(SqlPrototypeImpl sqlPrototypeImpl) {
		this.sqlPrototypeImpl = sqlPrototypeImpl;
		this.checkFlags();
		this.checkType();
	}

	@Override
	public boolean validateByInt(SqlObject sqlObj) {
		try {
			int val = this.resultSet.getInt(5);
			this.isValid &= sqlObj.compareTo(val) == 0;
			return this.isValid;
		} catch (ConnectorException | SQLException e) {
			this.exception = e;
		}
		return false;
	}

	@Override
	public boolean validateByBOOL(SqlObject sqlObj) {
		try {
			boolean val = this.resultSet.getBoolean(5);
			this.isValid &= sqlObj.compareTo(val) == 0;
			return this.isValid;
		} catch (ConnectorException | SQLException e) {
			this.exception = e;
		}
		return false;
	}

	@Override
	public boolean validateByDouble(SqlObject sqlObj) {
		try {
			double val = this.resultSet.getDouble(5);
			this.isValid &= sqlObj.compareTo(val) == 0;
			return this.isValid;
		} catch (ConnectorException | SQLException e) {
			this.exception = e;
		}
		return false;
	}

	@Override
	public boolean validateByString(SqlObject sqlObj) {
		try {
			String val = this.resultSet.getString(5);
			this.isValid &= sqlObj.compareTo(val) == 0;
			return this.isValid;
		} catch (ConnectorException | SQLException e) {
			this.exception = e;
		}
		return false;

	}

	@Override
	public boolean isValid() throws ConnectorException {
		if (this.exception != null) {
			ConnectorException.build(this.exception);
		}
		return this.isValid;
	}

	private void checkFlags() {
		this.isValid &= this.isPk == this.sqlPrototypeImpl.isPrimaryKey();
		this.isValid &= this.isNotNull == this.sqlPrototypeImpl.isNotNull();
	}

	private void checkType() {
		if (this.type == SqlObject.Type.TIMESTAMP) {
			this.isValid &= (this.sqlPrototypeImpl.getType() == this.type) || (this.sqlPrototypeImpl.getType() == SqlObject.Type.DATE);
		} else {
			this.isValid &= this.sqlPrototypeImpl.getType() == this.type;
		}
	}

}
