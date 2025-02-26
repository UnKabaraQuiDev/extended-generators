package lu.kbra.extended_generators.db.table;

import lu.pcy113.pclib.db.DataBase;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.annotations.Column;
import lu.pcy113.pclib.db.annotations.Constraint;
import lu.pcy113.pclib.db.annotations.Constraint.Type;
import lu.pcy113.pclib.db.annotations.DB_Table;

import lu.kbra.extended_generators.db.data.GeneratorData;

//@formatter:off
@DB_Table(name = "generators", columns = {
		@Column(name = "id", type = "int", autoIncrement = true),
		@Column(name = "chunk_id", type = "int"),
		@Column(name = "pos_x", type = "int"),
		@Column(name = "pos_y", type = "int"),
		@Column(name = "pos_z", type = "int")
}, constraints = {
		@Constraint(name = "pk_id", type = Type.PRIMARY_KEY, columns = "id"),
		@Constraint(name = "fk_chunk_id", type = Type.FOREIGN_KEY, columns = "chunk_id", referenceTable = "chunks", referenceColumn = "id"),
		@Constraint(name = "uq_pos", type = Type.UNIQUE, columns = { "pos_x", "pos_y", "pos_z" })
})
//@formatter:on
public class GeneratorTable extends DataBaseTable<GeneratorData> {

	public static final GeneratorTable INSTANCE = null;

	public GeneratorTable(DataBase dbTest) {
		super(dbTest);
	}

}
