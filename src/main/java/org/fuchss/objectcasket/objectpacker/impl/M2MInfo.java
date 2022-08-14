package org.fuchss.objectcasket.objectpacker.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity()
@Table(name = "Many@Many")
final class M2MInfo<C, S> {

	static final String FIELD_CLIENT_TABLE_NAME = "clientTableName";
	static final String FIELD_CLIENT_COLUMN_NAME = "clientColumnName";
	static final String FIELD_JOIN_TABLE_NAME = "joinTableName";

	static final String COL_CLIENT_TABLE_NAME = "client@TableName";
	static final String COL_CLIENT_COLUMN_NAME = "client@ColumnName";
	static final String COL_SUPPLIER_TABLE_NAME = "supplier@TableName";
	static final String COL_JOIN_TABLE_NAME = "join@TableTableName";

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = COL_CLIENT_TABLE_NAME)
	private String clientTableName;

	@Column(name = COL_CLIENT_COLUMN_NAME)
	private String clientColumnName;

	@Column(name = COL_SUPPLIER_TABLE_NAME)
	private String supplierTableName;

	@Column(name = COL_JOIN_TABLE_NAME)
	private String joinTableName;

	@Transient
	private Class<C> clientClass;

	@Transient
	private ClassInfo<C> clientClassInfo;

	@Transient
	private Class<S> supplierClass;

	@Transient
	private ClassInfo<S> supplierClassInfo;

	@SuppressWarnings("unused")
	private M2MInfo() {
	}

	M2MInfo(String clientTableName, String clientColumnName, String supplierTableName, String joinTableName) {
		this.clientTableName = clientTableName;
		this.clientColumnName = clientColumnName;
		this.supplierTableName = supplierTableName;
		this.joinTableName = joinTableName;
	}

	void init(ClassInfo<C> clientInfo, ClassInfo<S> supplierInfo) {
		this.clientClassInfo = clientInfo;
		this.supplierClassInfo = supplierInfo;
		this.clientClass = this.clientClassInfo.myClass;
		this.supplierClass = this.supplierClassInfo.myClass;

	}

	String getClientTableName() {
		return this.clientTableName;
	}

	String getClientColumnName() {
		return this.clientColumnName;
	}

	String getSupplierTableName() {
		return this.supplierTableName;
	}

	String getJoinTableName() {
		return this.joinTableName;
	}

	Class<C> getClientClass() {
		return this.clientClass;
	}

	ClassInfo<C> getClientClassInfo() {
		return this.clientClassInfo;
	}

	Class<S> getSupplierClass() {
		return this.supplierClass;
	}

	ClassInfo<S> getSupplierClassInfo() {
		return this.supplierClassInfo;
	}

}
