package org.fuchss.objectcasket.o2m.objects.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "X")
public final class X {

	@Id
	Boolean pk;

	public X() {
	}

	public X(boolean x) {
		this.pk = x;
	}

	@ManyToOne
	@Column(name = "XxY")
	public Y myY;

}
