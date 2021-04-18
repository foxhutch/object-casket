package org.fuchss.objectcasket.justDoNotCrash;

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.fuchss.sqlconnector.SqlConnectionFactory;
import org.fuchss.sqlconnector.port.Configuration;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.sqlconnector.port.SqlPrototype;
import org.fuchss.tablemodule.TableModuleFactory;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.Table;
import org.fuchss.tablemodule.port.TableModule;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.TablePrototype;
import org.fuchss.tablemodule.port.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCreateTable {
	private static final Class<? extends Driver> DRIVER = org.sqlite.JDBC.class;
	private static final String DRIVERNAME = "jdbc:sqlite:";

	private File file;
	private SqlDatabase db;
	private SqlObjectFactory objectFactory;
	private TableModule module;

	@Before
	public void setUpConnection() throws Exception {
		this.file = File.createTempFile("TestCreateTable", "db");
		Configuration config = SqlConnectionFactory.FACTORY.sqlPort().sqlDatabaseFactory().createConfiguration();
		config.setDriver(TestCreateTable.DRIVER, TestCreateTable.DRIVERNAME);
		config.setUri(this.file.toURI().getPath());
		config.setUser("");
		config.setPasswd("");
		config.setFlag(Configuration.Flag.MODIFY, Configuration.Flag.CREATE);
		this.db = SqlConnectionFactory.FACTORY.sqlPort().sqlDatabaseFactory().openDatabase(config);
		this.objectFactory = SqlConnectionFactory.FACTORY.sqlPort().sqlObjectFactory();

		this.module = TableModuleFactory.FACTORY.modulePort().tableModuleBuilder().tableModule(this.db, this.objectFactory);

	}

	@After
	public void cleanup() throws Exception {
		SqlConnectionFactory.FACTORY.sqlPort().sqlDatabaseFactory().closeDatabase(this.db);
		this.file.delete();
	}

	@Test
	public void testCreate() throws ConnectorException, TableModuleException, IOException {

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		TablePrototype tab = this.module.mkTablePrototype("Table");
		Set<SqlPrototype.Flag> flags = new HashSet<>();
		flags.add(SqlPrototype.Flag.PRIMARY_KEY);
		flags.add(SqlPrototype.Flag.NOT_NULL);
		flags.add(SqlPrototype.Flag.AUTOINCREMENT);
		flags.add(SqlPrototype.Flag.UNIQUE);

		Set<SqlPrototype.Flag> flags1 = new HashSet<>();
		tab.addColumn("id_tab", Integer.class, null, flags);
		tab.addColumn("id_tab1", Short.class, null, flags1);
		tab.addColumn("id_tab2", Long.class, null, flags1);
		tab.addColumn("id_tab3", Integer.TYPE, null, flags1);
		tab.addColumn("id_tab4", Boolean.class, null, flags1);
		tab.addColumn("id_tab5", Boolean.TYPE, null, flags1);
		tab.addColumn("id_tab6", Double.class, null, flags1);
		tab.addColumn("id_tab7", Double.TYPE, null, flags1);
		tab.addColumn("id_tab8", Float.class, null, flags1);
		tab.addColumn("id_tab9", Float.TYPE, null, flags1);
		tab.addColumn("id_tab10", Byte.class, null, flags1);
		tab.addColumn("id_tab11", Byte.TYPE, null, flags1);
		tab.addColumn("id_tab12", Character.class, null, flags1);
		tab.addColumn("id_tab13", Character.TYPE, null, flags1);
		tab.addColumn("id_tab14", String.class, null, flags1);
		tab.addColumn("id_tab15", Date.class, null, flags1);
		tab.addColumn("id_tab16", Double.class, SqlObject.Type.REAL, flags1);
		tab.addColumn("id_tab17", String.class, SqlObject.Type.TEXT, flags1);
		tab.addColumn("id_tab18", Double.class, SqlObject.Type.NUMERIC, flags1);
		tab.addColumn("id_tab19", Date.class, SqlObject.Type.TIMESTAMP, flags1);

		Table table = this.module.assignOrCreateTable(tab, null);

		cal.setTime(today);

		Transaction tr = this.module.beginTransaction();

		Row row = table.mkRow(tr);

		// row.write(tr, "id_tab", 11);
		row.write(tr, "id_tab1", 12);
		row.write(tr, "id_tab2", 13);
		row.write(tr, "id_tab3", 14);
		row.write(tr, "id_tab4", true);
		row.write(tr, "id_tab5", false);
		row.write(tr, "id_tab6", 11.2);
		row.write(tr, "id_tab7", 12.3);
		row.write(tr, "id_tab8", 13.4f);
		row.write(tr, "id_tab9", 14.5f);
		row.write(tr, "id_tab10", 15);
		row.write(tr, "id_tab11", 16);
		row.write(tr, "id_tab12", 'c');
		row.write(tr, "id_tab13", 'd');
		row.write(tr, "id_tab14", "def");
		row.write(tr, "id_tab15", today);
		row.write(tr, "id_tab16", 112.2);
		row.write(tr, "id_tab17", "TextText");
		row.write(tr, "id_tab18", 1.23);
		row.write(tr, "id_tab19", today);

		int x1 = row.read(tr, "id_tab", Integer.class);
		System.out.println(x1);
		short x2 = row.read(tr, "id_tab1", Short.class);
		System.out.println(x2);
		long x3 = row.read(tr, "id_tab2", Long.class);
		System.out.println(x3);
		int x4 = row.read(tr, "id_tab3", Integer.TYPE);
		System.out.println(x4);
		boolean x5 = row.read(tr, "id_tab4", Boolean.class);
		System.out.println(x5);
		boolean x6 = row.read(tr, "id_tab5", Boolean.TYPE);
		System.out.println(x6);
		double x7 = row.read(tr, "id_tab6", Double.class);
		System.out.println(x7);
		double x8 = row.read(tr, "id_tab7", Double.TYPE);
		System.out.println(x8);
		float x9 = row.read(tr, "id_tab8", Float.class);
		System.out.println(x9);
		float x10 = row.read(tr, "id_tab9", Float.TYPE);
		System.out.println(x10);
		byte x11 = row.read(tr, "id_tab10", Byte.class);
		System.out.println(x11);
		byte x12 = row.read(tr, "id_tab11", Byte.TYPE);
		System.out.println(x12);
		char x13 = row.read(tr, "id_tab12", Character.class);
		System.out.println(x13);
		char x14 = row.read(tr, "id_tab13", Character.TYPE);
		System.out.println(x14);
		String x15 = row.read(tr, "id_tab14", String.class);
		System.out.println(x15);
		Date x16 = row.read(tr, "id_tab15", Date.class);
		System.out.println(x16);
		double x17 = row.read(tr, "id_tab16", Double.class);
		System.out.println(x17);
		String x18 = row.read(tr, "id_tab17", String.class);
		System.out.println(x18);
		double x19 = row.read(tr, "id_tab18", Double.class);
		System.out.println(x19);
		Date x20 = row.read(tr, "id_tab19", Date.class);
		System.out.println(x20);

		this.module.commit(tr);

		tr = this.module.beginTransaction();

		cal.setTime(today);
		row.write(tr, "id_tab1", 312);
		row.write(tr, "id_tab2", 313);
		row.write(tr, "id_tab3", 314);
		row.write(tr, "id_tab4", false);
		row.write(tr, "id_tab5", true);
		row.write(tr, "id_tab6", 311.2);
		row.write(tr, "id_tab7", 312.3);
		row.write(tr, "id_tab8", 313.4f);
		row.write(tr, "id_tab9", 314.5f);
		row.write(tr, "id_tab10", 215);
		row.write(tr, "id_tab11", 216);
		row.write(tr, "id_tab12", 'e');
		row.write(tr, "id_tab13", 'f');
		row.write(tr, "id_tab14", "xdef");
		row.write(tr, "id_tab15", today);
		row.write(tr, "id_tab16", 3112.2);
		row.write(tr, "id_tab17", "TextTextText");
		row.write(tr, "id_tab18", 21.23);
		row.write(tr, "id_tab19", today);

		this.module.commit(tr);

	}
}
