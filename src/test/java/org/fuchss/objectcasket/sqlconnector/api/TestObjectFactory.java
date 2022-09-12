package org.fuchss.objectcasket.sqlconnector.api;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

class TestObjectFactory {

	SqlObjectFactory factory = SqlPort.SQL_PORT.sqlObjectFactory();

	@Test
	void boxing() {
		Class<? extends Serializable> x = SqlObjectMaps.respectBoxing(null);
		Assertions.assertNull(x);
		x = SqlObjectMaps.respectBoxing(Character.TYPE);
		Assertions.assertEquals(x, Character.class);
		x = SqlObjectMaps.respectBoxing(Double.TYPE);
		Assertions.assertEquals(x, Double.class);
		x = SqlObjectMaps.respectBoxing(Float.TYPE);
		Assertions.assertEquals(x, Float.class);
		x = SqlObjectMaps.respectBoxing(Long.TYPE);
		Assertions.assertEquals(x, Long.class);
		x = SqlObjectMaps.respectBoxing(Integer.TYPE);
		Assertions.assertEquals(x, Integer.class);
		x = SqlObjectMaps.respectBoxing(Short.TYPE);
		Assertions.assertEquals(x, Short.class);
		x = SqlObjectMaps.respectBoxing(Byte.TYPE);
		Assertions.assertEquals(x, Byte.class);
		x = SqlObjectMaps.respectBoxing(Boolean.TYPE);
		Assertions.assertEquals(x, Boolean.class);

		x = SqlObjectMaps.respectBoxing(ArrayList.class);
		Assertions.assertEquals(x, ArrayList.class);

	}

	@Test
	void integerPrototypes() throws CasketException {

		SqlColumnSignature colSig = null;

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Long.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Long.class, Long.valueOf(1L));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Long.class, 1L);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Long.TYPE, 1L);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, Integer.valueOf(1));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Integer.class, 1);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Integer.TYPE, 1);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Short.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Short.class, Short.valueOf((short) 1));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Short.class, (short) 1);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Short.TYPE, (short) 1);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Byte.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Byte.class, Byte.valueOf((byte) 1));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Byte.class, (byte) 1);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Byte.TYPE, (byte) 1);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Boolean.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Boolean.class, Boolean.valueOf(true));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Boolean.class, true);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Boolean.TYPE, false);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Date.class, null);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.LONG, Date.class, new Date(1));
		Assertions.assertNotNull(colSig);

	}

	@Test
	void realPrototypes() throws CasketException {

		SqlColumnSignature colSig = null;

		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Double.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Double.class, Double.valueOf(1.1));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Double.class, 1.1);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Double.TYPE, 1.1);
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Float.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Float.class, Float.valueOf((float) 1.1));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Float.class, (float) 1.1);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.DOUBLE, Float.TYPE, (float) 1.1);
		Assertions.assertNotNull(colSig);

	}

	@Test
	void textPrototypes() throws CasketException {

		SqlColumnSignature colSig = null;

		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, Character.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, Character.class, Character.valueOf('a'));
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, Character.class, 'a');
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, Character.TYPE, 'a');
		Assertions.assertNotNull(colSig);

		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.TEXT, String.class, "ab");
		Assertions.assertNotNull(colSig);

	}

	@Test
	void blobPrototypes() throws CasketException {

		SqlColumnSignature colSig = null;
		ArrayList<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3));

		colSig = this.factory.mkColumnSignature(StorageClass.BLOB, ArrayList.class, null);
		Assertions.assertNotNull(colSig);
		colSig = this.factory.mkColumnSignature(StorageClass.BLOB, ArrayList.class, l);
		Assertions.assertNotNull(colSig);
	}

	@Test
	void integerSqlObjects() throws CasketException {

		SqlObject obj = null;
		obj = this.factory.mkSqlObject(StorageClass.LONG, null);
		this.checkConversionOfNullToInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, Long.valueOf(1L));
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, 1L);
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, Integer.valueOf(1));
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, 1);
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, Short.valueOf((short) 1));
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, (short) 1);
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, Byte.valueOf((byte) 1));
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, (byte) 1);
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, Boolean.valueOf(true));
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, true);
		this.checkConversionOfInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, false);
		this.checkConversionOfFalseToInt(obj);

		obj = this.factory.mkSqlObject(StorageClass.LONG, new Date(1));
		this.checkConversionOfInt(obj);
	}

	private void checkConversionOfInt(SqlObject obj) {
		Integer cInteger;
		Long cLong;
		Short cShort;
		Byte cByte;
		Boolean cBoolean;
		Date cDate;

		int tInteger;
		long tLong;
		short tShort;
		byte tByte;
		boolean tBoolean;

		Assertions.assertNotNull(obj);

		cInteger = obj.get(Integer.class);
		cLong = obj.get(Long.class);
		cShort = obj.get(Short.class);
		cByte = obj.get(Byte.class);
		cBoolean = obj.get(Boolean.class);
		cDate = obj.get(Date.class);
		tInteger = obj.get(Integer.TYPE);
		tLong = obj.get(Long.TYPE);
		tShort = obj.get(Short.TYPE);
		tByte = obj.get(Byte.TYPE);
		tBoolean = obj.get(Boolean.TYPE);

		Assertions.assertNotNull(cInteger);
		Assertions.assertNotNull(cLong);
		Assertions.assertNotNull(cShort);
		Assertions.assertNotNull(cByte);
		Assertions.assertNotNull(cBoolean);
		Assertions.assertNotNull(cDate);
		Assertions.assertEquals(cInteger, tInteger);
		Assertions.assertEquals(cLong, tLong);
		Assertions.assertEquals(cShort, tShort);
		Assertions.assertEquals(cByte, tByte);
		Assertions.assertEquals(cBoolean, tBoolean);
		Assertions.assertEquals(cDate, new Date(1));

	}

	private void checkConversionOfFalseToInt(SqlObject obj) {
		Integer cInteger;
		Long cLong;
		Short cShort;
		Byte cByte;
		Boolean cBoolean;
		Date cDate;

		int tInteger;
		long tLong;
		short tShort;
		byte tByte;
		boolean tBoolean;

		Assertions.assertNotNull(obj);

		cInteger = obj.get(Integer.class);
		cLong = obj.get(Long.class);
		cShort = obj.get(Short.class);
		cByte = obj.get(Byte.class);
		cBoolean = obj.get(Boolean.class);
		cDate = obj.get(Date.class);
		tInteger = obj.get(Integer.TYPE);
		tLong = obj.get(Long.TYPE);
		tShort = obj.get(Short.TYPE);
		tByte = obj.get(Byte.TYPE);
		tBoolean = obj.get(Boolean.TYPE);

		Assertions.assertNotNull(cInteger);
		Assertions.assertNotNull(cLong);
		Assertions.assertNotNull(cShort);
		Assertions.assertNotNull(cByte);
		Assertions.assertNotNull(cBoolean);
		Assertions.assertNotNull(cDate);
		Assertions.assertEquals(cInteger, tInteger);
		Assertions.assertEquals(cLong, tLong);
		Assertions.assertEquals(cShort, tShort);
		Assertions.assertEquals(cByte, tByte);
		Assertions.assertEquals(cBoolean, tBoolean);
		Assertions.assertEquals(cDate, new Date(0));

	}

	private void checkConversionOfNullToInt(SqlObject obj) {
		Integer cInteger;
		Long cLong;
		Short cShort;
		Byte cByte;
		Boolean cBoolean;
		Date cDate;

		int tInteger;
		long tLong;
		short tShort;
		byte tByte;
		boolean tBoolean;

		Assertions.assertNotNull(obj);

		cInteger = obj.get(Integer.class);
		cLong = obj.get(Long.class);
		cShort = obj.get(Short.class);
		cByte = obj.get(Byte.class);
		cBoolean = obj.get(Boolean.class);
		cDate = obj.get(Date.class);
		tInteger = obj.get(Integer.TYPE);
		tLong = obj.get(Long.TYPE);
		tShort = obj.get(Short.TYPE);
		tByte = obj.get(Byte.TYPE);
		tBoolean = obj.get(Boolean.TYPE);

		Assertions.assertNull(cInteger);
		Assertions.assertNull(cLong);
		Assertions.assertNull(cShort);
		Assertions.assertNull(cByte);
		Assertions.assertNull(cBoolean);
		Assertions.assertNull(cDate);

		Assertions.assertEquals(0, tInteger);
		Assertions.assertEquals(0, tLong);
		Assertions.assertEquals(0, tShort);
		Assertions.assertEquals(0, tByte);
		Assertions.assertFalse(tBoolean);

	}

	@Test
	void realSqlObjects() throws CasketException {

		SqlObject obj = null;

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, null);
		Assertions.assertNotNull(obj);
		this.checkConversionOfReal(obj, true, false);

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, Double.valueOf(1.1));
		Assertions.assertNotNull(obj);
		this.checkConversionOfReal(obj, false, false);

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, 1.1);
		Assertions.assertNotNull(obj);
		this.checkConversionOfReal(obj, false, false);

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, Float.valueOf((float) 1.1));
		Assertions.assertNotNull(obj);
		this.checkConversionOfReal(obj, false, true);

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, (float) 1.1);
		Assertions.assertNotNull(obj);
		this.checkConversionOfReal(obj, false, true);
	}

	private void checkConversionOfReal(SqlObject obj, boolean isNull, boolean wasFloat) {
		Double cDouble;
		Float cFloat;
		double tDouble;
		float tFloat;

		cDouble = obj.get(Double.class);
		cFloat = obj.get(Float.class);
		tDouble = obj.get(Double.TYPE);
		tFloat = obj.get(Float.TYPE);

		if (isNull) {
			Assertions.assertNull(cDouble);
			Assertions.assertNull(cFloat);
			Assertions.assertEquals(0.0, tDouble);
			Assertions.assertEquals(0.0, tFloat);

		} else {
			Assertions.assertNotNull(cDouble);
			Assertions.assertNotNull(cFloat);
			Assertions.assertEquals((wasFloat ? (double) ((float) 1.1) : 1.1), cDouble);
			Assertions.assertEquals(cFloat, (float) 1.1);
			Assertions.assertEquals((wasFloat ? (double) ((float) 1.1) : 1.1), tDouble);
			Assertions.assertEquals(tFloat, (float) 1.1);
		}
	}

	@Test
	void textSqlObjects() throws CasketException {

		SqlObject obj = null;

		obj = this.factory.mkSqlObject(StorageClass.TEXT, null);
		Assertions.assertNotNull(obj);
		this.checkConversionOfText(obj, true, false);

		obj = this.factory.mkSqlObject(StorageClass.TEXT, Character.valueOf('a'));
		Assertions.assertNotNull(obj);
		this.checkConversionOfText(obj, false, true);

		obj = this.factory.mkSqlObject(StorageClass.TEXT, 'a');
		Assertions.assertNotNull(obj);
		this.checkConversionOfText(obj, false, true);

		obj = this.factory.mkSqlObject(StorageClass.TEXT, "ab");
		Assertions.assertNotNull(obj);
		this.checkConversionOfText(obj, false, false);

	}

	private void checkConversionOfText(SqlObject obj, boolean isNull, boolean isChar) {
		Character cChar;
		String cString;

		char tChar;

		cChar = obj.get(Character.class);
		cString = obj.get(String.class);
		tChar = obj.get(Character.TYPE);

		if (isNull) {
			Assertions.assertNull(cChar);
			Assertions.assertNull(cString);
			Assertions.assertEquals(tChar, (char) 0);
		} else {
			Assertions.assertNotNull(cChar);
			Assertions.assertNotNull(cString);
			if (isChar) {
				Assertions.assertEquals('a', cChar);
				Assertions.assertEquals("a", cString);
				Assertions.assertEquals('a', tChar);
			} else {
				Assertions.assertEquals('a', cChar);
				Assertions.assertEquals("ab", cString);
				Assertions.assertEquals('a', tChar);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void blobSqlObjects() throws CasketException {

		SqlObject obj = null;
		ArrayList<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3));

		obj = this.factory.mkSqlObject(StorageClass.BLOB, null);
		Assertions.assertNotNull(obj);
		ArrayList<Integer> xl = obj.get(ArrayList.class);
		Assertions.assertNull(xl);

		obj = this.factory.mkSqlObject(StorageClass.BLOB, l);
		Assertions.assertNotNull(obj);
		xl = obj.get(ArrayList.class);
		Assertions.assertNotNull(xl);
		Assertions.assertEquals(xl.size(), l.size());
		for (int idx = 0; idx < l.size(); idx++) {
			Assertions.assertEquals(xl.get(idx), l.get(idx));
		}

	}

	@Test
	void duplicateSqlObjects() throws CasketException {

		SqlObject obj = null;
		SqlObject obj1 = null;
		ArrayList<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3));

		obj = this.factory.mkSqlObject(StorageClass.LONG, null);
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, Long.valueOf(1L));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, Integer.valueOf(1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, Short.valueOf((short) 1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, Byte.valueOf((byte) 1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, Boolean.valueOf(true));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.LONG, new Date(1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);

		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, null);
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, Double.valueOf(1.1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.DOUBLE, Float.valueOf((float) 1.1));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);

		obj = this.factory.mkSqlObject(StorageClass.TEXT, null);
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.TEXT, Character.valueOf('a'));
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.TEXT, "ab");
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);

		obj = this.factory.mkSqlObject(StorageClass.BLOB, null);
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);
		obj = this.factory.mkSqlObject(StorageClass.BLOB, l);
		obj1 = this.factory.duplicate(obj);
		Assertions.assertNotNull(obj1);

	}

}
