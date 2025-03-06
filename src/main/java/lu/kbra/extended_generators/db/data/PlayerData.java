package lu.kbra.extended_generators.db.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.table.HomeTable;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.async.NextTask;
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
public class PlayerData implements SafeSQLEntry {

	private int id;
	private UUID uuid;
	private String name;

	private List<HomeData> homes;

	public PlayerData() {
	}

	public PlayerData(Player player) {
		this(player.getUniqueId(), player.getName());
	}

	public PlayerData(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public NextTask<Void, HomeData> addHome(final String name, final Location loc) {
		if (hasHome(name)) {
			HomeData hd = getHome(name);
			hd.setLocation(loc);
			hd.setDimension(loc.getWorld().getName());
			return HomeTable.INSTANCE.update(hd).thenApply(PCUtils.single2SingleMultiMap());
		} else {
			HomeData hd = new HomeData(id, name, loc);
			return HomeTable.INSTANCE.insertAndReload(hd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(homes::add);
		}
	}

	public NextTask<Void, HomeData> removeHome(final String name, final Location loc) {
		HomeData hd = getHome(name);
		return HomeTable.INSTANCE.delete(hd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(c -> homes.remove(c));
	}

	public HomeData getHome(String name) {
		return homes.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
	}

	public boolean hasHome(String name) {
		return homes.stream().anyMatch(c -> c.getName().equals(name));
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public boolean isOnline() {
		return Optional.ofNullable(getOnlinePlayer()).map(c -> (boolean) (c == null ? false : c.isOnline())).get();
	}

	public PlayerData loadHomes() {
		homes = new ArrayList<HomeData>();
		HomeTable.INSTANCE.query(HomeData.byPlayer(id)).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(c -> System.out.println("loaded: "+c.size()+" homes")).thenApply(homes::addAll).run();
		return this;
	}

	@GeneratedKeyUpdate(type = Type.INDEX)
	public void generatedKeyupdate(Integer bigInt) {
		this.id = bigInt.intValue();
	}

	@Reload
	public void reload(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.uuid = UUID.fromString(rs.getString("uuid"));
		this.name = rs.getString("name");
	}

	@Override
	public <T extends SQLEntry> String getPreparedInsertSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeInsert(table, new String[] { "uuid", "name" });
	}

	@Override
	public <T extends SQLEntry> String getPreparedUpdateSQL(DataBaseTable<T> table) {
		return SQLBuilder.safeUpdate(table, new String[] { "name" }, new String[] { "id" });
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
		stmt.setString(1, uuid.toString());
		stmt.setString(2, name);
	}

	@Override
	public void prepareUpdateSQL(PreparedStatement stmt) throws SQLException {
		stmt.setString(1, name);

		stmt.setInt(2, id);
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

	@UniqueKey("uuid")
	public UUID getUuid() {
		return uuid;
	}

	@UniqueKey("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HomeData> getHomes() {
		return homes;
	}

	@Override
	public String toString() {
		return "PlayerData [id=" + id + ", uuid=" + uuid + ", name=" + name + "]";
	}

	@Override
	public PlayerData clone() {
		return new PlayerData();
	}

	public static SQLQuery<PlayerData> byName(String name) {
		return new SafeSQLQuery<PlayerData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<PlayerData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "name" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, name);
			}

			@Override
			public PlayerData clone() {
				return new PlayerData();
			}
		};
	}

	public static SQLQuery<PlayerData> byUUID(UUID uuid) {
		return new SafeSQLQuery<PlayerData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<PlayerData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "uuid" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, uuid.toString());
			}

			@Override
			public PlayerData clone() {
				return new PlayerData();
			}
		};
	}

	public static SQLQuery<PlayerData> byId(int id) {
		return new SafeSQLQuery<PlayerData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<PlayerData> table) {
				return SQLBuilder.safeSelect(table, new String[] { "id" });
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, id);
			}

			@Override
			public PlayerData clone() {
				return new PlayerData();
			}
		};
	}

}
