package org.fuchss.objectcasket.tables.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "EL1")
public final class EL1 {

	@Id
	@GeneratedValue
	public Integer pk;

	@ManyToMany
	@JoinTable(name = "EL1xER1")
	public Set<ER1> m2m = new HashSet<>();
}