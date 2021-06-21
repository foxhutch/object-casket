package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "BR")
public final class BR {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	@Column(name = "BLxBR")
	@JoinColumn(name = "BRxBL")
	public BL one2one;

}