package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_NUMERIC1")
final class PK_NUMERIC1 implements PK__Object<PK_NUMERIC1> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Long cLong;

	PK_NUMERIC1() {
	}

	PK_NUMERIC1(long x) {
		this.cLong = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC1 cLong " + this.cLong;
	}

	@Override
	public boolean sameAs(PK_NUMERIC1 x, PK_NUMERIC1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cLong.longValue() == y.cLong.longValue()));
	}

}

@Entity()
@Table(name = "PK_NUMERIC2")
final class PK_NUMERIC2 implements PK__Object<PK_NUMERIC2> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Integer cInteger;

	PK_NUMERIC2() {
	}

	PK_NUMERIC2(int x) {
		this.cInteger = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC2 cInteger " + this.cInteger;
	}

	@Override
	public boolean sameAs(PK_NUMERIC2 x, PK_NUMERIC2 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cInteger.intValue() == y.cInteger.intValue()));
	}

}

@Entity()
@Table(name = "PK_NUMERIC3")
final class PK_NUMERIC3 implements PK__Object<PK_NUMERIC3> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Short cShort;

	PK_NUMERIC3() {
	}

	PK_NUMERIC3(short x) {
		this.cShort = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC3 cShort " + this.cShort;
	}

	@Override
	public boolean sameAs(PK_NUMERIC3 x, PK_NUMERIC3 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cShort.intValue() == y.cShort.intValue()));
	}

}

@Entity()
@Table(name = "PK_NUMERIC4")
final class PK_NUMERIC4 implements PK__Object<PK_NUMERIC4> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Byte cByte;

	PK_NUMERIC4() {
	}

	PK_NUMERIC4(byte x) {
		this.cByte = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC4 cByte " + this.cByte;
	}

	@Override
	public boolean sameAs(PK_NUMERIC4 x, PK_NUMERIC4 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cByte.intValue() == y.cByte.intValue()));
	}

}

@Entity()
@Table(name = "PK_NUMERIC5")
final class PK_NUMERIC5 implements PK__Object<PK_NUMERIC5> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	long tLong;

	PK_NUMERIC5() {
	}

	PK_NUMERIC5(long x) {
		this.tLong = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC5 tLong " + this.tLong;
	}

	@Override
	public boolean sameAs(PK_NUMERIC5 x, PK_NUMERIC5 y) {
		return x.tLong == y.tLong;
	}

}

@Entity()
@Table(name = "PK_NUMERIC6")
final class PK_NUMERIC6 implements PK__Object<PK_NUMERIC6> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	int tInteger;

	PK_NUMERIC6() {
	}

	PK_NUMERIC6(int x) {
		this.tInteger = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC6 tInteger " + this.tInteger;
	}

	@Override
	public boolean sameAs(PK_NUMERIC6 x, PK_NUMERIC6 y) {
		return x.tInteger == y.tInteger;
	}

}

@Entity()
@Table(name = "PK_NUMERIC7")
final class PK_NUMERIC7 implements PK__Object<PK_NUMERIC7> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	short tShort;

	PK_NUMERIC7() {
	}

	PK_NUMERIC7(short x) {
		this.tShort = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC7 tShort " + this.tShort;
	}

	@Override
	public boolean sameAs(PK_NUMERIC7 x, PK_NUMERIC7 y) {
		return x.tShort == y.tShort;
	}

}

@Entity()
@Table(name = "PK_NUMERIC8")
final class PK_NUMERIC8 implements PK__Object<PK_NUMERIC8> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	byte tByte;

	PK_NUMERIC8() {
	}

	PK_NUMERIC8(byte x) {
		this.tByte = x;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC8 tByte " + this.tByte;
	}

	@Override
	public boolean sameAs(PK_NUMERIC8 x, PK_NUMERIC8 y) {
		return x.tByte == y.tByte;
	}

}

@Entity()
@Table(name = "PK_NUMERIC9")
final class PK_NUMERIC9 implements PK__Object<PK_NUMERIC9> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Double cDouble;

	PK_NUMERIC9() {
	}

	PK_NUMERIC9(double x) {
		this.cDouble = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC9 x, PK_NUMERIC9 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()));
	}

	@Override
	public String toString() {
		return "PK_NUMERIC9 cDouble " + (this.cDouble == null ? "NULL" : ("" + this.cDouble));
	}

}

@Entity()
@Table(name = "PK_NUMERIC10")
final class PK_NUMERIC10 implements PK__Object<PK_NUMERIC10> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	double tDouble;

	PK_NUMERIC10() {
	}

	PK_NUMERIC10(double x) {
		this.tDouble = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC10 x, PK_NUMERIC10 y) {
		return x.tDouble == y.tDouble;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC10 tDouble " + this.tDouble;
	}

}

@Entity()
@Table(name = "PK_NUMERIC11")
final class PK_NUMERIC11 implements PK__Object<PK_NUMERIC11> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Float cFloat;

	PK_NUMERIC11() {
	}

	PK_NUMERIC11(float x) {
		this.cFloat = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC11 x, PK_NUMERIC11 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()));
	}

	@Override
	public String toString() {
		return "PK_NUMERIC11 cFloat " + (this.cFloat == null ? "NULL" : ("" + this.cFloat));
	}

}

@Entity()
@Table(name = "PK_NUMERIC12")
final class PK_NUMERIC12 implements PK__Object<PK_NUMERIC12> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	float tFloat;

	PK_NUMERIC12() {
	}

	PK_NUMERIC12(float x) {
		this.tFloat = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC12 x, PK_NUMERIC12 y) {
		return x.tFloat == y.tFloat;
	}

	@Override
	public String toString() {
		return "PK_NUMERIC11 tFloat " + this.tFloat;
	}

}
