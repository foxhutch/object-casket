package org.fuchss.objectcasket.m2m.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "A")
public final class A {

	@Id
	public Character pk;

	@ManyToMany
	@JoinTable(name = "AxB")
	public Set<B> b = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "AxC")
	public Set<C> c = new HashSet<>();

	@SuppressWarnings("unused")
	private A() {
	}

	public A(char x) {
		this.pk = x;
	}

	@Override
	public String toString() {
		return "a=" + this.pk + "";
	}

}
