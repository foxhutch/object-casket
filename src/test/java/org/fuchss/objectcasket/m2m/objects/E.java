package org.fuchss.objectcasket.m2m.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "E")
public final class E {

	@Id
	public Character pk;

	@ManyToMany
	@JoinTable(name = "DxE", joinColumns = @JoinColumn(name = "id_d"), inverseJoinColumns = @JoinColumn(name = "id_e"))
	public Set<D> d = new HashSet<>();

	@SuppressWarnings("unused")
	private E() {
	}

	public E(char x) {
		this.pk = x;
	}

}