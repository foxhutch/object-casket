package org.fuchss.objectcasket.o2m.fundamentals;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__C_Object;

//
// ------ 1              n -----
//| bool | x------------> |  Z  |
// ------                  -----
//

//
//------ 1              n -----
//| Bool | x------------> |  Z  |
//------                  -----
//

@Entity()
@Table(name = "O2M_C_BOOL1")
final class O2M_C_BOOL1 implements O2M__C_Object<O2M_C_BOOL1> {

	@Id
	Boolean cBoolean;

	O2M_C_BOOL1() {
	}

	O2M_C_BOOL1(boolean x) {
		this.cBoolean = x;
	}

	@OneToMany 	public Set<O2M_S_BOOL1> 	b1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_BOOL2> 	b2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER1> 	i1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER2> 	i2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER3> 	i3 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER4> 	i4 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER5> 	i5 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER6> 	i6 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER7> 	i7 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER8>	i8 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DATE> 		date = new HashSet<>();
	@OneToMany	public Set<O2M_S_TIMESTAMP> time = new HashSet<>();
	@OneToMany	public Set<O2M_S_CHAR1> 	c1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_CHAR2> 	c2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DOUBLE1> 	d1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DOUBLE2> 	d2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_FLOAT1> 	f1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_FLOAT2> 	f2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC1> 	n1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC2> 	n2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC3> 	n3 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC4> 	n4 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC5> 	n5 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC6> 	n6 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC7> 	n7 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC8> 	n8 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC9> 	n9 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC10> n10 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC11> n11 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC12> n12 = new HashSet<>();
	@OneToMany	public Set<O2M_S_TEXT> 		t = new HashSet<>();
	@OneToMany	public Set<O2M_S_VARCHAR> 	v = new HashSet<>();
	
	
	@Override
	public <X> void add(Set<X> suppliers, X ignore) {

		if (ignore instanceof O2M_S_BOOL1)		suppliers.forEach(s -> this.b1.add((O2M_S_BOOL1) s));
		if (ignore instanceof O2M_S_BOOL2)		suppliers.forEach(s -> this.b2.add((O2M_S_BOOL2) s));
		if (ignore instanceof O2M_S_INTEGER1)	suppliers.forEach(s -> this.i1.add((O2M_S_INTEGER1) s));
		if (ignore instanceof O2M_S_INTEGER2)	suppliers.forEach(s -> this.i2.add((O2M_S_INTEGER2) s));
		if (ignore instanceof O2M_S_INTEGER3)	suppliers.forEach(s -> this.i3.add((O2M_S_INTEGER3) s));
		if (ignore instanceof O2M_S_INTEGER4)	suppliers.forEach(s -> this.i4.add((O2M_S_INTEGER4) s));
		if (ignore instanceof O2M_S_INTEGER5)	suppliers.forEach(s -> this.i5.add((O2M_S_INTEGER5) s));
		if (ignore instanceof O2M_S_INTEGER6)	suppliers.forEach(s -> this.i6.add((O2M_S_INTEGER6) s));
		if (ignore instanceof O2M_S_INTEGER7)	suppliers.forEach(s -> this.i7.add((O2M_S_INTEGER7) s));
		if (ignore instanceof O2M_S_INTEGER8)	suppliers.forEach(s -> this.i8.add((O2M_S_INTEGER8) s));
		if (ignore instanceof O2M_S_DATE)		suppliers.forEach(s -> this.date.add((O2M_S_DATE) s));
		if (ignore instanceof O2M_S_TIMESTAMP)	suppliers.forEach(s -> this.time.add((O2M_S_TIMESTAMP) s));
		if (ignore instanceof O2M_S_CHAR1)		suppliers.forEach(s -> this.c1.add((O2M_S_CHAR1) s));
		if (ignore instanceof O2M_S_CHAR2)		suppliers.forEach(s -> this.c2.add((O2M_S_CHAR2) s));
		if (ignore instanceof O2M_S_DOUBLE1)	suppliers.forEach(s -> this.d1.add((O2M_S_DOUBLE1) s));
		if (ignore instanceof O2M_S_DOUBLE2)	suppliers.forEach(s -> this.d2.add((O2M_S_DOUBLE2) s));
		if (ignore instanceof O2M_S_FLOAT1)		suppliers.forEach(s -> this.f1.add((O2M_S_FLOAT1) s));
		if (ignore instanceof O2M_S_FLOAT2)		suppliers.forEach(s -> this.f2.add((O2M_S_FLOAT2) s));
		if (ignore instanceof O2M_S_NUMERIC1)	suppliers.forEach(s -> this.n1.add((O2M_S_NUMERIC1) s));
		if (ignore instanceof O2M_S_NUMERIC2)	suppliers.forEach(s -> this.n2.add((O2M_S_NUMERIC2) s));
		if (ignore instanceof O2M_S_NUMERIC3)	suppliers.forEach(s -> this.n3.add((O2M_S_NUMERIC3) s));
		if (ignore instanceof O2M_S_NUMERIC4)	suppliers.forEach(s -> this.n4.add((O2M_S_NUMERIC4) s));
		if (ignore instanceof O2M_S_NUMERIC5)	suppliers.forEach(s -> this.n5.add((O2M_S_NUMERIC5) s));
		if (ignore instanceof O2M_S_NUMERIC6)	suppliers.forEach(s -> this.n6.add((O2M_S_NUMERIC6) s));
		if (ignore instanceof O2M_S_NUMERIC7)	suppliers.forEach(s -> this.n7.add((O2M_S_NUMERIC7) s));
		if (ignore instanceof O2M_S_NUMERIC8)	suppliers.forEach(s -> this.n8.add((O2M_S_NUMERIC8) s));
		if (ignore instanceof O2M_S_NUMERIC9)	suppliers.forEach(s -> this.n9.add((O2M_S_NUMERIC9) s));
		if (ignore instanceof O2M_S_NUMERIC10)	suppliers.forEach(s -> this.n10.add((O2M_S_NUMERIC10) s));
		if (ignore instanceof O2M_S_NUMERIC11)	suppliers.forEach(s -> this.n11.add((O2M_S_NUMERIC11) s));
		if (ignore instanceof O2M_S_NUMERIC12)	suppliers.forEach(s -> this.n12.add((O2M_S_NUMERIC12) s));
		if (ignore instanceof O2M_S_TEXT)		suppliers.forEach(s -> this.t.add((O2M_S_TEXT) s));
		if (ignore instanceof O2M_S_VARCHAR)		suppliers.forEach(s -> this.v.add((O2M_S_VARCHAR) s));
	
	}

	@Override
	public boolean sameAs(O2M_C_BOOL1 x, O2M_C_BOOL1 y) {
		if (x == y)
			return true;
		if (!((x != null) && (y != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue())))
			return false;
		
		if (!this.check_Supplier(x.b1, y.b1)) return false;
		if (!this.check_Supplier(x.b2, y.b2)) return false;
		if (!this.check_Supplier(x.i1, y.i1)) return false;
		if (!this.check_Supplier(x.i2, y.i2)) return false;
		if (!this.check_Supplier(x.i3, y.i3)) return false;
		if (!this.check_Supplier(x.i4, y.i4)) return false;
		if (!this.check_Supplier(x.i5, y.i5)) return false;
		if (!this.check_Supplier(x.i6, y.i6)) return false;
		if (!this.check_Supplier(x.i7, y.i7)) return false;
		if (!this.check_Supplier(x.i8, y.i8)) return false;
		if (!this.check_Supplier(x.date, y.date)) return false;
		if (!this.check_Supplier(x.time, y.time)) return false;
		if (!this.check_Supplier(x.c1, y.c1)) return false;
		if (!this.check_Supplier(x.c2, y.c2)) return false;
		if (!this.check_Supplier(x.d1, y.d1)) return false;
		if (!this.check_Supplier(x.d2, y.d2)) return false;
		if (!this.check_Supplier(x.f1, y.f1)) return false;
		if (!this.check_Supplier(x.f2, y.f2)) return false;
		if (!this.check_Supplier(x.n1, y.n1)) return false;
		if (!this.check_Supplier(x.n2, y.n2)) return false;
		if (!this.check_Supplier(x.n3, y.n3)) return false;
		if (!this.check_Supplier(x.n4, y.n4)) return false;
		if (!this.check_Supplier(x.n5, y.n5)) return false;
		if (!this.check_Supplier(x.n6, y.n6)) return false;
		if (!this.check_Supplier(x.n7, y.n7)) return false;
		if (!this.check_Supplier(x.n8, y.n8)) return false;
		if (!this.check_Supplier(x.n9, y.n9)) return false;
		if (!this.check_Supplier(x.n10, y.n10)) return false;
		if (!this.check_Supplier(x.n11, y.n11)) return false;
		if (!this.check_Supplier(x.n12, y.n12)) return false;
		if (!this.check_Supplier(x.t, y.t)) return false;
		if (!this.check_Supplier(x.v, y.v)) return false;
		
		return true;
	}

}

@Entity()
@Table(name = "O2M_C_BOOL2")
final class O2M_C_BOOL2 implements O2M__C_Object<O2M_C_BOOL2> {

	@Id
	boolean tBoolean;

	O2M_C_BOOL2() {
	}

	O2M_C_BOOL2(boolean x) {
		this.tBoolean = x;
	}

	@OneToMany	public Set<O2M_S_BOOL1> 	b1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_BOOL2> 	b2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER1> 	i1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER2> 	i2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER3> 	i3 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER4> 	i4 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER5> 	i5 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER6> 	i6 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER7> 	i7 = new HashSet<>();
	@OneToMany	public Set<O2M_S_INTEGER8> 	i8 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DATE> 		date = new HashSet<>();
	@OneToMany	public Set<O2M_S_TIMESTAMP> time = new HashSet<>();
	@OneToMany	public Set<O2M_S_CHAR1> 	c1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_CHAR2> 	c2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DOUBLE1> 	d1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_DOUBLE2> 	d2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_FLOAT1> 	f1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_FLOAT2> 	f2 = new HashSet<>();

	@OneToMany	public Set<O2M_S_NUMERIC1> 	n1 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC2> 	n2 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC3> 	n3 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC4> 	n4 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC5> 	n5 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC6> 	n6 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC7> 	n7 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC8> 	n8 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC9> 	n9 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC10> n10 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC11> n11 = new HashSet<>();
	@OneToMany	public Set<O2M_S_NUMERIC12> n12 = new HashSet<>();
	@OneToMany	public Set<O2M_S_TEXT> 		t = new HashSet<>();
	@OneToMany	public Set<O2M_S_VARCHAR> 	v = new HashSet<>();
	

	@Override
	public <X> void add(Set<X> suppliers, X ignore) {
		if (ignore instanceof O2M_S_BOOL1)		suppliers.forEach(s -> this.b1.add((O2M_S_BOOL1) s));
		if (ignore instanceof O2M_S_BOOL2)		suppliers.forEach(s -> this.b2.add((O2M_S_BOOL2) s));
		if (ignore instanceof O2M_S_INTEGER1)	suppliers.forEach(s -> this.i1.add((O2M_S_INTEGER1) s));
		if (ignore instanceof O2M_S_INTEGER2)	suppliers.forEach(s -> this.i2.add((O2M_S_INTEGER2) s));
		if (ignore instanceof O2M_S_INTEGER3)	suppliers.forEach(s -> this.i3.add((O2M_S_INTEGER3) s));
		if (ignore instanceof O2M_S_INTEGER4)	suppliers.forEach(s -> this.i4.add((O2M_S_INTEGER4) s));
		if (ignore instanceof O2M_S_INTEGER5)	suppliers.forEach(s -> this.i5.add((O2M_S_INTEGER5) s));
		if (ignore instanceof O2M_S_INTEGER6)	suppliers.forEach(s -> this.i6.add((O2M_S_INTEGER6) s));
		if (ignore instanceof O2M_S_INTEGER7)	suppliers.forEach(s -> this.i7.add((O2M_S_INTEGER7) s));
		if (ignore instanceof O2M_S_INTEGER8)	suppliers.forEach(s -> this.i8.add((O2M_S_INTEGER8) s));
		if (ignore instanceof O2M_S_DATE)		suppliers.forEach(s -> this.date.add((O2M_S_DATE) s));
		if (ignore instanceof O2M_S_TIMESTAMP)	suppliers.forEach(s -> this.time.add((O2M_S_TIMESTAMP) s));
		if (ignore instanceof O2M_S_CHAR1)		suppliers.forEach(s -> this.c1.add((O2M_S_CHAR1) s));
		if (ignore instanceof O2M_S_CHAR2)		suppliers.forEach(s -> this.c2.add((O2M_S_CHAR2) s));
		if (ignore instanceof O2M_S_DOUBLE1)	suppliers.forEach(s -> this.d1.add((O2M_S_DOUBLE1) s));
		if (ignore instanceof O2M_S_DOUBLE2)	suppliers.forEach(s -> this.d2.add((O2M_S_DOUBLE2) s));
		if (ignore instanceof O2M_S_FLOAT1)		suppliers.forEach(s -> this.f1.add((O2M_S_FLOAT1) s));
		if (ignore instanceof O2M_S_FLOAT2)		suppliers.forEach(s -> this.f2.add((O2M_S_FLOAT2) s));
		if (ignore instanceof O2M_S_NUMERIC1)	suppliers.forEach(s -> this.n1.add((O2M_S_NUMERIC1) s));
		if (ignore instanceof O2M_S_NUMERIC2)	suppliers.forEach(s -> this.n2.add((O2M_S_NUMERIC2) s));
		if (ignore instanceof O2M_S_NUMERIC3)	suppliers.forEach(s -> this.n3.add((O2M_S_NUMERIC3) s));
		if (ignore instanceof O2M_S_NUMERIC4)	suppliers.forEach(s -> this.n4.add((O2M_S_NUMERIC4) s));
		if (ignore instanceof O2M_S_NUMERIC5)	suppliers.forEach(s -> this.n5.add((O2M_S_NUMERIC5) s));
		if (ignore instanceof O2M_S_NUMERIC6)	suppliers.forEach(s -> this.n6.add((O2M_S_NUMERIC6) s));
		if (ignore instanceof O2M_S_NUMERIC7)	suppliers.forEach(s -> this.n7.add((O2M_S_NUMERIC7) s));
		if (ignore instanceof O2M_S_NUMERIC8)	suppliers.forEach(s -> this.n8.add((O2M_S_NUMERIC8) s));
		if (ignore instanceof O2M_S_NUMERIC9)	suppliers.forEach(s -> this.n9.add((O2M_S_NUMERIC9) s));
		if (ignore instanceof O2M_S_NUMERIC10)	suppliers.forEach(s -> this.n10.add((O2M_S_NUMERIC10) s));
		if (ignore instanceof O2M_S_NUMERIC11)	suppliers.forEach(s -> this.n11.add((O2M_S_NUMERIC11) s));
		if (ignore instanceof O2M_S_NUMERIC12)	suppliers.forEach(s -> this.n12.add((O2M_S_NUMERIC12) s));
		if (ignore instanceof O2M_S_TEXT)		suppliers.forEach(s -> this.t.add((O2M_S_TEXT) s));
		if (ignore instanceof O2M_S_VARCHAR)	suppliers.forEach(s -> this.v.add((O2M_S_VARCHAR) s));
		
	}

	@Override
	public boolean sameAs(O2M_C_BOOL2 x, O2M_C_BOOL2 y) {
		if (x == y)
			return true;
		if (!((x != null) && (y != null) && (x.tBoolean == y.tBoolean)))
			return false;

		if (!this.check_Supplier(x.b1, y.b1)) return false;
		if (!this.check_Supplier(x.b2, y.b2)) return false;
		if (!this.check_Supplier(x.i1, y.i1)) return false;
		if (!this.check_Supplier(x.i2, y.i2)) return false;
		if (!this.check_Supplier(x.i3, y.i3)) return false;
		if (!this.check_Supplier(x.i4, y.i4)) return false;
		if (!this.check_Supplier(x.i5, y.i5)) return false;
		if (!this.check_Supplier(x.i6, y.i6)) return false;
		if (!this.check_Supplier(x.i7, y.i7)) return false;
		if (!this.check_Supplier(x.i8, y.i8)) return false;
		if (!this.check_Supplier(x.date, y.date)) return false;
		if (!this.check_Supplier(x.time, y.time)) return false;
		if (!this.check_Supplier(x.c1, y.c1)) return false;
		if (!this.check_Supplier(x.c2, y.c2)) return false;
		if (!this.check_Supplier(x.d1, y.d1)) return false;
		if (!this.check_Supplier(x.d2, y.d2)) return false;
		if (!this.check_Supplier(x.f1, y.f1)) return false;
		if (!this.check_Supplier(x.f2, y.f2)) return false;
		if (!this.check_Supplier(x.n1, y.n1)) return false;
		if (!this.check_Supplier(x.n2, y.n2)) return false;
		if (!this.check_Supplier(x.n3, y.n3)) return false;
		if (!this.check_Supplier(x.n4, y.n4)) return false;
		if (!this.check_Supplier(x.n5, y.n5)) return false;
		if (!this.check_Supplier(x.n6, y.n6)) return false;
		if (!this.check_Supplier(x.n7, y.n7)) return false;
		if (!this.check_Supplier(x.n8, y.n8)) return false;
		if (!this.check_Supplier(x.n9, y.n9)) return false;
		if (!this.check_Supplier(x.n10, y.n10)) return false;
		if (!this.check_Supplier(x.n11, y.n11)) return false;
		if (!this.check_Supplier(x.n12, y.n12)) return false;
		if (!this.check_Supplier(x.t, y.t)) return false;
		if (!this.check_Supplier(x.v, y.v)) return false;


		return true;
	}

}
