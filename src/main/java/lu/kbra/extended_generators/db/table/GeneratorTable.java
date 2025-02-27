package lu.kbra.extended_generators.db.table;

import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.Column;
import lu.pcy113.pclib.db.annotations.Constraint;
import lu.pcy113.pclib.db.annotations.Constraint.Type;
import lu.pcy113.pclib.db.annotations.DB_Table;

//@formatter:off
@DB_Table(name = "generators", columns = {
		@Column(name = "id", type = "integer primary key autoincrement"),
		@Column(name = "chunk_id", type = "int"),
		@Column(name = "player_id", type = "int"),
		@Column(name = "pos_x", type = "int"),
		@Column(name = "pos_y", type = "int"),
		@Column(name = "pos_z", type = "int"),
		@Column(name = "type", type = "text"),
		@Column(name = "affinity", type = "text", notNull = false),
		@Column(name = "tier", type = "int", default_ = "1")
}, constraints = {
		// @Constraint(name = "pk_id", type = Type.PRIMARY_KEY, columns = "id"),
		@Constraint(name = "fk_chunk_id", type = Type.FOREIGN_KEY, foreignKey = "chunk_id", referenceTable = "chunks", referenceColumn = "id"),
		@Constraint(name = "fk_player_id", type = Type.FOREIGN_KEY, foreignKey = "player_id", referenceTable = "players", referenceColumn = "id"),
		@Constraint(name = "uq_pos", type = Type.UNIQUE, columns = { "pos_x", "pos_y", "pos_z" })
})
//@formatter:on
public class GeneratorTable extends DataBaseTable<GeneratorData> {

	public static GeneratorTable INSTANCE;

	public GeneratorTable(DataBase dbTest) {
		super(dbTest);
		
		INSTANCE = this;
	}

}
