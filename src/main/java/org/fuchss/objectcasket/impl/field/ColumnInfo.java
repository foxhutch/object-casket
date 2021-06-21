package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.field.FieldInfo.Kind;

public class ColumnInfo {

	// private static final String DEFAULT_FK_COLUMN = "@id_%s_%s"; // table name
	// followed by field name
	// private static final String DEFAULT_FK_JOIN_COLUMN = "@idj_%s_%s"; // table
	// name followed by field name

	/////////// TABLE ////////////
	private String tableName;
	// The class represented by this table
	private Class<?> entityClass;

	/////////// COLUMN / CELL ////////////
	private String name;
	// The java type of the FK in this cell or the type of the value field
	// corresponding to this cell.
	private Class<?> javaType;
	private Field field;
	private Kind kind;

	public static ColumnInfo mkLocalColumnInfo(Class<?> entityClass, Field columnField, String tableName, Kind kind) {
		Objects.requireNonNull(entityClass);
		Objects.requireNonNull(columnField);
		Objects.requireNonNull(tableName);
		Objects.requireNonNull(kind);
		return new ColumnInfo(entityClass, columnField, null, tableName, kind);
	}

	public static ColumnInfo mkForeignColumnInfo(Class<?> entityClass, Field columnField, String columnName, String tableName, Kind kind) {
		Objects.requireNonNull(entityClass);
		Objects.requireNonNull(tableName);
		Objects.requireNonNull(kind);
		return new ColumnInfo(entityClass, columnField, columnName, tableName, kind);
	}

	private ColumnInfo(Class<?> entityClass, Field columnField, String columnName, String tableName, Kind kind) {
		this.entityClass = entityClass;
		this.field = columnField;
		this.tableName = tableName;
		this.kind = kind;
		this.createColumnName(columnName);
	}

	private void createColumnName(String columnName) {
		if ((columnName != null) && !columnName.isBlank()) {
			this.name = columnName;
			return;
		}
		if (this.kind == Kind.VALUE) {
			Column column = this.field.getAnnotation(Column.class);
			this.name = ((column == null) || (this.name = column.name()).isEmpty()) ? this.field.getName() : this.name;
			return;
		}

		if ((this.kind == Kind.MANY2ONE) || (this.kind == Kind.ONE2ONE)) {
			Class<?> foreignClass = this.field.getType();

			Table table = foreignClass.getAnnotation(Table.class);
			String foreignTableName = ((table == null) || (foreignTableName = foreignClass.getAnnotation(Table.class).name()).isEmpty()) ? foreignClass.getSimpleName() : foreignTableName;
			this.name = FieldInfo.fkColumnName(this.field, foreignTableName);
			return;
		}

		if ((this.kind == Kind.ONE2MANY) || (this.kind == Kind.MANY2MANY)) {
			// in an 1-to-m or am m-to-m association we don't need a column in the entity
			// table!
			this.name = null;
			return;
		}

		throw new Error("No reachable code");
	}

	public Class<?> getEntityClass() {
		return this.entityClass;
	}

	public String getTableName() {
		return this.tableName;
	}

	public Field getField() {
		return this.field;
	}

	public String getName() {
		return this.name;
	}

	public void setJavaType(Class<?> columnType) {
		this.javaType = columnType;
	}

	public Class<?> getJavaType() {
		return this.javaType;
	}

	public Kind getKind() {
		return this.kind;
	}

	/**
	 * @deprecated Why !?
	 */
	@Deprecated
	public void setKind(Kind kind) {
		this.kind = kind;
		System.out.println("Just setting Kind to " + kind);
	}

	public boolean isPK() {
		return this.field.getAnnotation(Id.class) != null;
	}

	public boolean isGenerated() {
		return this.field.getAnnotation(GeneratedValue.class) != null;
	}

	@Override
	public String toString() {
		return "ColumnInfo [tableName=" + this.tableName + ", name=" + this.name + ", entityClass=" + this.entityClass + ", javaType=" + this.javaType + ", field=" + this.field + ", kind=" + this.kind + "]";
	}
}
