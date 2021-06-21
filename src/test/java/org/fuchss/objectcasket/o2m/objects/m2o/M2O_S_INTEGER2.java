package org.fuchss.objectcasket.o2m.objects.m2o;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "M2O_S_INTEGER2")
public final class M2O_S_INTEGER2 implements M2O__S_Object<M2O_S_INTEGER2> {

	@Id
	@GeneratedValue
	Integer pk;

	@ManyToOne
	@Column(name = "b1i2")
	public M2O_C_BOOL1 x_m2o;

	@ManyToOne
	@Column(name = "b2i2")
	public M2O_C_BOOL2 y_m2o;

	@ManyToOne
	@Column(name = "c1i2")
	public M2O_C_CHAR1 c1_m2o;

	@ManyToOne
	@Column(name = "c2i2")
	public M2O_C_CHAR2 c2_m2o;

	@Override
	public boolean sameAs(M2O_S_INTEGER2 x, M2O_S_INTEGER2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.pk.intValue() == y.pk.intValue()));
	}

	@Override
	public String toString() {
		return String.format("%s pk = %s, x_m2o =[%s, %s], y_m2o =[%s, %s], c1_m2o =[%s, %s], c2_m2o =[%s, %s]", this.getClass().getSimpleName(), //
				(this.pk == null ? "NULL" : ("" + this.pk)), //
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