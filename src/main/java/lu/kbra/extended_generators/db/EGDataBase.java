package lu.kbra.extended_generators.db;

import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseConnector;
import lu.pcy113.pclib.db.annotations.DB_Base;

@DB_Base(name = "extended_generators")
public class EGDataBase extends DataBase {

	public EGDataBase(DataBaseConnector connector) {
		super(connector);
	}

}
