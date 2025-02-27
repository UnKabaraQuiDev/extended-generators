package lu.kbra.extended_generators.db.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import lu.kbra.extended_generators.db.table.GeneratorTable;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.SQLBuilder;
import lu.pcy113.pclib.db.annotations.GeneratedKey;
import lu.pcy113.pclib.db.annotations.GeneratedKeyUpdate;
import lu.pcy113.pclib.db.annotations.GeneratedKeyUpdate.Type;
import lu.pcy113.pclib.db.annotations.Reload;
import lu.pcy113.pclib.db.annotations.UniqueKey;
import lu.pcy113.pclib.db.impl.SQLEntry;
import lu.pcy113.pclib.db.impl.SQLEntry.SafeSQLEntry;
import lu.pcy113.pclib.db.impl.SQLQuery;
import lu.pcy113.pclib.db.impl.SQLQuery.SafeSQLQuery;

@GeneratedKey("id")
public class ChunkData implements SafeSQLEntry {

	private int id;
	private String posDimension;
	private int posX, posZ;

	private Chunk chunk;
	private List<GeneratorData> generators;

	public ChunkData() {
	}

	public ChunkData(Chunk chunk) {
		this.posDimension = chunk.getWorld().getName();
		this.posX = chunk.getX();
		this.posZ = chunk.getZ();

		this.chunk = chunk;
	}

	public ChunkData(int id, String posDimension, int posX, int posZ, Chunk chunk, List<GeneratorData> generators) {
		this.id = id;
		this.posDimension = posDimension;
		this.posX = posX;
		this.posZ = posZ;
		this.chunk = chunk;
		this.generators = generators;
	}

	public ChunkData loadAll() {
		loadBukkit();
		loadGenerators();
		return this;
	}

	public ChunkData loadBukkit() {
		this.chunk = Bukkit.getWorld(posDimension).getChunkAt(posX, posZ);
		return this;
	}

	public ChunkData loadGenerators() {
		this.generators = GeneratorTable.INSTANCE.query(GeneratorData.byChunk(id)).thenApply(PCUtils.single2SingleMultiMap()).run();
		return this;
	}

	@GeneratedKeyUpdate(type = Type.INDEX)
	public void generatedKeyUpdate(Integer bigInt) {
		this.id = bigInt.intValue();
	}

	@Reload
	public void reload(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.posDimension = rs.getString("pos_dimension");
		this.posX = rs.getInt("pos_x");
		this.posZ = rs.getInt("pos_z");
	}

	@Override
	public <T extends SQLEntry> String getPreparedInsertSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeInsert(table, new String[] { "pos_dimension", "pos_x", "pos_z" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedUpdateSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeUpdate(table, new String[] { "pos_dimension", "pos_x", "pos_z" }, new String[] { "id" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedDeleteSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeDelete(table, new String[] { "id" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedSelectSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeSelect(table, new String[] { "id" });
	}

	@Override
	public void prepareInsertSQL(PreparedStatement stmt) throws SQLException {
		stmt.setString(1, posDimension);
		stmt.setInt(2, posX);
		stmt.setInt(3, posZ);
	}

	@Override
	public void prepareUpdateSQL(PreparedStatement stmt) throws SQLException {
		stmt.setString(1, posDimension);
		stmt.setInt(2, posX);
		stmt.setInt(3, posZ);

		stmt.setInt(4, id);
	}

	@Override
	public void prepareDeleteSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, id);
	}

	@Override
	public void prepareSelectSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, id);
	}

	public int getId() {
		return id;
	}

	@UniqueKey("pos_dimension")
	public String getPosDimension() {
		return posDimension;
	}

	public void setPosDimension(String posDimension) {
		this.posDimension = posDimension;
	}

	@UniqueKey("pos_x")
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	@UniqueKey("pos_z")
	public int getPosZ() {
		return posZ;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public List<GeneratorData> getGenerators() {
		return generators;
	}

	public void setGenerators(List<GeneratorData> generators) {
		this.generators = generators;
	}

	@Override
	public String toString() {
		return "ChunkData [id=" + id + ", posDimension=" + posDimension + ", posX=" + posX + ", posZ=" + posZ + ", chunk=" + chunk + ", generators=" + generators + "]";
	}

	@Override
	public ChunkData clone() {
		return new ChunkData();
	}

	public static SQLQuery<ChunkData> byId(int d) {
		return new SafeSQLQuery<ChunkData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<ChunkData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "id" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, d);
			}

			@Override
			public ChunkData clone() {
				return new ChunkData();
			}
		};
	}

	public static SQLQuery<ChunkData> byChunk(Chunk chunk) {
		return new SafeSQLQuery<ChunkData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<ChunkData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "pos_dimension", "pos_x", "pos_z" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, chunk.getWorld().getName());
				stmt.setInt(2, chunk.getX());
				stmt.setInt(3, chunk.getZ());
			}

			@Override
			public ChunkData clone() {
				return new ChunkData();
			}
		};
	}

}
