package lu.kbra.extended_generators.db.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
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

@GeneratedKey("id")
public class HomeData implements SafeSQLEntry {

	/** inclusive */
	public static final int MAX_TIER = 5;

	private int id, playerId, posX, posY, posZ;
	private String name, dimension;

	private Location location;

	public HomeData() {
	}

	public HomeData(int pdId, String name, Location loc) {
		this.playerId = pdId;
		this.name = name;
		this.location = loc;
		this.dimension = loc.getWorld().getName();
	}

	@GeneratedKeyUpdate(type = Type.INDEX)
	public void generatedKeyUpdate(Integer bigInt) {
		this.id = bigInt.intValue();
	}

	@Reload
	public void reload(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.playerId = rs.getInt("player_id");
		this.posX = rs.getInt("pos_x");
		this.posY = rs.getInt("pos_y");
		this.posZ = rs.getInt("pos_z");
		this.dimension = rs.getString("dimension");
		this.name = rs.getString("name");
	}

	public HomeData loadAll() {
		loadBukkit();
		return this;
	}

	public HomeData loadBukkit() {
		this.location = new Location(Bukkit.getWorld(dimension), posX, posY, posZ);
		return this;
	}

	@Override
	public <T extends SQLEntry> String getPreparedInsertSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeInsert(table, new String[] { "player_id", "dimension", "pos_x", "pos_y", "pos_z", "name" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedUpdateSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeUpdate(table, new String[] { "player_id", "dimension", "pos_x", "pos_y", "pos_z", "name" }, new String[] { "id" });
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
		stmt.setString(2, dimension);
		stmt.setInt(3, posX);
		stmt.setInt(4, posY);
		stmt.setInt(5, posZ);
		stmt.setString(6, name);
	}

	@Override
	public void prepareUpdateSQL(PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, playerId);
		stmt.setString(2, dimension);
		stmt.setInt(3, posX);
		stmt.setInt(4, posY);
		stmt.setInt(5, posZ);
		stmt.setString(6, name);

		stmt.setInt(7, id);
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

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "HomeData [id=" + id + ", playerId=" + playerId + ", posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + ", name=" + name + ", dimension=" + dimension + ", location=" + location + "]";
	}

	@Override
	public HomeData clone() {
		return new HomeData();
	}

	public static SQLQuery<HomeData> byPlayer(final PlayerData pd) {
		return byPlayer(pd.getId());
	}

	public static SQLQuery<HomeData> byPlayer(final int playerId) {
		return new SafeSQLQuery<HomeData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<HomeData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "player_id" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, playerId);
			}

			@Override
			public HomeData clone() {
				return new HomeData();
			}
		};
	}

	public static SQLQuery<HomeData> byLocation(final int chunkId, final Location loc) {
		return new SafeSQLQuery<HomeData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<HomeData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "pos_x", "pos_y", "pos_z" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, chunkId);
				stmt.setInt(2, loc.getBlockX());
				stmt.setInt(3, loc.getBlockY());
				stmt.setInt(4, loc.getBlockZ());
			}

			@Override
			public HomeData clone() {
				return new HomeData();
			}
		};
	}

}
