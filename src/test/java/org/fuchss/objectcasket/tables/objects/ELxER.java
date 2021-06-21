package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "ELxER")
public final class ELxER {

	@Id
	@GeneratedValue
	public Integer pk;

}