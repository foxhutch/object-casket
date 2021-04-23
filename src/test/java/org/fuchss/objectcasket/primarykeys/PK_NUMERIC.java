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

	Long attr1;

	Long attr2;

	PK_NUMERIC1() {
	}

	PK_NUMERIC1(Long x) {
		this.cLong = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC1 x, PK_NUMERIC1 y) {
		return (((x.cLong == y.cLong) || ((x.cLong != null) && (y.cLong != null) && (x.cLong.longValue() == y.cLong.longValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.longValue() == y.attr1.longValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.longValue() == y.attr2.longValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC1 cLong = %s, attr1 = %s, attr2 = %s", "" + this.cLong, "" + this.attr1, "" + this.attr2);
	}
}

@Entity()
@Table(name = "PK_NUMERIC2")
final class PK_NUMERIC2 implements PK__Object<PK_NUMERIC2> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Integer cInteger;

	Integer attr1;

	Integer attr2;

	PK_NUMERIC2() {
	}

	PK_NUMERIC2(Integer x) {
		this.cInteger = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC2 x, PK_NUMERIC2 y) {
		return (((x.cInteger == y.cInteger) || ((x.cInteger != null) && (y.cInteger != null) && (x.cInteger.intValue() == y.cInteger.intValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.intValue() == y.attr1.intValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.intValue() == y.attr2.intValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC2 cInteger = %s, attr1 = %s, attr2 = %s", "" + this.cInteger, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_NUMERIC3")
final class PK_NUMERIC3 implements PK__Object<PK_NUMERIC3> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Short cShort;

	Short attr1;

	Short attr2;

	PK_NUMERIC3() {
	}

	PK_NUMERIC3(Short x) {
		this.cShort = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC3 x, PK_NUMERIC3 y) {
		return (((x.cShort == y.cShort) || ((x.cShort != null) && (y.cShort != null) && (x.cShort.shortValue() == y.cShort.shortValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.shortValue() == y.attr1.shortValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.shortValue() == y.attr2.shortValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC3 cShort = %s, attr1 = %s, attr2 = %s", "" + this.cShort, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_NUMERIC4")
final class PK_NUMERIC4 implements PK__Object<PK_NUMERIC4> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Byte cByte;

	Byte attr1;

	Byte attr2;

	PK_NUMERIC4() {
	}

	PK_NUMERIC4(Byte x) {
		this.cByte = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC4 x, PK_NUMERIC4 y) {
		return (((x.cByte == y.cByte) || ((x.cByte != null) && (y.cByte != null) && (x.cByte.byteValue() == y.cByte.byteValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.byteValue() == y.attr1.byteValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.byteValue() == y.attr2.byteValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC4 cByte = %s, attr1 = %s, attr2 = %s", "" + this.cByte, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_NUMERIC5")
final class PK_NUMERIC5 implements PK__Object<PK_NUMERIC5> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	long tLong;

	long attr1;

	PK_NUMERIC5() {
	}

	PK_NUMERIC5(long x) {
		this.tLong = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC5 x, PK_NUMERIC5 y) {
		return ((x.tLong == y.tLong) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC5 tLong = %s, attr1 = %s", "" + this.tLong, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_NUMERIC6")
final class PK_NUMERIC6 implements PK__Object<PK_NUMERIC6> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	int tInteger;

	int attr1;

	PK_NUMERIC6() {
	}

	PK_NUMERIC6(int x) {
		this.tInteger = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC6 x, PK_NUMERIC6 y) {
		return ((x.tInteger == y.tInteger) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC6 tInteger = %s, attr1 = %s", "" + this.tInteger, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_NUMERIC7")
final class PK_NUMERIC7 implements PK__Object<PK_NUMERIC7> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	short tShort;

	short attr1;

	PK_NUMERIC7() {
	}

	PK_NUMERIC7(short x) {
		this.tShort = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC7 x, PK_NUMERIC7 y) {
		return ((x.tShort == y.tShort) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC7 tInteger = %s, attr1 = %s", "" + this.tShort, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_NUMERIC8")
final class PK_NUMERIC8 implements PK__Object<PK_NUMERIC8> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	byte tByte;

	byte attr1;

	PK_NUMERIC8() {
	}

	PK_NUMERIC8(byte x) {
		this.tByte = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC8 x, PK_NUMERIC8 y) {
		return ((x.tByte == y.tByte) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC8 tByte = %s, attr1 = %s", "" + this.tByte, "" + this.attr1);
	}

}

@Entity()
@Table(name = "PK_NUMERIC9")
final class PK_NUMERIC9 implements PK__Object<PK_NUMERIC9> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Double cDouble;

	Double attr1;

	Double attr2;

	PK_NUMERIC9() {
	}

	PK_NUMERIC9(double x) {
		this.cDouble = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC9 x, PK_NUMERIC9 y) {
		return (((x.cDouble == y.cDouble) || ((x.cDouble != null) && (y.cDouble != null) && (x.cDouble.doubleValue() == y.cDouble.doubleValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.doubleValue() == y.attr1.doubleValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.doubleValue() == y.attr2.doubleValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC9 cDouble = %s, attr1 = %s, attr2 = %s", "" + this.cDouble, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_NUMERIC10")
final class PK_NUMERIC10 implements PK__Object<PK_NUMERIC10> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	double tDouble;

	double attr1;

	PK_NUMERIC10() {
	}

	PK_NUMERIC10(double x) {
		this.tDouble = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC10 x, PK_NUMERIC10 y) {
		return ((x.tDouble == y.tDouble) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC10 tDouble = %s, attr1 = %s", "" + this.tDouble, "" + this.attr1);
	}
}

@Entity()
@Table(name = "PK_NUMERIC11")
final class PK_NUMERIC11 implements PK__Object<PK_NUMERIC11> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	Float cFloat;

	Float attr1;

	Float attr2;

	PK_NUMERIC11() {
	}

	PK_NUMERIC11(float x) {
		this.cFloat = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC11 x, PK_NUMERIC11 y) {
		return (((x.cFloat == y.cFloat) || ((x.cFloat != null) && (y.cFloat != null) && (x.cFloat.floatValue() == y.cFloat.floatValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.floatValue() == y.attr1.floatValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.floatValue() == y.attr2.floatValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC11 cFloat = %s, attr1 = %s, attr2 = %s", "" + this.cFloat, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_NUMERIC12")
final class PK_NUMERIC12 implements PK__Object<PK_NUMERIC12> {

	@Id
	@Column(columnDefinition = "NUMERIC")
	float tFloat;

	float attr1;

	PK_NUMERIC12() {
	}

	PK_NUMERIC12(float x) {
		this.tFloat = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_NUMERIC12 x, PK_NUMERIC12 y) {
		return ((x.tFloat == y.tFloat) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_NUMERIC12 tFloat = %s, attr1 = %s", "" + this.tFloat, "" + this.attr1);
	}

}
