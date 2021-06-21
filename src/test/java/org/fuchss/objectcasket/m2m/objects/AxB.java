package org.fuchss.objectcasket.m2m.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "AxB")
public final class AxB {
	@Id
	@GeneratedValue
	Integer id;
}