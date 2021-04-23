package org.fuchss.objectcasket.o2m.variants;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

//
//------ 1              n -----
//| C   | x------------> |  S  |
//------                  -----
//
// Queue<E>, Deque<E>, TransferQueue<E>
// BlockingQueue<E>, BlockingDeque<E>
// List<E>
// Set<E>, SortedSet<E>, NavigableSet<E>

public class TestO2M_Variants extends TestBase {

	static final int ROWS = 10;

	@SuppressWarnings("unused")
	@Test
	public void test() {
		if (ROWS < 100)
			return;
		try {

			Session session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					O2M_SET_C.class, //
					O2M_SET_S.class, //
					O2M_LIST_C.class, //
					O2M_LIST_S.class, //
					O2M_DEQUE_C.class, //
					O2M_DEQUE_S.class);
			session.open();

			Set<O2M_SET_C> sql_c1 = new HashSet<>();
			O2M_SET_C sql_c1_obj = null;
			Set<O2M_LIST_C> sql_c2 = new HashSet<>();
			O2M_LIST_C sql_c2_obj = null;
			Set<O2M_DEQUE_C> sql_c3 = new HashSet<>();
			O2M_DEQUE_C sql_c3_obj = null;

			Set<O2M_SET_S> sql_s1 = new HashSet<>();
			O2M_SET_S sql_s1_obj = null;
			Set<O2M_LIST_S> sql_s2 = new HashSet<>();
			O2M_LIST_S sql_s2_obj = null;
			Set<O2M_DEQUE_S> sql_s3 = new HashSet<>();
			O2M_DEQUE_S sql_s3_obj = null;

			for (int i = 0; i < ROWS; i++) {
				sql_s1.add(sql_s1_obj = new O2M_SET_S());
				sql_s2.add(sql_s2_obj = new O2M_LIST_S());
				sql_s3.add(sql_s3_obj = new O2M_DEQUE_S());
			}

			sql_c1_obj = new O2M_SET_C();
			sql_c1_obj.add(sql_s1, sql_s1_obj);

			sql_c2_obj = new O2M_LIST_C();
			sql_c2_obj.add(sql_s2, sql_s2_obj);

			sql_c3_obj = new O2M_DEQUE_C();
			sql_c3_obj.add(sql_s3, sql_s3_obj);

			sql_c1.add(sql_c1_obj);
			sql_c2.add(sql_c2_obj);
			sql_c3.add(sql_c3_obj);

			session.persist(sql_c1_obj);
			session.persist(sql_c2_obj);
			session.persist(sql_c3_obj);
			this.storePort.sessionManager().terminate(session);

			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(//
					O2M_SET_C.class, //
					O2M_SET_S.class, //
					O2M_LIST_C.class, //
					O2M_LIST_S.class, //
					O2M_DEQUE_C.class, //
					O2M_DEQUE_S.class);
			session.open();

			Set<O2M_SET_C> sql_1 = session.getAllObjects(O2M_SET_C.class);
			Set<O2M_LIST_C> sql_2 = session.getAllObjects(O2M_LIST_C.class);
			Set<O2M_DEQUE_C> sql_3 = session.getAllObjects(O2M_DEQUE_C.class);

			Assert.assertTrue(sql_c1_obj.check(sql_c1, sql_1));
			Assert.assertTrue(sql_c2_obj.check(sql_c2, sql_2));
			Assert.assertTrue(sql_c3_obj.check(sql_c3, sql_3));

			for (O2M_DEQUE_C client : sql_c3) {
				System.out.print(client);
				for (O2M_DEQUE_S supplier : client.supplier) {
					System.out.print(supplier);
				}
				System.out.println();
			}

			this.storePort.sessionManager().terminate(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
