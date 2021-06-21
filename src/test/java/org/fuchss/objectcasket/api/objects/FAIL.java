package org.fuchss.objectcasket.api.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FA IL")
public final class FAIL {
	@Id
	@GeneratedValue
	public Integer id;
}