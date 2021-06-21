package org.fuchss.objectcasket.m2m.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "C")
public final class C {

	@Id
	public Character pk;

	@ManyToMany
	@JoinTable(name = "AxC")
	public Set<A> a = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "BxC")
	public Set<B> b = new HashSet<>();

	@SuppressWarnings("unused")
	private C() {
	}

	public C(char x) {
		this.pk = x;
	}

	@Override
	public String toString() {
		return "c=" + this.pk + "";
	}

}