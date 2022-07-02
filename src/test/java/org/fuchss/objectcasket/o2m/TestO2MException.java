package org.fuchss.objectcasket.o2m;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.o2m.objects.o2m.O2M_C_BOOL1;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestO2MException extends TestBase {

	Session session = null;

	@Test
	public void test() throws Exception {

		Exception exception = Assertions.assertThrows(ObjectCasketException.class, () -> {
			this.session = this.storePort.sessionManager().session(this.config());
			this.session.declareClass( //
					O2M_C_BOOL1.class //
			);
			this.session.open();
		});

		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains("O2M_C_BOOL1"));
	}

}
