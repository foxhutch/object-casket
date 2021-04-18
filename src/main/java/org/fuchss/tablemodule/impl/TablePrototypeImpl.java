package org.fuchss.tablemodule.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.TablePrototype;

public class TablePrototypeImpl implements TablePrototype {

	private String name;

	private Map<String, ColumnImpl<?>> columnMap = new HashMap<>();
	private ColumnImpl<?> pk;

	public TablePrototypeImpl(String name) {
		this.name = name;
	}

	public ColumnImpl<?> getColumnForName(String columnName) {
		return this.columnMap.get(columnName);
	}

	@Override
	public Set<String> getColumnNames() {
		return new HashSet<>(this.columnMap.keySet());
	}

	public String getTableName() {
		return this.name;
	}

	public String getPrimeryKey() {
		return (this.pk == null) ? null : this.pk.columnName();
	}

	@Override
	public <T> void addColumn(String columnName, Class<T> type, SqlObject.Type sqlType, Set<SqlPrototype.Flag> flags) throws TableModuleException {
		this.checkColumnName(columnName);
		SqlObject.Type calculatedSqlType = this.checkType(type, sqlType);
		Set<SqlPrototype.Flag> calculatedFlags = this.checkFlags(flags, calculatedSqlType);
		ColumnImpl<T> col = new ColumnImpl<>(columnName, type, sqlType, calculatedFlags);
		if (calculatedFlags.contains(SqlPrototype.Flag.PRIMARY_KEY)) {
			this.pk = col;
		}
		this.columnMap.put(columnName, col);
	}

	private void checkColumnName(String columnName) throws TableModuleException {
		if (this.getColumnForName(columnName) != null) {
			TablePrototypeException.Error.ColumnInUse.build(columnName, this.toString());
		}
	}

	private Set<SqlPrototype.Flag> checkFlags(Set<SqlPrototype.Flag> flags, SqlObject.Type sqlType) throws TableModuleException {
		if (flags == null) {
			return new HashSet<>();
		}
		if (flags.contains(SqlPrototype.Flag.PRIMARY_KEY)) {
			this.twoPrimaryKeys();
			this.properPrimaryKey(flags, sqlType);
		}
		return new HashSet<>(flags);
	}

	private void twoPrimaryKeys() throws TableModuleException {
		if (this.pk != null) {
			TablePrototypeException.Error.TwoPrimaryKeys.build(this.getPrimeryKey(), this.toString());
		}
	}

	private void properPrimaryKey(Set<SqlPrototype.Flag> flags, SqlObject.Type sqlType) throws TableModuleException {
		if (!SqlObject.Type.PK_SQL_TYPES.contains(sqlType)) {
			TablePrototypeException.Error.WrongPrimaryKeyType.build(sqlType.toString());
		}
		if (flags.contains(SqlPrototype.Flag.AUTOINCREMENT) && !SqlObject.Type.AUTOINCREMENT_SQL_TYPES.contains(sqlType)) {
			TablePrototypeException.Error.WrongPrimaryKeyTypeWithAutoIncrement.build(sqlType.toString());
		}
	}

	private <T> SqlObject.Type checkType(Class<T> type, SqlObject.Type sqlType) throws TableModuleException {
		SqlObject.Type columnType = ((sqlType == null) || (type == null)) ? SqlObject.Type.getDefaultType(type) : sqlType;
		if (columnType == null) {
			TablePrototypeException.Error.NoDefaultTypeFound.build((type == null) ? "null" : type.getSimpleName());
		}
		return columnType;

	}

	@Override
	public void removeColumn(String columnName) throws TableModuleException {
		this.checkColumnExists(columnName);
		ColumnImpl<?> col = this.columnMap.remove(columnName);
		if (this.pk == col) {
			this.pk = null;
		}
	}

	private void checkColumnExists(String columnName) throws TableModuleException {
		if (this.getColumnForName(columnName) == null) {
			TablePrototypeException.Error.UnknownColulmnName.build(columnName, this.toString());
		}
	}

	private static class TablePrototypeException extends TableModuleException {

		private static final long serialVersionUID = 1L;

		private TablePrototypeException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			WrongPrimaryKeyType("%s is not a propere type for a primary key."), //
			WrongPrimaryKeyTypeWithAutoIncrement("%s is not a propere type for an auto-incrementable primary key."), //

			UnknownColulmnName("Unknown column %s in table %s."), //
			ColumnInUse("A column with name %s already exists for table %s."), //
			TwoPrimaryKeys("A primary key with name %s already exists for table %s."), //
			NoDefaultTypeFound("Type %s is an invalid type for a sql column."); //

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws TableModuleException {
				TableModuleException.build(new TablePrototypeException(this, arg));
			}

		}

	}

}
