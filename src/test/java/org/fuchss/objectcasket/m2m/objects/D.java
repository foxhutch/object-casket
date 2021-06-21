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
@Table(name = "D")
public final class D {

	@Id
	public Character pk;

	@ManyToMany
	@JoinTable(name = "DxE", joinColumns = @JoinColumn(name = "id_e"), inverseJoinColumns = @JoinColumn(name = "id_d"))
	public Set<E> e = new HashSet<>();

	@SuppressWarnings("unused")
	private D() {
	}

	public D(char x) {
		this.pk = x;
	}

}
