package org.fuchss.objectcasket.o2m;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_C_BOOL1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_C_BOOL2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_C_CHAR1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_C_CHAR2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_BOOL1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_BOOL2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_CHAR1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_CHAR2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_DATE;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_DOUBLE1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_DOUBLE2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_FLOAT1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_FLOAT2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER3;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER4;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER5;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER6;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER7;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_INTEGER8;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC1;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC10;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC11;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC12;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC2;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC3;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC4;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC5;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC6;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC7;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC8;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_NUMERIC9;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_TEXT;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_TIMESTAMP;
import org.fuchss.objectcasket.o2m.objects.m2o.M2O_S_VARCHAR;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestM2O extends TestBase {

	static final int ROWS = 10;

	@Test
	public void testBOOL() throws Exception {

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				M2O_C_BOOL1.class, //
				M2O_C_BOOL2.class, //
				M2O_C_CHAR1.class, //
				M2O_C_CHAR2.class, //
				M2O_S_BOOL1.class, //
				M2O_S_BOOL2.class, //
				M2O_S_INTEGER1.class, //
				M2O_S_INTEGER2.class, //
				M2O_S_INTEGER3.class, //
				M2O_S_INTEGER4.class, //
				M2O_S_INTEGER5.class, //
				M2O_S_INTEGER6.class, //
				M2O_S_INTEGER7.class, //
				M2O_S_INTEGER8.class, //
				M2O_S_DATE.class, //
				M2O_S_TIMESTAMP.class, //
				M2O_S_CHAR1.class, //
				M2O_S_CHAR2.class, //
				M2O_S_DOUBLE1.class, //
				M2O_S_DOUBLE2.class, //
				M2O_S_FLOAT1.class, //
				M2O_S_FLOAT2.class, //
				M2O_S_NUMERIC1.class, //
				M2O_S_NUMERIC2.class, //
				M2O_S_NUMERIC3.class, //
				M2O_S_NUMERIC4.class, //
				M2O_S_NUMERIC5.class, //
				M2O_S_NUMERIC6.class, //
				M2O_S_NUMERIC7.class, //
				M2O_S_NUMERIC8.class, //
				M2O_S_NUMERIC9.class, //
				M2O_S_NUMERIC10.class, //
				M2O_S_NUMERIC11.class, //
				M2O_S_NUMERIC12.class, //
				M2O_S_TEXT.class, //
				M2O_S_VARCHAR.class);
		session.open();

		Set<M2O_C_BOOL1> sql_c1 = new HashSet<>();
		M2O_C_BOOL1 sql_c1_obj = null;

		Set<M2O_C_BOOL2> sql_c2 = new HashSet<>();
		M2O_C_BOOL2 sql_c2_obj = null;

		Set<M2O_S_BOOL1> sql_b1_c1 = new HashSet<>();
		Set<M2O_S_BOOL1> sql_b1_c2 = new HashSet<>();
		Set<M2O_S_BOOL2> sql_b2_c1 = new HashSet<>();
		Set<M2O_S_BOOL2> sql_b2_c2 = new HashSet<>();

		Set<M2O_S_INTEGER1> sql_i1_c1 = new HashSet<>();
		Set<M2O_S_INTEGER1> sql_i1_c2 = new HashSet<>();
		Set<M2O_S_INTEGER2> sql_i2_c1 = new HashSet<>();
		Set<M2O_S_INTEGER2> sql_i2_c2 = new HashSet<>();
		Set<M2O_S_INTEGER3> sql_i3_c1 = new HashSet<>();
		Set<M2O_S_INTEGER3> sql_i3_c2 = new HashSet<>();
		Set<M2O_S_INTEGER4> sql_i4_c1 = new HashSet<>();
		Set<M2O_S_INTEGER4> sql_i4_c2 = new HashSet<>();
		Set<M2O_S_INTEGER5> sql_i5_c1 = new HashSet<>();
		Set<M2O_S_INTEGER5> sql_i5_c2 = new HashSet<>();
		Set<M2O_S_INTEGER6> sql_i6_c1 = new HashSet<>();
		Set<M2O_S_INTEGER6> sql_i6_c2 = new HashSet<>();
		Set<M2O_S_INTEGER7> sql_i7_c1 = new HashSet<>();
		Set<M2O_S_INTEGER7> sql_i7_c2 = new HashSet<>();
		Set<M2O_S_INTEGER8> sql_i8_c1 = new HashSet<>();
		Set<M2O_S_INTEGER8> sql_i8_c2 = new HashSet<>();

		Set<M2O_S_DATE> sql_date_c1 = new HashSet<>();
		Set<M2O_S_DATE> sql_date_c2 = new HashSet<>();

		Set<M2O_S_TIMESTAMP> sql_time_c1 = new HashSet<>();
		Set<M2O_S_TIMESTAMP> sql_time_c2 = new HashSet<>();

		Set<M2O_S_CHAR1> sql_c1_c1 = new HashSet<>();
		Set<M2O_S_CHAR1> sql_c1_c2 = new HashSet<>();
		Set<M2O_S_CHAR2> sql_c2_c1 = new HashSet<>();
		Set<M2O_S_CHAR2> sql_c2_c2 = new HashSet<>();

		Set<M2O_S_DOUBLE1> sql_d1_c1 = new HashSet<>();
		Set<M2O_S_DOUBLE1> sql_d1_c2 = new HashSet<>();
		Set<M2O_S_DOUBLE2> sql_d2_c1 = new HashSet<>();
		Set<M2O_S_DOUBLE2> sql_d2_c2 = new HashSet<>();

		Set<M2O_S_FLOAT1> sql_f1_c1 = new HashSet<>();
		Set<M2O_S_FLOAT1> sql_f1_c2 = new HashSet<>();
		Set<M2O_S_FLOAT2> sql_f2_c1 = new HashSet<>();
		Set<M2O_S_FLOAT2> sql_f2_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC1> sql_n1_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC1> sql_n1_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC2> sql_n2_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC2> sql_n2_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC3> sql_n3_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC3> sql_n3_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC4> sql_n4_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC4> sql_n4_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC5> sql_n5_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC5> sql_n5_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC6> sql_n6_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC6> sql_n6_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC7> sql_n7_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC7> sql_n7_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC8> sql_n8_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC8> sql_n8_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC9> sql_n9_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC9> sql_n9_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC10> sql_n10_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC10> sql_n10_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC11> sql_n11_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC11> sql_n11_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC12> sql_n12_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC12> sql_n12_c2 = new HashSet<>();

		Set<M2O_S_TEXT> sql_t_c1 = new HashSet<>();
		Set<M2O_S_TEXT> sql_t_c2 = new HashSet<>();

		Set<M2O_S_VARCHAR> sql_v_c1 = new HashSet<>();
		Set<M2O_S_VARCHAR> sql_v_c2 = new HashSet<>();

		M2O_S_BOOL1 o2m_s_b1 = null;
		sql_b1_c1.add(o2m_s_b1 = new M2O_S_BOOL1(true));
		sql_b1_c2.add(o2m_s_b1 = new M2O_S_BOOL1(false));

		M2O_S_BOOL2 o2m_s_b2 = null;
		sql_b2_c1.add(o2m_s_b2 = new M2O_S_BOOL2(true));
		sql_b2_c2.add(o2m_s_b2 = new M2O_S_BOOL2(false));

		M2O_S_INTEGER1 o2m_s_i1 = null;
		M2O_S_INTEGER2 o2m_s_i2 = null;
		M2O_S_INTEGER3 o2m_s_i3 = null;
		M2O_S_INTEGER4 o2m_s_i4 = null;
		M2O_S_INTEGER5 o2m_s_i5 = null;
		M2O_S_INTEGER6 o2m_s_i6 = null;
		M2O_S_INTEGER7 o2m_s_i7 = null;
		M2O_S_INTEGER8 o2m_s_i8 = null;

		M2O_S_DATE o2m_s_date = null;

		M2O_S_TIMESTAMP o2m_s_time = null;

		M2O_S_CHAR1 o2m_s_c1 = null;
		M2O_S_CHAR2 o2m_s_c2 = null;

		M2O_S_DOUBLE1 o2m_s_d1 = null;
		M2O_S_DOUBLE2 o2m_s_d2 = null;

		M2O_S_FLOAT1 o2m_s_f1 = null;
		M2O_S_FLOAT2 o2m_s_f2 = null;

		M2O_S_NUMERIC1 o2m_s_n1 = null;
		M2O_S_NUMERIC2 o2m_s_n2 = null;
		M2O_S_NUMERIC3 o2m_s_n3 = null;
		M2O_S_NUMERIC4 o2m_s_n4 = null;
		M2O_S_NUMERIC5 o2m_s_n5 = null;

		M2O_S_NUMERIC6 o2m_s_n6 = null;

		M2O_S_NUMERIC7 o2m_s_n7 = null;

		M2O_S_NUMERIC8 o2m_s_n8 = null;

		M2O_S_NUMERIC9 o2m_s_n9 = null;
		M2O_S_NUMERIC10 o2m_s_n10 = null;

		M2O_S_NUMERIC11 o2m_s_n11 = null;
		M2O_S_NUMERIC12 o2m_s_n12 = null;

		M2O_S_TEXT o2m_s_t = null;

		M2O_S_VARCHAR o2m_s_v = null;

		for (int i = 0; i < ROWS; i++) {

			sql_i1_c1.add(o2m_s_i1 = new M2O_S_INTEGER1());
			sql_i1_c2.add(o2m_s_i1 = new M2O_S_INTEGER1());
			sql_i2_c1.add(o2m_s_i2 = new M2O_S_INTEGER2());
			sql_i2_c2.add(o2m_s_i2 = new M2O_S_INTEGER2());
			sql_i3_c1.add(o2m_s_i3 = new M2O_S_INTEGER3());
			sql_i3_c2.add(o2m_s_i3 = new M2O_S_INTEGER3());
			sql_i4_c1.add(o2m_s_i4 = new M2O_S_INTEGER4());
			sql_i4_c2.add(o2m_s_i4 = new M2O_S_INTEGER4());
			sql_i5_c1.add(o2m_s_i5 = new M2O_S_INTEGER5(i + 1));
			sql_i5_c2.add(o2m_s_i5 = new M2O_S_INTEGER5(ROWS + 1 + i));
			sql_i6_c1.add(o2m_s_i6 = new M2O_S_INTEGER6(i + 1));
			sql_i6_c2.add(o2m_s_i6 = new M2O_S_INTEGER6(ROWS + i + 1));
			sql_i7_c1.add(o2m_s_i7 = new M2O_S_INTEGER7((short) (i + 1)));
			sql_i7_c2.add(o2m_s_i7 = new M2O_S_INTEGER7((short) (ROWS + i + 1)));
			sql_i8_c1.add(o2m_s_i8 = new M2O_S_INTEGER8((byte) (i + 1)));
			sql_i8_c2.add(o2m_s_i8 = new M2O_S_INTEGER8((byte) (ROWS + i + 1)));

			cal.add(Calendar.DAY_OF_YEAR, 1);
			sql_date_c1.add(o2m_s_date = new M2O_S_DATE(cal.getTime()));
			sql_time_c1.add(o2m_s_time = new M2O_S_TIMESTAMP(cal.getTime()));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			sql_date_c2.add(o2m_s_date = new M2O_S_DATE(cal.getTime()));
			sql_time_c2.add(o2m_s_time = new M2O_S_TIMESTAMP(cal.getTime()));

			sql_c1_c1.add(o2m_s_c1 = new M2O_S_CHAR1((char) (65 + i)));
			sql_c1_c2.add(o2m_s_c1 = new M2O_S_CHAR1((char) (97 + i)));
			sql_c2_c1.add(o2m_s_c2 = new M2O_S_CHAR2((char) (65 + i)));
			sql_c2_c2.add(o2m_s_c2 = new M2O_S_CHAR2((char) (97 + i)));

			sql_d1_c1.add(o2m_s_d1 = new M2O_S_DOUBLE1(0.1 * (i + 1)));
			sql_d1_c2.add(o2m_s_d1 = new M2O_S_DOUBLE1(0.1 * (ROWS + i + 1)));
			sql_d2_c1.add(o2m_s_d2 = new M2O_S_DOUBLE2(0.1 * (i + 1)));
			sql_d2_c2.add(o2m_s_d2 = new M2O_S_DOUBLE2(0.1 * (ROWS + i + 1)));

			sql_f1_c1.add(o2m_s_f1 = new M2O_S_FLOAT1((float) (0.1 * (i + 1))));
			sql_f1_c2.add(o2m_s_f1 = new M2O_S_FLOAT1((float) (0.1 * (ROWS + i + 1))));
			sql_f2_c1.add(o2m_s_f2 = new M2O_S_FLOAT2((float) (0.1 * (i + 1))));
			sql_f2_c2.add(o2m_s_f2 = new M2O_S_FLOAT2((float) (0.1 * (ROWS + i + 1))));

			sql_n1_c1.add(o2m_s_n1 = new M2O_S_NUMERIC1(i + 1));
			sql_n1_c2.add(o2m_s_n1 = new M2O_S_NUMERIC1(ROWS + 1 + i));
			sql_n2_c1.add(o2m_s_n2 = new M2O_S_NUMERIC2(i + 1));
			sql_n2_c2.add(o2m_s_n2 = new M2O_S_NUMERIC2(ROWS + 1 + i));
			sql_n3_c1.add(o2m_s_n3 = new M2O_S_NUMERIC3((short) (i + 1)));
			sql_n3_c2.add(o2m_s_n3 = new M2O_S_NUMERIC3((short) (ROWS + 1 + i)));
			sql_n4_c1.add(o2m_s_n4 = new M2O_S_NUMERIC4((byte) (i + 1)));
			sql_n4_c2.add(o2m_s_n4 = new M2O_S_NUMERIC4((byte) (ROWS + 1 + i)));
			sql_n5_c1.add(o2m_s_n5 = new M2O_S_NUMERIC5(i + 1));
			sql_n5_c2.add(o2m_s_n5 = new M2O_S_NUMERIC5(ROWS + 1 + i));
			sql_n6_c1.add(o2m_s_n6 = new M2O_S_NUMERIC6(i + 1));
			sql_n6_c2.add(o2m_s_n6 = new M2O_S_NUMERIC6(ROWS + i + 1));

			sql_n7_c1.add(o2m_s_n7 = new M2O_S_NUMERIC7((short) (i + 1)));
			sql_n7_c2.add(o2m_s_n7 = new M2O_S_NUMERIC7((short) (ROWS + i + 1)));

			sql_n8_c1.add(o2m_s_n8 = new M2O_S_NUMERIC8((byte) (i + 1)));
			sql_n8_c2.add(o2m_s_n8 = new M2O_S_NUMERIC8((byte) (ROWS + i + 1)));

			sql_n9_c1.add(o2m_s_n9 = new M2O_S_NUMERIC9(0.1 * (i + 1)));
			sql_n9_c2.add(o2m_s_n9 = new M2O_S_NUMERIC9(0.1 * (ROWS + 1 + i)));
			sql_n10_c1.add(o2m_s_n10 = new M2O_S_NUMERIC10(0.1 * (i + 1)));
			sql_n10_c2.add(o2m_s_n10 = new M2O_S_NUMERIC10(0.1 * (ROWS + i + 1)));

			sql_n11_c1.add(o2m_s_n11 = new M2O_S_NUMERIC11((float) (0.1 * (i + 1))));
			sql_n11_c2.add(o2m_s_n11 = new M2O_S_NUMERIC11((float) (0.1 * (ROWS + i + 1))));
			sql_n12_c1.add(o2m_s_n12 = new M2O_S_NUMERIC12((float) (0.1 * (i + 1))));
			sql_n12_c2.add(o2m_s_n12 = new M2O_S_NUMERIC12((float) (0.1 * (ROWS + i + 1))));

			sql_t_c1.add(o2m_s_t = new M2O_S_TEXT("" + (i + 1)));
			sql_t_c2.add(o2m_s_t = new M2O_S_TEXT("" + (ROWS + i + 1)));

			sql_v_c1.add(o2m_s_v = new M2O_S_VARCHAR("" + (i + 1)));
			sql_v_c2.add(o2m_s_v = new M2O_S_VARCHAR("" + (ROWS + i + 1)));

		}

		sql_c1_obj = new M2O_C_BOOL1(true);
		sql_c2_obj = new M2O_C_BOOL2(false);

		sql_c1_obj.add(sql_b1_c1, o2m_s_b1);
		sql_c1_obj.add(sql_b2_c1, o2m_s_b2);
		sql_c2_obj.add(sql_b1_c2, o2m_s_b1);
		sql_c2_obj.add(sql_b2_c2, o2m_s_b2);

		sql_c1_obj.add(sql_i1_c1, o2m_s_i1);
		sql_c1_obj.add(sql_i2_c1, o2m_s_i2);
		sql_c1_obj.add(sql_i3_c1, o2m_s_i3);
		sql_c1_obj.add(sql_i4_c1, o2m_s_i4);
		sql_c1_obj.add(sql_i5_c1, o2m_s_i5);
		sql_c1_obj.add(sql_i6_c1, o2m_s_i6);
		sql_c1_obj.add(sql_i7_c1, o2m_s_i7);
		sql_c1_obj.add(sql_i8_c1, o2m_s_i8);
		sql_c2_obj.add(sql_i1_c2, o2m_s_i1);
		sql_c2_obj.add(sql_i2_c2, o2m_s_i2);
		sql_c2_obj.add(sql_i3_c2, o2m_s_i3);
		sql_c2_obj.add(sql_i4_c2, o2m_s_i4);
		sql_c2_obj.add(sql_i5_c2, o2m_s_i5);
		sql_c2_obj.add(sql_i6_c2, o2m_s_i6);
		sql_c2_obj.add(sql_i7_c2, o2m_s_i7);
		sql_c2_obj.add(sql_i8_c2, o2m_s_i8);

		sql_c1_obj.add(sql_date_c1, o2m_s_date);
		sql_c1_obj.add(sql_time_c1, o2m_s_time);
		sql_c2_obj.add(sql_date_c2, o2m_s_date);
		sql_c2_obj.add(sql_time_c2, o2m_s_time);

		sql_c1_obj.add(sql_c1_c1, o2m_s_c1);
		sql_c1_obj.add(sql_c2_c1, o2m_s_c2);
		sql_c2_obj.add(sql_c1_c2, o2m_s_c1);
		sql_c2_obj.add(sql_c2_c2, o2m_s_c2);

		sql_c1_obj.add(sql_d1_c1, o2m_s_d1);
		sql_c1_obj.add(sql_d2_c1, o2m_s_d2);
		sql_c2_obj.add(sql_d1_c2, o2m_s_d1);
		sql_c2_obj.add(sql_d2_c2, o2m_s_d2);

		sql_c1_obj.add(sql_f1_c1, o2m_s_f1);
		sql_c1_obj.add(sql_f2_c1, o2m_s_f2);
		sql_c2_obj.add(sql_f1_c2, o2m_s_f1);
		sql_c2_obj.add(sql_f2_c2, o2m_s_f2);

		sql_c1_obj.add(sql_n1_c1, o2m_s_n1);
		sql_c1_obj.add(sql_n2_c1, o2m_s_n2);
		sql_c1_obj.add(sql_n3_c1, o2m_s_n3);
		sql_c1_obj.add(sql_n4_c1, o2m_s_n4);
		sql_c1_obj.add(sql_n5_c1, o2m_s_n5);
		sql_c1_obj.add(sql_n6_c1, o2m_s_n6);
		sql_c1_obj.add(sql_n7_c1, o2m_s_n7);
		sql_c1_obj.add(sql_n8_c1, o2m_s_n8);
		sql_c1_obj.add(sql_n9_c1, o2m_s_n9);
		sql_c1_obj.add(sql_n10_c1, o2m_s_n10);
		sql_c1_obj.add(sql_n11_c1, o2m_s_n11);
		sql_c1_obj.add(sql_n12_c1, o2m_s_n12);
		sql_c2_obj.add(sql_n1_c2, o2m_s_n1);
		sql_c2_obj.add(sql_n2_c2, o2m_s_n2);
		sql_c2_obj.add(sql_n3_c2, o2m_s_n3);
		sql_c2_obj.add(sql_n4_c2, o2m_s_n4);
		sql_c2_obj.add(sql_n5_c2, o2m_s_n5);
		sql_c2_obj.add(sql_n6_c2, o2m_s_n6);

		sql_c2_obj.add(sql_n7_c2, o2m_s_n7);

		sql_c2_obj.add(sql_n8_c2, o2m_s_n8);

		sql_c2_obj.add(sql_n9_c2, o2m_s_n9);
		sql_c2_obj.add(sql_n10_c2, o2m_s_n10);

		sql_c2_obj.add(sql_n11_c2, o2m_s_n11);
		sql_c2_obj.add(sql_n12_c2, o2m_s_n12);

		sql_c1_obj.add(sql_t_c1, o2m_s_t);
		sql_c2_obj.add(sql_t_c2, o2m_s_t);

		sql_c1_obj.add(sql_v_c1, o2m_s_v);
		sql_c2_obj.add(sql_v_c2, o2m_s_v);

		session.persist(sql_c1_obj);
		sql_c1.add(sql_c1_obj);

		session.persist(sql_c2_obj);
		sql_c2.add(sql_c2_obj);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				M2O_C_BOOL1.class, //
				M2O_C_BOOL2.class, //
				M2O_C_CHAR1.class, //
				M2O_C_CHAR2.class, //
				M2O_S_BOOL1.class, //
				M2O_S_BOOL2.class, //
				M2O_S_INTEGER1.class, //
				M2O_S_INTEGER2.class, //
				M2O_S_INTEGER3.class, //
				M2O_S_INTEGER4.class, //
				M2O_S_INTEGER5.class, //
				M2O_S_INTEGER6.class, //
				M2O_S_INTEGER7.class, //
				M2O_S_INTEGER8.class, //
				M2O_S_DATE.class, //
				M2O_S_TIMESTAMP.class, //
				M2O_S_CHAR1.class, //
				M2O_S_CHAR2.class, //
				M2O_S_DOUBLE1.class, //
				M2O_S_DOUBLE2.class, //
				M2O_S_FLOAT1.class, //
				M2O_S_FLOAT2.class, //
				M2O_S_NUMERIC1.class, //
				M2O_S_NUMERIC2.class, //
				M2O_S_NUMERIC3.class, //
				M2O_S_NUMERIC4.class, //
				M2O_S_NUMERIC5.class, //
				M2O_S_NUMERIC6.class, //
				M2O_S_NUMERIC7.class, //
				M2O_S_NUMERIC8.class, //
				M2O_S_NUMERIC9.class, //
				M2O_S_NUMERIC10.class, //
				M2O_S_NUMERIC11.class, //
				M2O_S_NUMERIC12.class, //
				M2O_S_TEXT.class, //
				M2O_S_VARCHAR.class);
		session.open();

		Set<M2O_C_BOOL1> sql_1 = session.getAllObjects(M2O_C_BOOL1.class);
		Set<M2O_C_BOOL2> sql_2 = session.getAllObjects(M2O_C_BOOL2.class);

		Assert.assertTrue(sql_c1_obj.check(sql_c1, sql_1));
		Assert.assertTrue(sql_c2_obj.check(sql_c2, sql_2));

		this.storePort.sessionManager().terminate(session);

	}

	@Test
	public void testCHAR() throws Exception {

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				M2O_C_BOOL1.class, //
				M2O_C_BOOL2.class, //
				M2O_C_CHAR1.class, //
				M2O_C_CHAR2.class, //
				M2O_S_BOOL1.class, //
				M2O_S_BOOL2.class, //
				M2O_S_INTEGER1.class, //
				M2O_S_INTEGER2.class, //
				M2O_S_INTEGER3.class, //
				M2O_S_INTEGER4.class, //
				M2O_S_INTEGER5.class, //
				M2O_S_INTEGER6.class, //
				M2O_S_INTEGER7.class, //
				M2O_S_INTEGER8.class, //
				M2O_S_DATE.class, //
				M2O_S_TIMESTAMP.class, //
				M2O_S_CHAR1.class, //
				M2O_S_CHAR2.class, //
				M2O_S_DOUBLE1.class, //
				M2O_S_DOUBLE2.class, //
				M2O_S_FLOAT1.class, //
				M2O_S_FLOAT2.class, //
				M2O_S_NUMERIC1.class, //
				M2O_S_NUMERIC2.class, //
				M2O_S_NUMERIC3.class, //
				M2O_S_NUMERIC4.class, //
				M2O_S_NUMERIC5.class, //
				M2O_S_NUMERIC6.class, //
				M2O_S_NUMERIC7.class, //
				M2O_S_NUMERIC8.class, //
				M2O_S_NUMERIC9.class, //
				M2O_S_NUMERIC10.class, //
				M2O_S_NUMERIC11.class, //
				M2O_S_NUMERIC12.class, //
				M2O_S_TEXT.class, //
				M2O_S_VARCHAR.class);
		session.open();

		Set<M2O_C_CHAR1> sql_c1 = new HashSet<>();
		M2O_C_CHAR1 sql_c1_obj = null;

		Set<M2O_C_CHAR2> sql_c2 = new HashSet<>();
		M2O_C_CHAR2 sql_c2_obj = null;

		Set<M2O_S_BOOL1> sql_b1_c1 = new HashSet<>();
		Set<M2O_S_BOOL1> sql_b1_c2 = new HashSet<>();
		Set<M2O_S_BOOL2> sql_b2_c1 = new HashSet<>();
		Set<M2O_S_BOOL2> sql_b2_c2 = new HashSet<>();

		Set<M2O_S_INTEGER1> sql_i1_c1 = new HashSet<>();
		Set<M2O_S_INTEGER1> sql_i1_c2 = new HashSet<>();
		Set<M2O_S_INTEGER2> sql_i2_c1 = new HashSet<>();
		Set<M2O_S_INTEGER2> sql_i2_c2 = new HashSet<>();
		Set<M2O_S_INTEGER3> sql_i3_c1 = new HashSet<>();
		Set<M2O_S_INTEGER3> sql_i3_c2 = new HashSet<>();
		Set<M2O_S_INTEGER4> sql_i4_c1 = new HashSet<>();
		Set<M2O_S_INTEGER4> sql_i4_c2 = new HashSet<>();
		Set<M2O_S_INTEGER5> sql_i5_c1 = new HashSet<>();
		Set<M2O_S_INTEGER5> sql_i5_c2 = new HashSet<>();
		Set<M2O_S_INTEGER6> sql_i6_c1 = new HashSet<>();
		Set<M2O_S_INTEGER6> sql_i6_c2 = new HashSet<>();
		Set<M2O_S_INTEGER7> sql_i7_c1 = new HashSet<>();
		Set<M2O_S_INTEGER7> sql_i7_c2 = new HashSet<>();
		Set<M2O_S_INTEGER8> sql_i8_c1 = new HashSet<>();
		Set<M2O_S_INTEGER8> sql_i8_c2 = new HashSet<>();

		Set<M2O_S_DATE> sql_date_c1 = new HashSet<>();
		Set<M2O_S_DATE> sql_date_c2 = new HashSet<>();

		Set<M2O_S_TIMESTAMP> sql_time_c1 = new HashSet<>();
		Set<M2O_S_TIMESTAMP> sql_time_c2 = new HashSet<>();

		Set<M2O_S_CHAR1> sql_c1_c1 = new HashSet<>();
		Set<M2O_S_CHAR1> sql_c1_c2 = new HashSet<>();
		Set<M2O_S_CHAR2> sql_c2_c1 = new HashSet<>();
		Set<M2O_S_CHAR2> sql_c2_c2 = new HashSet<>();

		Set<M2O_S_DOUBLE1> sql_d1_c1 = new HashSet<>();
		Set<M2O_S_DOUBLE1> sql_d1_c2 = new HashSet<>();
		Set<M2O_S_DOUBLE2> sql_d2_c1 = new HashSet<>();
		Set<M2O_S_DOUBLE2> sql_d2_c2 = new HashSet<>();

		Set<M2O_S_FLOAT1> sql_f1_c1 = new HashSet<>();
		Set<M2O_S_FLOAT1> sql_f1_c2 = new HashSet<>();
		Set<M2O_S_FLOAT2> sql_f2_c1 = new HashSet<>();
		Set<M2O_S_FLOAT2> sql_f2_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC1> sql_n1_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC1> sql_n1_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC2> sql_n2_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC2> sql_n2_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC3> sql_n3_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC3> sql_n3_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC4> sql_n4_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC4> sql_n4_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC5> sql_n5_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC5> sql_n5_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC6> sql_n6_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC6> sql_n6_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC7> sql_n7_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC7> sql_n7_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC8> sql_n8_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC8> sql_n8_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC9> sql_n9_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC9> sql_n9_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC10> sql_n10_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC10> sql_n10_c2 = new HashSet<>();

		Set<M2O_S_NUMERIC11> sql_n11_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC11> sql_n11_c2 = new HashSet<>();
		Set<M2O_S_NUMERIC12> sql_n12_c1 = new HashSet<>();
		Set<M2O_S_NUMERIC12> sql_n12_c2 = new HashSet<>();

		Set<M2O_S_TEXT> sql_t_c1 = new HashSet<>();
		Set<M2O_S_TEXT> sql_t_c2 = new HashSet<>();

		Set<M2O_S_VARCHAR> sql_v_c1 = new HashSet<>();
		Set<M2O_S_VARCHAR> sql_v_c2 = new HashSet<>();

		M2O_S_BOOL1 o2m_s_b1 = null;
		sql_b1_c1.add(o2m_s_b1 = new M2O_S_BOOL1(true));
		sql_b1_c2.add(o2m_s_b1 = new M2O_S_BOOL1(false));

		M2O_S_BOOL2 o2m_s_b2 = null;
		sql_b2_c1.add(o2m_s_b2 = new M2O_S_BOOL2(true));
		sql_b2_c2.add(o2m_s_b2 = new M2O_S_BOOL2(false));

		M2O_S_INTEGER1 o2m_s_i1 = null;
		M2O_S_INTEGER2 o2m_s_i2 = null;
		M2O_S_INTEGER3 o2m_s_i3 = null;
		M2O_S_INTEGER4 o2m_s_i4 = null;
		M2O_S_INTEGER5 o2m_s_i5 = null;
		M2O_S_INTEGER6 o2m_s_i6 = null;
		M2O_S_INTEGER7 o2m_s_i7 = null;
		M2O_S_INTEGER8 o2m_s_i8 = null;

		M2O_S_DATE o2m_s_date = null;

		M2O_S_TIMESTAMP o2m_s_time = null;

		M2O_S_CHAR1 o2m_s_c1 = null;
		M2O_S_CHAR2 o2m_s_c2 = null;

		M2O_S_DOUBLE1 o2m_s_d1 = null;
		M2O_S_DOUBLE2 o2m_s_d2 = null;

		M2O_S_FLOAT1 o2m_s_f1 = null;
		M2O_S_FLOAT2 o2m_s_f2 = null;

		M2O_S_NUMERIC1 o2m_s_n1 = null;
		M2O_S_NUMERIC2 o2m_s_n2 = null;
		M2O_S_NUMERIC3 o2m_s_n3 = null;
		M2O_S_NUMERIC4 o2m_s_n4 = null;
		M2O_S_NUMERIC5 o2m_s_n5 = null;

		M2O_S_NUMERIC6 o2m_s_n6 = null;

		M2O_S_NUMERIC7 o2m_s_n7 = null;

		M2O_S_NUMERIC8 o2m_s_n8 = null;

		M2O_S_NUMERIC9 o2m_s_n9 = null;
		M2O_S_NUMERIC10 o2m_s_n10 = null;

		M2O_S_NUMERIC11 o2m_s_n11 = null;
		M2O_S_NUMERIC12 o2m_s_n12 = null;

		M2O_S_TEXT o2m_s_t = null;

		M2O_S_VARCHAR o2m_s_v = null;

		for (int i = 0; i < ROWS; i++) {

			sql_i1_c1.add(o2m_s_i1 = new M2O_S_INTEGER1());
			sql_i1_c2.add(o2m_s_i1 = new M2O_S_INTEGER1());
			sql_i2_c1.add(o2m_s_i2 = new M2O_S_INTEGER2());
			sql_i2_c2.add(o2m_s_i2 = new M2O_S_INTEGER2());
			sql_i3_c1.add(o2m_s_i3 = new M2O_S_INTEGER3());
			sql_i3_c2.add(o2m_s_i3 = new M2O_S_INTEGER3());
			sql_i4_c1.add(o2m_s_i4 = new M2O_S_INTEGER4());
			sql_i4_c2.add(o2m_s_i4 = new M2O_S_INTEGER4());
			sql_i5_c1.add(o2m_s_i5 = new M2O_S_INTEGER5(i + 1));
			sql_i5_c2.add(o2m_s_i5 = new M2O_S_INTEGER5(ROWS + 1 + i));
			sql_i6_c1.add(o2m_s_i6 = new M2O_S_INTEGER6(i + 1));
			sql_i6_c2.add(o2m_s_i6 = new M2O_S_INTEGER6(ROWS + i + 1));
			sql_i7_c1.add(o2m_s_i7 = new M2O_S_INTEGER7((short) (i + 1)));
			sql_i7_c2.add(o2m_s_i7 = new M2O_S_INTEGER7((short) (ROWS + i + 1)));
			sql_i8_c1.add(o2m_s_i8 = new M2O_S_INTEGER8((byte) (i + 1)));
			sql_i8_c2.add(o2m_s_i8 = new M2O_S_INTEGER8((byte) (ROWS + i + 1)));

			cal.add(Calendar.DAY_OF_YEAR, 1);
			sql_date_c1.add(o2m_s_date = new M2O_S_DATE(cal.getTime()));
			sql_time_c1.add(o2m_s_time = new M2O_S_TIMESTAMP(cal.getTime()));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			sql_date_c2.add(o2m_s_date = new M2O_S_DATE(cal.getTime()));
			sql_time_c2.add(o2m_s_time = new M2O_S_TIMESTAMP(cal.getTime()));

			sql_c1_c1.add(o2m_s_c1 = new M2O_S_CHAR1((char) (65 + i)));
			sql_c1_c2.add(o2m_s_c1 = new M2O_S_CHAR1((char) (97 + i)));
			sql_c2_c1.add(o2m_s_c2 = new M2O_S_CHAR2((char) (65 + i)));
			sql_c2_c2.add(o2m_s_c2 = new M2O_S_CHAR2((char) (97 + i)));

			sql_d1_c1.add(o2m_s_d1 = new M2O_S_DOUBLE1(0.1 * (i + 1)));
			sql_d1_c2.add(o2m_s_d1 = new M2O_S_DOUBLE1(0.1 * (ROWS + i + 1)));
			sql_d2_c1.add(o2m_s_d2 = new M2O_S_DOUBLE2(0.1 * (i + 1)));
			sql_d2_c2.add(o2m_s_d2 = new M2O_S_DOUBLE2(0.1 * (ROWS + i + 1)));

			sql_f1_c1.add(o2m_s_f1 = new M2O_S_FLOAT1((float) (0.1 * (i + 1))));
			sql_f1_c2.add(o2m_s_f1 = new M2O_S_FLOAT1((float) (0.1 * (ROWS + i + 1))));
			sql_f2_c1.add(o2m_s_f2 = new M2O_S_FLOAT2((float) (0.1 * (i + 1))));
			sql_f2_c2.add(o2m_s_f2 = new M2O_S_FLOAT2((float) (0.1 * (ROWS + i + 1))));

			sql_n1_c1.add(o2m_s_n1 = new M2O_S_NUMERIC1(i + 1));
			sql_n1_c2.add(o2m_s_n1 = new M2O_S_NUMERIC1(ROWS + 1 + i));
			sql_n2_c1.add(o2m_s_n2 = new M2O_S_NUMERIC2(i + 1));
			sql_n2_c2.add(o2m_s_n2 = new M2O_S_NUMERIC2(ROWS + 1 + i));
			sql_n3_c1.add(o2m_s_n3 = new M2O_S_NUMERIC3((short) (i + 1)));
			sql_n3_c2.add(o2m_s_n3 = new M2O_S_NUMERIC3((short) (ROWS + 1 + i)));
			sql_n4_c1.add(o2m_s_n4 = new M2O_S_NUMERIC4((byte) (i + 1)));
			sql_n4_c2.add(o2m_s_n4 = new M2O_S_NUMERIC4((byte) (ROWS + 1 + i)));
			sql_n5_c1.add(o2m_s_n5 = new M2O_S_NUMERIC5(i + 1));
			sql_n5_c2.add(o2m_s_n5 = new M2O_S_NUMERIC5(ROWS + 1 + i));
			sql_n6_c1.add(o2m_s_n6 = new M2O_S_NUMERIC6(i + 1));
			sql_n6_c2.add(o2m_s_n6 = new M2O_S_NUMERIC6(ROWS + i + 1));

			sql_n7_c1.add(o2m_s_n7 = new M2O_S_NUMERIC7((short) (i + 1)));
			sql_n7_c2.add(o2m_s_n7 = new M2O_S_NUMERIC7((short) (ROWS + i + 1)));

			sql_n8_c1.add(o2m_s_n8 = new M2O_S_NUMERIC8((byte) (i + 1)));
			sql_n8_c2.add(o2m_s_n8 = new M2O_S_NUMERIC8((byte) (ROWS + i + 1)));

			sql_n9_c1.add(o2m_s_n9 = new M2O_S_NUMERIC9(0.1 * (i + 1)));
			sql_n9_c2.add(o2m_s_n9 = new M2O_S_NUMERIC9(0.1 * (ROWS + 1 + i)));
			sql_n10_c1.add(o2m_s_n10 = new M2O_S_NUMERIC10(0.1 * (i + 1)));
			sql_n10_c2.add(o2m_s_n10 = new M2O_S_NUMERIC10(0.1 * (ROWS + i + 1)));

			sql_n11_c1.add(o2m_s_n11 = new M2O_S_NUMERIC11((float) (0.1 * (i + 1))));
			sql_n11_c2.add(o2m_s_n11 = new M2O_S_NUMERIC11((float) (0.1 * (ROWS + i + 1))));
			sql_n12_c1.add(o2m_s_n12 = new M2O_S_NUMERIC12((float) (0.1 * (i + 1))));
			sql_n12_c2.add(o2m_s_n12 = new M2O_S_NUMERIC12((float) (0.1 * (ROWS + i + 1))));

			sql_t_c1.add(o2m_s_t = new M2O_S_TEXT("" + (i + 1)));
			sql_t_c2.add(o2m_s_t = new M2O_S_TEXT("" + (ROWS + i + 1)));

			sql_v_c1.add(o2m_s_v = new M2O_S_VARCHAR("" + (i + 1)));
			sql_v_c2.add(o2m_s_v = new M2O_S_VARCHAR("" + (ROWS + i + 1)));

		}

		sql_c1_obj = new M2O_C_CHAR1(true);
		sql_c2_obj = new M2O_C_CHAR2(false);

		sql_c1_obj.add(sql_b1_c1, o2m_s_b1);
		sql_c1_obj.add(sql_b2_c1, o2m_s_b2);
		sql_c2_obj.add(sql_b1_c2, o2m_s_b1);
		sql_c2_obj.add(sql_b2_c2, o2m_s_b2);

		sql_c1_obj.add(sql_i1_c1, o2m_s_i1);
		sql_c1_obj.add(sql_i2_c1, o2m_s_i2);
		sql_c1_obj.add(sql_i3_c1, o2m_s_i3);
		sql_c1_obj.add(sql_i4_c1, o2m_s_i4);
		sql_c1_obj.add(sql_i5_c1, o2m_s_i5);
		sql_c1_obj.add(sql_i6_c1, o2m_s_i6);
		sql_c1_obj.add(sql_i7_c1, o2m_s_i7);
		sql_c1_obj.add(sql_i8_c1, o2m_s_i8);
		sql_c2_obj.add(sql_i1_c2, o2m_s_i1);
		sql_c2_obj.add(sql_i2_c2, o2m_s_i2);
		sql_c2_obj.add(sql_i3_c2, o2m_s_i3);
		sql_c2_obj.add(sql_i4_c2, o2m_s_i4);
		sql_c2_obj.add(sql_i5_c2, o2m_s_i5);
		sql_c2_obj.add(sql_i6_c2, o2m_s_i6);
		sql_c2_obj.add(sql_i7_c2, o2m_s_i7);
		sql_c2_obj.add(sql_i8_c2, o2m_s_i8);

		sql_c1_obj.add(sql_date_c1, o2m_s_date);
		sql_c1_obj.add(sql_time_c1, o2m_s_time);
		sql_c2_obj.add(sql_date_c2, o2m_s_date);
		sql_c2_obj.add(sql_time_c2, o2m_s_time);

		sql_c1_obj.add(sql_c1_c1, o2m_s_c1);
		sql_c1_obj.add(sql_c2_c1, o2m_s_c2);
		sql_c2_obj.add(sql_c1_c2, o2m_s_c1);
		sql_c2_obj.add(sql_c2_c2, o2m_s_c2);

		sql_c1_obj.add(sql_d1_c1, o2m_s_d1);
		sql_c1_obj.add(sql_d2_c1, o2m_s_d2);
		sql_c2_obj.add(sql_d1_c2, o2m_s_d1);
		sql_c2_obj.add(sql_d2_c2, o2m_s_d2);

		sql_c1_obj.add(sql_f1_c1, o2m_s_f1);
		sql_c1_obj.add(sql_f2_c1, o2m_s_f2);
		sql_c2_obj.add(sql_f1_c2, o2m_s_f1);
		sql_c2_obj.add(sql_f2_c2, o2m_s_f2);

		sql_c1_obj.add(sql_n1_c1, o2m_s_n1);
		sql_c1_obj.add(sql_n2_c1, o2m_s_n2);
		sql_c1_obj.add(sql_n3_c1, o2m_s_n3);
		sql_c1_obj.add(sql_n4_c1, o2m_s_n4);
		sql_c1_obj.add(sql_n5_c1, o2m_s_n5);
		sql_c1_obj.add(sql_n6_c1, o2m_s_n6);
		sql_c1_obj.add(sql_n7_c1, o2m_s_n7);
		sql_c1_obj.add(sql_n8_c1, o2m_s_n8);
		sql_c1_obj.add(sql_n9_c1, o2m_s_n9);
		sql_c1_obj.add(sql_n10_c1, o2m_s_n10);
		sql_c1_obj.add(sql_n11_c1, o2m_s_n11);
		sql_c1_obj.add(sql_n12_c1, o2m_s_n12);
		sql_c2_obj.add(sql_n1_c2, o2m_s_n1);
		sql_c2_obj.add(sql_n2_c2, o2m_s_n2);
		sql_c2_obj.add(sql_n3_c2, o2m_s_n3);
		sql_c2_obj.add(sql_n4_c2, o2m_s_n4);
		sql_c2_obj.add(sql_n5_c2, o2m_s_n5);
		sql_c2_obj.add(sql_n6_c2, o2m_s_n6);

		sql_c2_obj.add(sql_n7_c2, o2m_s_n7);

		sql_c2_obj.add(sql_n8_c2, o2m_s_n8);

		sql_c2_obj.add(sql_n9_c2, o2m_s_n9);
		sql_c2_obj.add(sql_n10_c2, o2m_s_n10);

		sql_c2_obj.add(sql_n11_c2, o2m_s_n11);
		sql_c2_obj.add(sql_n12_c2, o2m_s_n12);

		sql_c1_obj.add(sql_t_c1, o2m_s_t);
		sql_c2_obj.add(sql_t_c2, o2m_s_t);

		sql_c1_obj.add(sql_v_c1, o2m_s_v);
		sql_c2_obj.add(sql_v_c2, o2m_s_v);

		session.persist(sql_c1_obj);
		sql_c1.add(sql_c1_obj);

		session.persist(sql_c2_obj);
		sql_c2.add(sql_c2_obj);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				M2O_C_BOOL1.class, //
				M2O_C_BOOL2.class, //
				M2O_C_CHAR1.class, //
				M2O_C_CHAR2.class, //
				M2O_S_BOOL1.class, //
				M2O_S_BOOL2.class, //
				M2O_S_INTEGER1.class, //
				M2O_S_INTEGER2.class, //
				M2O_S_INTEGER3.class, //
				M2O_S_INTEGER4.class, //
				M2O_S_INTEGER5.class, //
				M2O_S_INTEGER6.class, //
				M2O_S_INTEGER7.class, //
				M2O_S_INTEGER8.class, //
				M2O_S_DATE.class, //
				M2O_S_TIMESTAMP.class, //
				M2O_S_CHAR1.class, //
				M2O_S_CHAR2.class, //
				M2O_S_DOUBLE1.class, //
				M2O_S_DOUBLE2.class, //
				M2O_S_FLOAT1.class, //
				M2O_S_FLOAT2.class, //
				M2O_S_NUMERIC1.class, //
				M2O_S_NUMERIC2.class, //
				M2O_S_NUMERIC3.class, //
				M2O_S_NUMERIC4.class, //
				M2O_S_NUMERIC5.class, //
				M2O_S_NUMERIC6.class, //
				M2O_S_NUMERIC7.class, //
				M2O_S_NUMERIC8.class, //
				M2O_S_NUMERIC9.class, //
				M2O_S_NUMERIC10.class, //
				M2O_S_NUMERIC11.class, //
				M2O_S_NUMERIC12.class, //
				M2O_S_TEXT.class, //
				M2O_S_VARCHAR.class);
		session.open();

		Set<M2O_C_CHAR1> sql_1 = session.getAllObjects(M2O_C_CHAR1.class);
		Set<M2O_C_CHAR2> sql_2 = session.getAllObjects(M2O_C_CHAR2.class);

		Assert.assertTrue(sql_c1_obj.check(sql_c1, sql_1));
		Assert.assertTrue(sql_c2_obj.check(sql_c2, sql_2));

		this.storePort.sessionManager().terminate(session);

	}

}