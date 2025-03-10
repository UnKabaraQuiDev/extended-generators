package lu.kbra.extended_generators.db.table;

import lu.kbra.extended_generators.db.data.HomeData;
import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.Column;
import lu.pcy113.pclib.db.annotations.Constraint;
import lu.pcy113.pclib.db.annotations.Constraint.Type;
import lu.pcy113.pclib.db.annotations.DB_Table;

//@formatter:off
@DB_Table(name = "homes", columns = {
		@Column(name = "id", type = "integer primary key autoincrement"),
		@Column(name = "player_id", type = "int"),
		@Column(name = "name", type = "varchar(64)"),
		@Column(name = "dimension", type = "varchar(64)"),
		@Column(name = "pos_x", type = "int"),
		@Column(name = "pos_y", type = "int"),
		@Column(name = "pos_z", type = "int")
}, constraints = {
		// @Constraint(name = "pk_id", type = Type.PRIMARY_KEY, columns = "id"),
		@Constraint(name = "fk_player_id", type = Type.FOREIGN_KEY, foreignKey = "player_id", referenceTable = "players", referenceColumn = "id"),
})
//@formatter:on
public class HomeTable extends DataBaseTable<HomeData> {

	public static HomeTable INSTANCE;

	public HomeTable(DataBase dbTest) {
		super(dbTest);

		INSTANCE = this;
	}

}
