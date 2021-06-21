package org.fuchss.objectcasket.api.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "A")
public  final class A {
	@Id
	@GeneratedValue
	public Integer id;

	@OneToMany
	@JoinColumn(name = "AxB")
	public Set<B> manyBs = new HashSet<>();
}