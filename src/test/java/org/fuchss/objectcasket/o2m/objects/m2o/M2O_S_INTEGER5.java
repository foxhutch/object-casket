package org.fuchss.objectcasket.o2m.objects.m2o;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "M2O_S_INTEGER5")
public final class M2O_S_INTEGER5 implements M2O__S_Object<M2O_S_INTEGER5> {

	@Id
	long pk;

	@ManyToOne
	@Column(name = "b1i5")
	public M2O_C_BOOL1 x_m2o;

	@ManyToOne
	@Column(name = "b2i5")
	public M2O_C_BOOL2 y_m2o;

	@ManyToOne
	@Column(name = "c1i5")
	public M2O_C_CHAR1 c1_m2o;

	@ManyToOne
	@Column(name = "c2i5")
	public M2O_C_CHAR2 c2_m2o;

	M2O_S_INTEGER5() {
	}

	public M2O_S_INTEGER5(long x) {
		this.pk = x;
	}

	@Override
	public boolean sameAs(M2O_S_INTEGER5 x, M2O_S_INTEGER5 y) {
		return x.pk == y.pk;
	}

	@Override
	public String toString() {
		return String.format("%s pk = %s, x_m2o =[%s, %s], y_m2o =[%s, %s], c1_m2o =[%s, %s], c2_m2o =[%s, %s]", this.getClass().getSimpleName(), //
				this.pk, //
				(this.x_m2o == null ? " " : ("" + this.x_m2o.getClass().getSimpleName())), //
				(this.x_m2o == null ? "NULL" : ("" + this.x_m2o.pk)), //
				(this.y_m2o == null ? " " : ("" + this.y_m2o.getClass().getSimpleName())), //
				(this.y_m2o == null ? "NULL" : ("" + this.y_m2o.pk)), //
				(this.c1_m2o == null ? " " : ("" + this.c1_m2o.getClass().getSimpleName())), //
				(this.c1_m2o == null ? "NULL" : ("" + this.c1_m2o.pk)), //
				(this.c2_m2o == null ? " " : ("" + this.c2_m2o.getClass().getSimpleName())), //
				(this.c2_m2o == null ? "NULL" : ("" + this.c2_m2o.pk)));
	}

}