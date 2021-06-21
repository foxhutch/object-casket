package org.fuchss.objectcasket.m2m.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "DxE")
public final class DxE {
	@Id
	@GeneratedValue
	Integer id;

	@ManyToOne
	@Column(name = "id_d")
	public D d;

	@ManyToOne
	@Column(name = "id_e")
	public E e;

}