package lu.kbra.extended_generators.db;

import java.sql.SQLException;
import java.util.logging.Logger;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseConnector;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.DB_Base;

@DB_Base(name = "extended_generators")
public class EGDataBase extends DataBase {

	public Logger logger = ExtendedGenerators.INSTANCE.getLogger();
	
	public EGDataBase(DataBaseConnector connector) {
		super(connector);
	}
	
	public void create(DataBaseTable<?> table) {
		final String name = "`" + table.getTableName() + "`";
		// System.out.println(table.getCreateSQL());
		table.create().thenConsume((e) -> {
			e.ifError((d) -> logger.severe("Error creating Table: " + name + "\n" + table.getCreateSQL()));
			e.ifOk((d) -> logger.info(d.created() ? "Table: " + name + " created" : "Table: " + name + " already exists"));

			e.ifError(d -> d.printStackTrace());
		}).run();
	}

	public void createDB() throws SQLException {
		final String name = "`" + super.getDataBaseName() + "`";

		super.create().thenConsume((e) -> {
			e.ifError((d) -> logger.severe("Error creating Base: " + name));
			e.ifOk((d) -> logger.info(d.created() ? "Base: " + name + " created" : "Base: " + name + " already exists"));

			e.ifError(d -> d.printStackTrace());
		}).run();

		super.updateDataBaseConnector();
		super.getConnector().reset();
	}

}
