package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "DL")
public final class DL {

	@Id
	@GeneratedValue
	public Integer pk;

	@ManyToOne
	public DR many2one;
}