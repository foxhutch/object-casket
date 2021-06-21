package org.fuchss.objectcasket.m2m.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "B")
public final class B {

	@Id
	public Character pk;

	@ManyToMany
	@JoinTable(name = "BxC")
	public Set<C> c = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "AxB")
	public Set<A> a = new HashSet<>();

	@SuppressWarnings("unused")
	private B() {
	}

	public B(char x) {
		this.pk = x;
	}

	@Override
	public String toString() {
		return "b=" + this.pk + "";
	}

}