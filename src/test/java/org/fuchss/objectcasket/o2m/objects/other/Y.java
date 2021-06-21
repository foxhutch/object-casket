package org.fuchss.objectcasket.o2m.objects.other;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Y")
public final class Y {

	@Id
	// @GeneratedValue
	public Boolean pk;

	public Y() {
	}

	public Y(boolean x) {
		this.pk = x;
	}

	@OneToMany
	@JoinColumn(name = "XxY")
	public Set<X> myXs = new HashSet<>();

}
