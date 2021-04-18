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

	public static final Map<String, SqlObject.Type> sqliteStringToTypeMap = new HashMap<>() {
		private static final long serialVersionUID = 1L;
		{
			this.put("INTEGER", SqlObject.Type.INTEGER);
			this.put("BOOL", SqlObject.Type.BOOL);
			this.put("REAL", SqlObject.Type.REAL);
			this.put("DOUBLE", SqlObject.Type.DOUBLE);
			this.put("FLOAT", SqlObject.Type.FLOAT);
			this.put("CHAR", SqlObject.Type.CHAR);
			this.put("TEXT", SqlObject.Type.TEXT);
			this.put("VARCHAR", SqlObject.Type.VARCHAR);
			this.put("NUMERIC", SqlObject.Type.NUMERIC);
			this.put("DATE", SqlObject.Type.DATE);
			this.put("TIMESTAMP", SqlObject.Type.TIMESTAMP);
			this.put("DATETIME", SqlObject.Type.TIMESTAMP);
		}
	};

}
