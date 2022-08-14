package org.fuchss.objectcasket.objectpacker.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;

@SuppressWarnings("java:S3011")
class ClassInfo<T> {

	private String tableName;

	List<Field> allFields;
	Class<T> myClass;
	Constructor<T> defaultConstructor;

	private Field pkField;
	private Class<Serializable> pkType;
	private String pkColumnName;
	private boolean isGenerated;

	ClassInfo(Class<T> clazz, String tableName) throws CasketException {
		this.tableName = tableName;
		this.myClass = clazz;
		this.allFields = new ArrayList<>();
		this.calcAllFields(this.myClass);
		this.allFields.forEach(field -> field.setAccessible(true));
		this.init();
	}

	private void calcAllFields(Class<?> clazz) {
		this.allFields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Transient.class) && !Modifier.isStatic(field.getModifiers())).toList());
		Class<?> superClass = clazz.getSuperclass();
		if ((superClass == null) || superClass.equals(Object.class)) {
			return;
		}
		this.calcAllFields(superClass);
	}

	private void init() throws CasketException {
		for (Field field : this.allFields) {
			if (field.getAnnotation(Id.class) != null) {
				this.pkField = field;
				this.pkType = mkValueType(field);
				this.pkColumnName = mkColumnName(field);
				this.isGenerated = (field.getAnnotation(GeneratedValue.class) != null);
				return;
			}
		}
		throw CasketError.MISSING_PK.build();

	}

	static Class<Serializable> mkValueType(Field field) throws CasketException {
		if (!Serializable.class.isAssignableFrom(field.getType()))
			throw CasketError.NON_SERIALIZABLE_FIELD.build();
		@SuppressWarnings("unchecked")
		Class<Serializable> type = (Class<Serializable>) field.getType();
		return type;
	}

	@SuppressWarnings("java:S1121")
	static String mkColumnName(Field field, String... prohibited) throws CasketException {
		Column column = field.getAnnotation(Column.class);
		String name = null;
		name = ((column == null) || (name = column.name()).isEmpty()) ? field.getName() : name;
		if (Arrays.asList(prohibited).contains(name))
			throw CasketError.INVALID_NAME.build();
		return name;
	}

	Serializable getPK(Object obj) throws IllegalArgumentException, IllegalAccessException {
		return (Serializable) this.pkField.get(obj);
	}

	boolean notPkField(Field field) {
		return field != this.pkField;
	}

	String getColumnName() {
		return this.pkColumnName;
	}

	String getFieldName() {
		return this.pkField.getName();
	}

	boolean isGenerated() {
		return this.isGenerated;
	}

	String getTableName() {
		return this.tableName;
	}

	Class<Serializable> getType() {
		return this.pkType;
	}

}