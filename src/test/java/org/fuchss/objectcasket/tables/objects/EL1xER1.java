package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "EL1xER1")
public final class EL1xER1 {

	@Id
	@GeneratedValue
	public Integer pk;

}