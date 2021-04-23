package org.fuchss.objectcasket.o2m.fundamentals;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_INTEGER1")
final class O2M_S_INTEGER1 implements O2M__S_Object<O2M_S_INTEGER1> {

	@Id
	@GeneratedValue
	Long cLong;

	@Override
	public boolean sameAs(O2M_S_INTEGER1 x, O2M_S_INTEGER1 y) {

		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER1 cLong " + this.cLong;
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER2")
final class O2M_S_INTEGER2 implements O2M__S_Object<O2M_S_INTEGER2> {

	@Id
	@GeneratedValue
	Integer cInteger;

	@Override
	public String toString() {
		return "O2M_S_INTEGER2 cInteger " + this.cInteger;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER2 x, O2M_S_INTEGER2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cInteger.intValue() == y.cInteger.intValue()));
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER3")
final class O2M_S_INTEGER3 implements O2M__S_Object<O2M_S_INTEGER3> {

	@Id
	@GeneratedValue
	Short cShort;

	@Override
	public String toString() {
		return "O2M_S_INTEGER3 cShort " + this.cShort;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER3 x, O2M_S_INTEGER3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cShort.intValue() == y.cShort.intValue()));
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER4")
final class O2M_S_INTEGER4 implements O2M__S_Object<O2M_S_INTEGER4> {

	@Id
	@GeneratedValue
	Byte cByte;

	@Override
	public String toString() {
		return "O2M_S_INTEGER4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER4 x, O2M_S_INTEGER4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}
}

@Entity()
@Table(name = "O2M_S_INTEGER5")
final class O2M_S_INTEGER5 implements O2M__S_Object<O2M_S_INTEGER5> {

	@Id
	long tLong;

	O2M_S_INTEGER5() {
	}

	O2M_S_INTEGER5(long x) {
		this.tLong = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER5 tLong " + this.tLong;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER5 x, O2M_S_INTEGER5 y) {
		return x.tLong == y.tLong;
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER6")
final class O2M_S_INTEGER6 implements O2M__S_Object<O2M_S_INTEGER6> {

	@Id
	int tInteger;

	O2M_S_INTEGER6() {
	}

	O2M_S_INTEGER6(int x) {
		this.tInteger = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER6 tInteger " + this.tInteger;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER6 x, O2M_S_INTEGER6 y) {
		return x.tInteger == y.tInteger;
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER7")
final class O2M_S_INTEGER7 implements O2M__S_Object<O2M_S_INTEGER7> {

	@Id
	short tShort;

	O2M_S_INTEGER7() {
	}

	O2M_S_INTEGER7(short x) {
		this.tShort = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER7 tShort " + this.tShort;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER7 x, O2M_S_INTEGER7 y) {
		return x.tShort == y.tShort;
	}

}

@Entity()
@Table(name = "O2M_S_INTEGER8")
final class O2M_S_INTEGER8 implements O2M__S_Object<O2M_S_INTEGER8> {

	@Id
	byte tByte;

	O2M_S_INTEGER8() {
	}

	O2M_S_INTEGER8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_INTEGER8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(O2M_S_INTEGER8 x, O2M_S_INTEGER8 y) {
		return x.tByte == y.tByte;
	}

}
