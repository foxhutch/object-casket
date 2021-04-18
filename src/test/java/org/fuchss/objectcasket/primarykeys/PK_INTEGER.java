package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_INTEGER1")
final class PK_INTEGER1 implements PK__Object<PK_INTEGER1> {

	@Id
	@GeneratedValue
	Long cLong;

	@Override
	public String toString() {
		return "PK_INTEGER1 cLong " + this.cLong;
	}

	@Override
	public boolean sameAs(PK_INTEGER1 x, PK_INTEGER1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

}

@Entity()
@Table(name = "PK_INTEGER2")
final class PK_INTEGER2 implements PK__Object<PK_INTEGER2> {

	@Id
	@GeneratedValue
	Integer cInteger;

	@Override
	public String toString() {
		return "PK_INTEGER2 cInteger " + this.cInteger;
	}

	@Override
	public boolean sameAs(PK_INTEGER2 x, PK_INTEGER2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cInteger.intValue() == y.cInteger.intValue()));
	}

}

@Entity()
@Table(name = "PK_INTEGER3")
final class PK_INTEGER3 implements PK__Object<PK_INTEGER3> {

	@Id
	@GeneratedValue
	Short cShort;

	@Override
	public String toString() {
		return "PK_INTEGER3 cShort " + this.cShort;
	}

	@Override
	public boolean sameAs(PK_INTEGER3 x, PK_INTEGER3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cShort.intValue() == y.cShort.intValue()));
	}

}

@Entity()
@Table(name = "PK_INTEGER4")
final class PK_INTEGER4 implements PK__Object<PK_INTEGER4> {

	@Id
	@GeneratedValue
	Byte cByte;

	@Override
	public String toString() {
		return "PK_INTEGER4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(PK_INTEGER4 x, PK_INTEGER4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}

}

@Entity()
@Table(name = "PK_INTEGER5")
final class PK_INTEGER5 implements PK__Object<PK_INTEGER5> {

	@Id
	long tLong;

	PK_INTEGER5() {
	}

	PK_INTEGER5(long x) {
		this.tLong = x;
	}

	@Override
	public String toString() {
		return "PK_INTEGER5 tLong " + this.tLong;
	}

	@Override
	public boolean sameAs(PK_INTEGER5 x, PK_INTEGER5 y) {
		return x.tLong == y.tLong;
	}

}

@Entity()
@Table(name = "PK_INTEGER6")
final class PK_INTEGER6 implements PK__Object<PK_INTEGER6> {

	@Id
	int tInteger;

	PK_INTEGER6() {
	}

	PK_INTEGER6(int x) {
		this.tInteger = x;
	}

	@Override
	public String toString() {
		return "PK_INTEGER6 tInteger " + this.tInteger;
	}

	@Override
	public boolean sameAs(PK_INTEGER6 x, PK_INTEGER6 y) {
		return x.tInteger == y.tInteger;
	}

}

@Entity()
@Table(name = "PK_INTEGER7")
final class PK_INTEGER7 implements PK__Object<PK_INTEGER7> {

	@Id
	short tShort;

	PK_INTEGER7() {
	}

	PK_INTEGER7(short x) {
		this.tShort = x;
	}

	@Override
	public String toString() {
		return "PK_INTEGER7 tShort " + this.tShort;
	}

	@Override
	public boolean sameAs(PK_INTEGER7 x, PK_INTEGER7 y) {
		return x.tShort == y.tShort;
	}

}

@Entity()
@Table(name = "PK_INTEGER8")
final class PK_INTEGER8 implements PK__Object<PK_INTEGER8> {

	@Id
	byte tByte;

	PK_INTEGER8() {
	}

	PK_INTEGER8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "PK_INTEGER8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(PK_INTEGER8 x, PK_INTEGER8 y) {
		return x.tByte == y.tByte;
	}

}
