package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "BL")
public final class BL {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	@Column(name = "BRxBL")
	@JoinColumn(name = "BLxBR")
	public BR one2one;
}