package lu.kbra.extended_generators.db.data;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;

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

import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.PlayerManager;

@GeneratedKey("id")
public class GeneratorData implements SafeSQLEntry {

	private int id, playerId, chunkId, posX, posY, posZ;

	private ChunkData chunkData;
	private PlayerData playerData;
	private Location location;

	public GeneratorData() {
	}

	public GeneratorData(PlayerData playerData, Location location) {
		this.playerId = playerData.getId();
		this.posX = location.getBlockX();
		this.posY = location.getBlockY();
		this.posZ = location.getBlockZ();

		this.playerData = playerData;
		this.location = location;
	}

	public GeneratorData(int playerId, int chunkId, int posX, int posY, int posZ) {
		this.playerId = playerId;
		this.chunkId = chunkId;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	@GeneratedKeyUpdate(type = Type.INDEX)
	public void generatedKeyUpdate(BigInteger bigInt) {
		this.id = bigInt.intValue();
	}

	@Reload
	public void reload(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.playerId = rs.getInt("player_id");
		this.chunkId = rs.getInt("chunk_id");
		this.posX = rs.getInt("pos_x");
		this.posY = rs.getInt("pos_y");
		this.posZ = rs.getInt("pos_z");
	}

	public GeneratorData loadAll() {
		loadChunk();
		loadBukkit();
		loadPlayer();
		return this;
	}

	public GeneratorData loadChunk() {
		this.chunkData = ChunkManager.getChunk(chunkId).run();
		return this;
	}

	public GeneratorData loadBukkit() {
		this.location = new Location(chunkData.getChunk().getWorld(), posX, posY, posZ);
		return this;
	}

	public GeneratorData loadPlayer() {
		this.playerData = PlayerManager.getPlayer(playerId).run();
		return this;
	}

	@Override
	public <T extends SQLEntry> String getPreparedInsertSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeInsert(table, new String[] { "player_id", "chunk_id", "pos_x", "pos_y", "pos_z" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedUpdateSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeUpdate(table, new String[] { "player_id", "chunk_id", "pos_x", "pos_y", "pos_z" }, new String[] { "id" });
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
		stmt.setInt(1, playerId);
		stmt.setInt(2, chunkId);
		stmt.setInt(3, posX);
		stmt.setInt(4, posY);
		stmt.setInt(5, posZ);
	}

	@Override
	public void prepareUpdateSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, playerId);
		stmt.setInt(2, chunkId);
		stmt.setInt(3, posX);
		stmt.setInt(4, posY);
		stmt.setInt(5, posZ);

		stmt.setInt(6, id);
	}

	@Override
	public void prepareDeleteSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, id);
	}

	@Override
	public void prepareSelectSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, id);
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getChunkId() {
		return chunkId;
	}

	public void setChunkId(int chunkId) {
		this.chunkId = chunkId;
	}

	@UniqueKey("pos_x")
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	@UniqueKey("pos_y")
	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	@UniqueKey("pos_z")
	public int getPosZ() {
		return posZ;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public ChunkData getChunkData() {
		return chunkData;
	}

	public void setChunkData(ChunkData chunkData) {
		this.chunkData = chunkData;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	@Override
	public GeneratorData clone() {
		return new GeneratorData();
	}

	public static SQLQuery<GeneratorData> byChunk(ChunkData data) {
		return byChunk(data.getId());
	}

	public static SQLQuery<GeneratorData> byChunk(int chunkId) {
		return new SafeSQLQuery<GeneratorData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<GeneratorData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "chunk_id" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, chunkId);
			}

			@Override
			public GeneratorData clone() {
				return new GeneratorData();
			}
		};
	}

	public static SQLQuery<GeneratorData> byLocation(final int chunkId, final Location loc) {
		return new SafeSQLQuery<GeneratorData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<GeneratorData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "chunk_id", "pos_x", "pos_y", "pos_z" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, chunkId);
				stmt.setInt(2, loc.getBlockX());
				stmt.setInt(3, loc.getBlockY());
				stmt.setInt(4, loc.getBlockZ());
			}

			@Override
			public GeneratorData clone() {
				return new GeneratorData();
			}
		};
	}

}
