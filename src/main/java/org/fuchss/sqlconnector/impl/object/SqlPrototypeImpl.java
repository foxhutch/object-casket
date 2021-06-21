package org.fuchss.sqlconnector.impl.object;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class SqlPrototypeImpl implements SqlPrototype {

	private SqlObject.Type type;
	private Class<?> javaType;

	private List<Flag> flags = new ArrayList<>();
	private SqlObject defaultValue;

	@Override
	public void setFlag(Flag flag) {
		if (this.flags.contains(flag)) {
			return;
		}
		this.flags.add(flag);
	}

	@Override
	public void resetFlag(Flag flag) {
		this.flags.remove(flag);
		return;
	}

	@Override
	public void setType(SqlObject.Type type, Class<?> javaType) {
		this.type = type;
		this.javaType = javaType;
	}

	@Override
	public SqlObject.Type getType() {
		return this.type;
	}

	@Override
	public boolean isAutoIncrementedPrimaryKey() {
		boolean res = SqlObject.Type.AUTOINCREMENT_SQL_TYPES.contains(this.type);
		res &= this.flags.contains(Flag.AUTOINCREMENT);
		res &= this.flags.contains(Flag.PRIMARY_KEY);
		return res;
	}

	public boolean isPrimaryKey() {
		return this.flags.contains(Flag.PRIMARY_KEY);
	}

	public boolean isNotNull() {
		return this.flags.contains(Flag.NOT_NULL);
	}

	public String toSqlSubString(String columnName) {
		String sql = "\"" + columnName + "\"";
		sql += " " + this.type.name();

		if (this.flags.contains(Flag.PRIMARY_KEY)) {
			sql += " " + Flag.PRIMARY_KEY.name().replace("_", " ");
		}
		if (this.flags.contains(Flag.AUTOINCREMENT)) {
			sql += " " + Flag.AUTOINCREMENT.name().replace("_", " ");
		}
		if (this.flags.contains(Flag.UNIQUE)) {
			sql += " " + Flag.UNIQUE.name().replace("_", " ");
		}

		for (Flag flag : this.flags) {
			if ((flag != Flag.PRIMARY_KEY) && (flag != Flag.AUTOINCREMENT) && (flag != Flag.UNIQUE)) {
				sql += " " + flag.name().replace("_", " ");
			}
		}
		return sql;
	}

	public void validate(SqlPrototypeValidator validator) throws SQLException {
		validator.setPrototypeAndValidate(this);
		if (this.defaultValue == null) {
			return;
		}
		SqlPrototypeValidator.validationMap.get(this.type).apply(validator, this.defaultValue);
	}

	@Override
	public Class<?> getJavaType() {
		return this.javaType;
	}

}
