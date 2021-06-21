package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "AR")
public final class AR {

	@Id
	@GeneratedValue
	public Integer pk;

}