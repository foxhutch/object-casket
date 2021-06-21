package org.fuchss.objectcasket.tables.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "CL")
public final class CL {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToMany
	public Set<CR> one2many = new HashSet<>();
}