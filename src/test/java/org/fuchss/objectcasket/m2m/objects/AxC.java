package org.fuchss.objectcasket.m2m.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "AxC")
public final class AxC {
	@Id
	@GeneratedValue
	Integer id;

}