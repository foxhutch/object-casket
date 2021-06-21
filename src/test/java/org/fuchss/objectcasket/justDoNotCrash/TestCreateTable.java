package org.fuchss.objectcasket.justDoNotCrash;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import org.junit.Assert;
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
		Files.deleteIfExists(this.file.toPath());
		System.out.println("DB is " + this.file.getPath());
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
		System.out.println("DB " + this.file.getPath() + " deleted");
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

		int x1 = 11;
		short x2 = 12;
		long x3 = 13;
		int x4 = 14;
		boolean x5 = true;
		boolean x6 = false;
		double x7 = 11.2;
		double x8 = 12.3;
		float x9 = 13.4f;
		float x10 = 14.5f;
		byte x11 = 15;
		byte x12 = 16;
		char x13 = 'c';
		char x14 = 'd';
		String x15 = "def";
		Date x16 = today;
		double x17 = 112.2;
		String x18 = "TextText";
		double x19 = 1.23;
		Date x20 = today;

		row.write(tr, "id_tab", x1);
		row.write(tr, "id_tab1", x2);
		row.write(tr, "id_tab2", x3);
		row.write(tr, "id_tab3", x4);
		row.write(tr, "id_tab4", x5);
		row.write(tr, "id_tab5", x6);
		row.write(tr, "id_tab6", x7);
		row.write(tr, "id_tab7", x8);
		row.write(tr, "id_tab8", x9);
		row.write(tr, "id_tab9", x10);
		row.write(tr, "id_tab10", x11);
		row.write(tr, "id_tab11", x12);
		row.write(tr, "id_tab12", x13);
		row.write(tr, "id_tab13", x14);
		row.write(tr, "id_tab14", x15);
		row.write(tr, "id_tab15", x16);
		row.write(tr, "id_tab16", x17);
		row.write(tr, "id_tab17", x18);
		row.write(tr, "id_tab18", x19);
		row.write(tr, "id_tab19", x20);

		Assert.assertTrue(x1 == row.read(tr, "id_tab", Integer.class));
		Assert.assertTrue(x2 == row.read(tr, "id_tab1", Short.class));
		Assert.assertTrue(x3 == row.read(tr, "id_tab2", Long.class));
		Assert.assertTrue(x4 == row.read(tr, "id_tab3", Integer.TYPE));
		Assert.assertTrue(x5 == row.read(tr, "id_tab4", Boolean.class));
		Assert.assertTrue(x6 == row.read(tr, "id_tab5", Boolean.TYPE));
		Assert.assertTrue(x7 == row.read(tr, "id_tab6", Double.class));
		Assert.assertTrue(x8 == row.read(tr, "id_tab7", Double.TYPE));
		Assert.assertTrue(x9 == row.read(tr, "id_tab8", Float.class));
		Assert.assertTrue(x10 == row.read(tr, "id_tab9", Float.TYPE));
		Assert.assertTrue(x11 == row.read(tr, "id_tab10", Byte.class));
		Assert.assertTrue(x12 == row.read(tr, "id_tab11", Byte.TYPE));
		Assert.assertTrue(x13 == row.read(tr, "id_tab12", Character.class));
		Assert.assertTrue(x14 == row.read(tr, "id_tab13", Character.TYPE));
		Assert.assertTrue(x15 == row.read(tr, "id_tab14", String.class));
		Assert.assertTrue(x16.equals(row.read(tr, "id_tab15", Date.class)));
		Assert.assertTrue(x17 == row.read(tr, "id_tab16", Double.class));
		Assert.assertTrue(x18 == row.read(tr, "id_tab17", String.class));
		Assert.assertTrue(x19 == row.read(tr, "id_tab18", Double.class));
		Assert.assertTrue(x20.equals(row.read(tr, "id_tab19", Date.class)));

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
