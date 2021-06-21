package org.fuchss.objectcasket.api.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "C")
public  final class C {
	@Id
	@GeneratedValue
	public Integer id;

	@OneToMany
	public Set<B> manyBs = new HashSet<>();
}