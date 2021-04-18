package org.fuchss.objectcasket.impl.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class FieldInfo {
	protected Class<?> entityClass;
	protected SessionImpl session;
	protected String tableName;
	protected Field field;
	protected String columnName = "";
	protected Kind kind;
	protected Class<?> columnType;
	protected List<SqlPrototype.Flag> flags = new ArrayList<>();

	public FieldInfo(Field field, Class<?> entityClass, String tableName, SessionImpl session) {
		this.field = field;
		this.entityClass = entityClass;
		this.kind = Kind.VALUE;
		this.session = session;
	}

	protected FieldInfo(Field field, Class<?> entityClass, Kind kind, SessionImpl session) {
		this.field = field;
		this.entityClass = entityClass;
		this.kind = kind;
		this.session = session;
	}

	protected void addFlag(SqlPrototype.Flag flag) {
		if (!this.flags.contains(flag)) {
			this.flags.add(flag);
		}
	}

	public Field getField() {
		return this.field;
	}

	public Class<?> getEntityClass() {
		return this.entityClass;
	}

	public Class<?> getColumnType() {
		return this.columnType;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public Kind getKind() {
		return this.kind;
	}

	public boolean is(Kind kind) {
		return this.kind == kind;
	}

	public Set<SqlPrototype.Flag> getFlags() {
		Set<SqlPrototype.Flag> set = new HashSet<>();
		this.flags.forEach(f -> set.add(f));
		return set;
	}

	protected static String fkColumnName(Field field, String tableName, String columnName) {
		String name = ((columnName == null) || columnName.isEmpty()) ? String.format(FieldInfo.DEFAULT_FK_FORMAT, tableName, field.getName()) : columnName;
		return name;
	}

	protected static final String DEFAULT_FK_FORMAT = "id_%s_%s";

	public static enum Kind {
		ONE2ONE, ONE2MANY, MANY2ONE, MANY2MANY, VALUE;
	}

	protected static void findPossibleFields(Class<?> foreignSuperClass, List<Field> possibleFields, Class<? extends Annotation> annotation) {
		possibleFields.addAll(Arrays.asList(foreignSuperClass.getDeclaredFields()).stream().filter(field -> field.isAnnotationPresent(annotation)).collect(Collectors.toList()));
		Class<?> nextSuperClass = foreignSuperClass.getSuperclass();
		if ((nextSuperClass == null) || nextSuperClass.equals(Object.class)) {
			return;
		}
		FieldInfo.findPossibleFields(nextSuperClass, possibleFields, annotation);
	}

	protected static class FieldException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private FieldException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {
			WrongForeignAnnotation("Missing OneToOne annotation for field %s in %s, which is indicated by field %s in %s."), //
			WrongEntity("%s isn't a proper entity for foreign key field %s in %s."), //
			WrongOne2ManyField("%s isn't a proper one-to-many field in %s. The type of the field must be Set<X> where X is the entity class of the joined table."), //
			WrongOne2ManyEntity("%s isn't a proper entity for one-to-many field in %s."), //
			WrongMany2ManyField("%s isn't a proper many-to-many field in %s. The type of the field must be Set<X> where X is the entity class."), //
			WrongMany2ManyEntity("%s isn't a proper entity for the many-to-many field %s in %s."), //

			IncompatibleMany2ManyEntity("%s in %s and %s in %s are incompatible many-to-many field definitions, no matiching join and or inversejoin column."), //

			WrongValueField("%s isn't a proper value field in %s. Impossible to determin the sql type."), //
			IllegalPrimaryKey("%s isn't a proper primary key field in %s. Only %s are supported classes or types, only %s with autoincrement and only %s are supported sql types."), //

			MissingProperOneToManyAnnotation(""//
					+ "Missing or wrong @JoinColumn annotation for field %s in %s.\n" //
					+ "Following entries are required: @JoinColumn(name = x) where the name parameter must be the name of the foreign key colum in the joined table.\n"), //

			MissingJoinTable("Missing @JoinTable anotation for field %s in %s.\n"), //
			MissingRegisteredJoinTable("No class found for join table %s of	field %s in %s. Declare one first."), //
			MissingPrimaryKey("Missing Primary Key for class %s. Use @Id private [Integer|String] myPk.");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new FieldException(this, arg));
			}

		}
	}

}
