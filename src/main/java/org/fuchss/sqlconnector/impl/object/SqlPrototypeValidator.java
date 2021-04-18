package org.fuchss.sqlconnector.impl.object;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public interface SqlPrototypeValidator {

	void setPrototypeAndValidate(SqlPrototypeImpl sqlPrototypeImpl);

	boolean validateByInt(SqlObject sqlObj);

	boolean validateByBOOL(SqlObject sqlObj);

	boolean validateByDouble(SqlObject sqlObj);

	boolean validateByString(SqlObject sqlObj);

	boolean isValid() throws ConnectorException;

	public static final Map<SqlObject.Type, BiFunction<SqlPrototypeValidator, SqlObject, Boolean>> validationMap = new HashMap<SqlObject.Type, BiFunction<SqlPrototypeValidator, SqlObject, Boolean>>() {
		private static final long serialVersionUID = 1L;
		{
			this.put(SqlObject.Type.INTEGER, (validator, sqlObj) -> validator.validateByInt(sqlObj));
			this.put(SqlObject.Type.BOOL, (validator, sqlObj) -> validator.validateByBOOL(sqlObj));
			this.put(SqlObject.Type.REAL, (validator, sqlObj) -> validator.validateByDouble(sqlObj));
			this.put(SqlObject.Type.DOUBLE, (validator, sqlObj) -> validator.validateByDouble(sqlObj));
			this.put(SqlObject.Type.FLOAT, (validator, sqlObj) -> validator.validateByDouble(sqlObj));
			this.put(SqlObject.Type.CHAR, (validator, sqlObj) -> validator.validateByString(sqlObj));
			this.put(SqlObject.Type.TEXT, (validator, sqlObj) -> validator.validateByString(sqlObj));
			this.put(SqlObject.Type.VARCHAR, (validator, sqlObj) -> validator.validateByString(sqlObj));
			this.put(SqlObject.Type.NUMERIC, (validator, sqlObj) -> validator.validateByDouble(sqlObj));
			this.put(SqlObject.Type.DATE, (validator, sqlObj) -> validator.validateByString(sqlObj));
			this.put(SqlObject.Type.TIMESTAMP, (validator, sqlObj) -> validator.validateByString(sqlObj));
		}
	};

}
