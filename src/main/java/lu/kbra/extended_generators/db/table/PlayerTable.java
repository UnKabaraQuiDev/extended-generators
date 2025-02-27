package lu.kbra.extended_generators.db.table;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.Column;
import lu.pcy113.pclib.db.annotations.Constraint;
import lu.pcy113.pclib.db.annotations.Constraint.Type;
import lu.pcy113.pclib.db.annotations.DB_Table;

//@formatter:off
@DB_Table(name = "players", columns = {
		@Column(name = "id", type = "integer primary key autoincrement"),
		@Column(name = "name", type = "varchar(16)"),
		@Column(name = "uuid", type = "char(36)")
}, constraints = {
		// @Constraint(name = "pk_id", type = Type.PRIMARY_KEY, columns = "id"),
		@Constraint(name = "uq_uuid", type = Type.UNIQUE, columns = "uuid"),
		@Constraint(name = "uq_name", type = Type.UNIQUE, columns = "name")
})
//@formatter:on
public class PlayerTable extends DataBaseTable<PlayerData> {

	public static PlayerTable INSTANCE;

	public PlayerTable(DataBase dbTest) {
		super(dbTest);

		INSTANCE = this;
	}

}
