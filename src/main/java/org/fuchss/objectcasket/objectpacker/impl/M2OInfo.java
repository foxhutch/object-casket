package org.fuchss.objectcasket.objectpacker.impl;

import javax.persistence.*;

@Entity()
@Table(name = "Many@One")
final class M2OInfo {

	static final String FIELD_CLIENT_TABLE_NAME = "clientTableName";
	static final String FIELD_CLIENT_COLUMN_NAME = "clientColumnName";

	static final String COL_CLIENT_TABLE_NAME = "client@TableName";
	static final String COL_CLIENT_COLUMN_NAME = "client@ColumnName";
	static final String COL_SUPPLIER_TABLE_NAME = "supplier@TableName";

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = COL_CLIENT_TABLE_NAME)
	private String clientTableName;

	@Column(name = COL_CLIENT_COLUMN_NAME)
	private String clientColumnName;

	@Column(name = COL_SUPPLIER_TABLE_NAME)
	private String supplierTableName;

	@SuppressWarnings("unused")
	private M2OInfo() {

	}

	M2OInfo(String clientTableName, String clientColumnName, String supplierTableName) {
		this.clientTableName = clientTableName;
		this.clientColumnName = clientColumnName;
		this.supplierTableName = supplierTableName;

	}

	String getSupplierTableName() {
		return this.supplierTableName;
	}

	String getClientTableName() {
		return this.clientTableName;
	}

	String getClientColumnName() {
		return this.clientColumnName;
	}

}
