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

	Long attr1;

	Long attr2;

	PK_INTEGER1() {
	}

	PK_INTEGER1(Long x) {
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER1 x, PK_INTEGER1 y) {
		return (((x.cLong == y.cLong) || ((x.cLong != null) && (y.cLong != null) && (x.cLong.longValue() == y.cLong.longValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.longValue() == y.attr1.longValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.longValue() == y.attr2.longValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER1 cLong = %s, attr1 = %s, attr2 = %s", "" + this.cLong, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_INTEGER2")
final class PK_INTEGER2 implements PK__Object<PK_INTEGER2> {

	@Id
	@GeneratedValue
	Integer cInteger;

	Integer attr1;

	Integer attr2;

	PK_INTEGER2() {
	}

	PK_INTEGER2(Integer x) {
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER2 x, PK_INTEGER2 y) {
		return (((x.cInteger == y.cInteger) || ((x.cInteger != null) && (y.cInteger != null) && (x.cInteger.intValue() == y.cInteger.intValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.intValue() == y.attr1.intValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.intValue() == y.attr2.intValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER2 cInteger = %s, attr1 = %s, attr2 = %s", "" + this.cInteger, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_INTEGER3")
final class PK_INTEGER3 implements PK__Object<PK_INTEGER3> {

	@Id
	@GeneratedValue
	Short cShort;

	Short attr1;

	Short attr2;

	PK_INTEGER3() {
	}

	PK_INTEGER3(Short x) {
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER3 x, PK_INTEGER3 y) {
		return (((x.cShort == y.cShort) || ((x.cShort != null) && (y.cShort != null) && (x.cShort.shortValue() == y.cShort.shortValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.shortValue() == y.attr1.shortValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.shortValue() == y.attr2.shortValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER3 cShort = %s, attr1 = %s, attr2 = %s", "" + this.cShort, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_INTEGER4")
final class PK_INTEGER4 implements PK__Object<PK_INTEGER4> {

	@Id
	@GeneratedValue
	Byte cByte;

	Byte attr1;

	Byte attr2;

	PK_INTEGER4() {
	}

	PK_INTEGER4(Byte x) {
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER4 x, PK_INTEGER4 y) {
		return (((x.cByte == y.cByte) || ((x.cByte != null) && (y.cByte != null) && (x.cByte.byteValue() == y.cByte.byteValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.byteValue() == y.attr1.byteValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.byteValue() == y.attr2.byteValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER4 cByte = %s, attr1 = %s, attr2 = %s", "" + this.cByte, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_INTEGER5")
final class PK_INTEGER5 implements PK__Object<PK_INTEGER5> {

	@Id
	long tLong;

	long attr1;

	PK_INTEGER5() {
	}

	PK_INTEGER5(long x) {
		this.tLong = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER5 x, PK_INTEGER5 y) {
		return ((x.tLong == y.tLong) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER5 tLong = %s, attr1 = %s", "" + this.tLong, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_INTEGER6")
final class PK_INTEGER6 implements PK__Object<PK_INTEGER6> {

	@Id
	int tInteger;

	int attr1;

	PK_INTEGER6() {
	}

	PK_INTEGER6(int x) {
		this.tInteger = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER6 x, PK_INTEGER6 y) {
		return ((x.tInteger == y.tInteger) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER6 tInteger = %s, attr1 = %s", "" + this.tInteger, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_INTEGER7")
final class PK_INTEGER7 implements PK__Object<PK_INTEGER7> {

	@Id
	short tShort;

	short attr1;

	PK_INTEGER7() {
	}

	PK_INTEGER7(short x) {
		this.tShort = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER7 x, PK_INTEGER7 y) {
		return ((x.tShort == y.tShort) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER7 tInteger = %s, attr1 = %s", "" + this.tShort, "" + this.attr1);
	}
}

@Entity()
@Table(name = "PK_INTEGER8")
final class PK_INTEGER8 implements PK__Object<PK_INTEGER8> {

	@Id
	byte tByte;

	byte attr1;

	PK_INTEGER8() {
	}

	PK_INTEGER8(byte x) {
		this.tByte = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_INTEGER8 x, PK_INTEGER8 y) {
		return ((x.tByte == y.tByte) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_INTEGER8 tByte = %s, attr1 = %s", "" + this.tByte, "" + this.attr1);
	}

}
