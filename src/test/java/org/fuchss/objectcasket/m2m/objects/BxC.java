package org.fuchss.objectcasket.m2m.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "BxC")
public final class BxC {
	@Id
	@GeneratedValue
	Integer id;

}