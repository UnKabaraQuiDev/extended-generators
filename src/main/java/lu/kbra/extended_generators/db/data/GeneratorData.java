package lu.kbra.extended_generators.db.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Material;

import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.items.GeneratorType;
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
public class GeneratorData implements SafeSQLEntry {

	/** inclusive */
	public static final int MAX_TIER = 5;

	private int id, playerId, chunkId, posX, posY, posZ, tier;
	private GeneratorType type;
	private Material affinity;

	private ChunkData chunkData;
	private PlayerData playerData;
	private Location location;

	public GeneratorData() {
	}

	public GeneratorData(PlayerData playerData, Location location, GeneratorType type, Material affinity, int tier) {
		this.playerId = playerData.getId();
		this.posX = location.getBlockX();
		this.posY = location.getBlockY();
		this.posZ = location.getBlockZ();
		this.type = type;
		this.affinity = affinity;
		this.tier = tier;

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
	public void generatedKeyUpdate(Integer bigInt) {
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
		this.type = GeneratorType.valueOf(rs.getString("type"));
		this.affinity = rs.getString("affinity") == null ? null : Material.valueOf(rs.getString("affinity"));
	}

	/** items/min */
	public int calculateSpeed() {
		return (int) (Math.pow(tier, 2) * 6);
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
		return SQLBuilder.safeInsert(table, new String[] { "player_id", "chunk_id", "pos_x", "pos_y", "pos_z", "type", "affinity", "tier" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedUpdateSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeUpdate(table, new String[] { "player_id", "chunk_id", "pos_x", "pos_y", "pos_z", "type", "affinity", "tier" }, new String[] { "id" });
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
		stmt.setString(6, type.name());
		stmt.setString(7, affinity == null ? null : affinity.name());
		stmt.setInt(8, tier);
	}

	@Override
	public void prepareUpdateSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, playerId);
		stmt.setInt(2, chunkId);
		stmt.setInt(3, posX);
		stmt.setInt(4, posY);
		stmt.setInt(5, posZ);
		stmt.setString(6, type.name());
		stmt.setString(7, affinity == null ? null : affinity.name());
		stmt.setInt(8, tier);

		stmt.setInt(9, id);
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

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public GeneratorType getType() {
		return type;
	}

	public void setType(GeneratorType type) {
		this.type = type;
	}

	public Material getAffinity() {
		return affinity;
	}

	public void setAffinity(Material affinity) {
		this.affinity = affinity;
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

	@Override
	public String toString() {
		return "GeneratorData [id=" + id + ", playerId=" + playerId + ", chunkId=" + chunkId + ", posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + ", tier=" + tier + ", type=" + type + ", affinity=" + affinity + ", chunkData="
				+ chunkData + ", playerData=" + playerData + ", location=" + location + "]";
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
