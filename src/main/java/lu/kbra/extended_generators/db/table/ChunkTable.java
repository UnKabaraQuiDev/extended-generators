package lu.kbra.extended_generators.db.table;

import lu.kbra.extended_generators.db.data.ChunkData;
import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.Column;
import lu.pcy113.pclib.db.annotations.Constraint;
import lu.pcy113.pclib.db.annotations.Constraint.Type;
import lu.pcy113.pclib.db.annotations.DB_Table;

//@formatter:off
@DB_Table(name = "chunks", columns = {
		@Column(name = "id", type = "integer primary key autoincrement"),
		@Column(name = "pos_dimension", type = "varchar(36)"),
		@Column(name = "pos_x", type = "int"),
		@Column(name = "pos_z", type = "int")
}, constraints = {
		// @Constraint(name = "pk_id", type = Type.PRIMARY_KEY, columns = "id"),
		@Constraint(name = "uq_pos", type = Type.UNIQUE, columns = { "pos_dimension", "pos_x", "pos_z" })
})
//@formatter:on
public class ChunkTable extends DataBaseTable<ChunkData> {

	public static ChunkTable INSTANCE;

	public ChunkTable(DataBase dbTest) {
		super(dbTest);
		
		INSTANCE = this;
	}

}
