package org.fuchss.objectcasket.sqlconnector.impl.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * The implementation of {@link SqlColumnSignature}.
 *
 */
public class SqlColumnSignatureImpl implements SqlColumnSignature {

	private StorageClass type;
	private Class<? extends Serializable> javaType;

	private List<Flag> flags = new ArrayList<>();

	private SqlObj defaultValue;
	private SqlObj value;

	/**
	 * This constructor creates a signature from a prototype.
	 *
	 * @param ref
	 *            - the prototype.
	 */
	public SqlColumnSignatureImpl(SqlColumnSignatureImpl ref) {
		this.type = ref.type;
		this.javaType = ref.javaType;
		this.flags = new ArrayList<>(ref.flags);
		this.defaultValue = ref.defaultValue.duplicate();
	}

	/**
	 * This constructor creates a signature from scratch.
	 *
	 * @param type
	 *            - the referenced SQL type.
	 * @param javaType
	 *            - the target Java type.
	 * @param defaultValue
	 *            - the default value as an {@link SqlObj}.
	 * @throws CasketException
	 *             on error.
	 */
	public SqlColumnSignatureImpl(StorageClass type, Class<? extends Serializable> javaType, SqlObj defaultValue) throws CasketException {
		Util.objectsNotNull(type, javaType, defaultValue);
		this.type = type;
		this.javaType = javaType;
		this.defaultValue = defaultValue;
		this.checkType();
	}

	@Override
	public void setValue(SqlObject value) throws CasketException {
		Util.objectsNotNull(value);
		if (value.getStorageClass() != this.type)
			throw CasketError.INCOMPATIBLE_STORAGE_CLASSES.build();
		this.value = (SqlObj) value;
	}

	@Override
	public void clear() {
		this.value = null;
	}

	@Override
	public SqlObj getValue() {
		return (this.value == null ? this.defaultValue.duplicate() : this.value.duplicate());
	}

	@Override
	public void setFlag(Flag flag) throws CasketException {
		if (this.flags.contains(flag)) {
			return;
		}
		this.flags.add(flag);
		this.checkFlags();
	}

	@Override
	public void resetFlag(Flag flag) throws CasketException {
		this.flags.remove(flag);
		this.checkFlags();
	}

	@Override
	public StorageClass getType() {
		return this.type;
	}

	@Override
	public Class<? extends Serializable> getJavaType() {
		return this.javaType;
	}

	@Override
	public SqlObj getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public boolean isPrimaryKey() {
		return this.flags.contains(Flag.PRIMARY_KEY);
	}

	@Override
	public boolean isAutoIncrementedPrimaryKey() {
		boolean res = this.flags.contains(Flag.AUTOINCREMENT);
		res &= this.flags.contains(Flag.PRIMARY_KEY);
		return res;
	}

	private void checkType() throws CasketException {
		if (StorageClass.POSSIBLE_CLASS_MAP.get(this.type.name()).contains(this.javaType))
			return;
		for (Class<? extends Serializable> clazz : StorageClass.POSSIBLE_CLASS_MAP.get(this.type.name())) {
			if (clazz.isAssignableFrom(this.javaType))
				return;
		}
		throw CasketError.INVALID_PROTOTYPE.build();
	}

	private void checkFlags() throws CasketException {
		if (this.flags.contains(Flag.PRIMARY_KEY))
			this.checkPrimaryKey();
		if (this.flags.contains(Flag.AUTOINCREMENT))
			this.checkAutoIncrementedPrimaryKey();
		if (this.flags.contains(Flag.NOT_NULL) && this.defaultValue.isNull() && (!this.flags.contains(Flag.PRIMARY_KEY)))
			throw CasketError.MISSING_DEFAULT_VALUE.build();
	}

	private void checkPrimaryKey() throws CasketException {
		boolean res = this.flags.contains(Flag.PRIMARY_KEY);
		res &= !this.flags.contains(Flag.NOT_NULL);
		res &= this.defaultValue.isNull();
		res &= StorageClass.PK_SQL_TYPES.contains(this.type);
		res &= StorageClass.PK_JAVA_TYPES.contains(this.javaType);
		if (res)
			return;
		throw CasketError.WRONG_PK_TYPE.build();
	}

	private void checkAutoIncrementedPrimaryKey() throws CasketException {
		boolean res = this.flags.contains(Flag.AUTOINCREMENT);
		res &= this.flags.contains(Flag.PRIMARY_KEY);
		res &= this.defaultValue.isNull();
		res &= StorageClass.AUTOINCREMENT_SQL_TYPES.contains(this.type);
		res &= StorageClass.AUTOINCREMENTED_JAVA_TYPES.contains(this.javaType);
		if (res)
			return;
		throw CasketError.MISPLACED_AUTO_INCREMENT.build();
	}

	@Override
	public boolean notNull() {
		return this.flags.contains(Flag.NOT_NULL);
	}

}
