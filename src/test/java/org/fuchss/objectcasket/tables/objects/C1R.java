package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "C1R")
public final class C1R {

	@Id
	@GeneratedValue
	public Integer pk;

	@ManyToOne
	@Column(name = "C1LxC1R")
	public C1L many2One;

}