package org.fuchss.objectcasket.api.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "B")
public  final class B {
	@Id
	@GeneratedValue
	public Integer id;

	@ManyToOne
	@Column(name = "AxB")
	public A oneA;

	@ManyToOne
	public C oneC;
}