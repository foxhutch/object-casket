package org.fuchss.objectcasket.o2m.fundamentals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_NUMERIC1")
final class O2M_S_NUMERIC1 implements O2M__S_Object<O2M_S_NUMERIC1> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Long cLong;

	O2M_S_NUMERIC1() {
	}

	O2M_S_NUMERIC1(long x) {
		this.cLong = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC1 cLong " + this.cLong;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC1 x, O2M_S_NUMERIC1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC2")
final class O2M_S_NUMERIC2 implements O2M__S_Object<O2M_S_NUMERIC2> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Integer cInteger;

	O2M_S_NUMERIC2() {
	}

	O2M_S_NUMERIC2(int x) {
		this.cInteger = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC2 cInteger " + this.cInteger;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC2 x, O2M_S_NUMERIC2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cInteger.intValue() == y.cInteger.intValue()));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC3")
final class O2M_S_NUMERIC3 implements O2M__S_Object<O2M_S_NUMERIC3> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Short cShort;

	O2M_S_NUMERIC3() {
	}

	O2M_S_NUMERIC3(short x) {
		this.cShort = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC3 cShort " + this.cShort;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC3 x, O2M_S_NUMERIC3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cShort.intValue() == y.cShort.intValue()));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC4")
final class O2M_S_NUMERIC4 implements O2M__S_Object<O2M_S_NUMERIC4> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Byte cByte;

	O2M_S_NUMERIC4() {
	}

	O2M_S_NUMERIC4(byte x) {
		this.cByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC4 x, O2M_S_NUMERIC4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC5")
final class O2M_S_NUMERIC5 implements O2M__S_Object<O2M_S_NUMERIC5> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	long tLong;

	O2M_S_NUMERIC5() {
	}

	O2M_S_NUMERIC5(long x) {
		this.tLong = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC5 tLong " + this.tLong;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC5 x, O2M_S_NUMERIC5 y) {
		return x.tLong == y.tLong;
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC6")
final class O2M_S_NUMERIC6 implements O2M__S_Object<O2M_S_NUMERIC6> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	int tInteger;

	O2M_S_NUMERIC6() {
	}

	O2M_S_NUMERIC6(int x) {
		this.tInteger = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC6 tInteger " + this.tInteger;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC6 x, O2M_S_NUMERIC6 y) {
		return x.tInteger == y.tInteger;
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC7")
final class O2M_S_NUMERIC7 implements O2M__S_Object<O2M_S_NUMERIC7> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	short tShort;

	O2M_S_NUMERIC7() {
	}

	O2M_S_NUMERIC7(short x) {
		this.tShort = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC7 tShort " + this.tShort;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC7 x, O2M_S_NUMERIC7 y) {
		return x.tShort == y.tShort;
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC8")
final class O2M_S_NUMERIC8 implements O2M__S_Object<O2M_S_NUMERIC8> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	byte tByte;

	O2M_S_NUMERIC8() {
	}

	O2M_S_NUMERIC8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC8 x, O2M_S_NUMERIC8 y) {
		return x.tByte == y.tByte;
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC9")
final class O2M_S_NUMERIC9 implements O2M__S_Object<O2M_S_NUMERIC9> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Double cDouble;

	O2M_S_NUMERIC9() {
	}

	O2M_S_NUMERIC9(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC9 x, O2M_S_NUMERIC9 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC9 cDouble " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC10")
final class O2M_S_NUMERIC10 implements O2M__S_Object<O2M_S_NUMERIC10> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	double tDouble;

	O2M_S_NUMERIC10() {
	}

	O2M_S_NUMERIC10(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC10 x, O2M_S_NUMERIC10 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC10 tDouble " + this.tDouble;
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC11")
final class O2M_S_NUMERIC11 implements O2M__S_Object<O2M_S_NUMERIC11> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Float cFloat;

	O2M_S_NUMERIC11() {
	}

	O2M_S_NUMERIC11(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC11 x, O2M_S_NUMERIC11 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC11 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}

@Entity()
@Table(name = "O2M_S_NUMERIC12")
final class O2M_S_NUMERIC12 implements O2M__S_Object<O2M_S_NUMERIC12> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	float tFloat;

	O2M_S_NUMERIC12() {
	}

	O2M_S_NUMERIC12(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(O2M_S_NUMERIC12 x, O2M_S_NUMERIC12 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "O2M_S_NUMERIC11 tFloat " + this.tFloat;
	}

}
