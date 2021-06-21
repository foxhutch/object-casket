package org.fuchss.objectcasket.tables.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "C1L")
public final class C1L {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToMany
	@JoinColumn(name = "C1LxC1R")
	public Set<C1R> one2many = new HashSet<>();
}