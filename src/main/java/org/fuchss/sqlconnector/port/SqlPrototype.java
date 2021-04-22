package org.fuchss.sqlconnector.port;

public interface SqlPrototype {

	void setFlag(Flag flag);

	void resetFlag(Flag flag);

	void setType(SqlObject.Type type, Class<?> javaType);

	SqlObject.Type getType();

	Class<?> getJavaType();

	boolean isAutoIncrementedPrimaryKey();

	public static enum Flag {
		PRIMARY_KEY, AUTOINCREMENT, NOT_NULL, UNIQUE
	}

}
