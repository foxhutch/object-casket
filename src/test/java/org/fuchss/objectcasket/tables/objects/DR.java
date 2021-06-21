package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "DR")
public final class DR {

	@Id
	@GeneratedValue
	public Integer pk;

}