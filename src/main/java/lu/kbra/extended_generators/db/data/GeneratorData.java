package lu.kbra.extended_generators.db.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.impl.SQLQuery;
import lu.pcy113.pclib.db.impl.SQLQuery.SafeSQLQuery;

public class GeneratorData {

	private String name;
	private UUID uuid;
	private PlayerData playerData;
	private Location location;

	public GeneratorData() {
	}

	public GeneratorData(PlayerData playerData, Location location) {
		this(UUID.randomUUID(), playerData, location);
	}

	public GeneratorData(UUID uuid, PlayerData playerData, Location location) {
		this.name = PCUtils.hashString(uuid.toString(), "sha-256");
		this.uuid = uuid;
		this.playerData = playerData;
		this.location = location;
	}

	public GeneratorData(String name, UUID uuid, PlayerData playerData, Location location) {
		this.name = name;
		this.uuid = uuid;
		this.playerData = playerData;
		this.location = location;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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

	public void save(YamlConfiguration config) {
		ConfigurationSection sec = config.createSection(name);
		sec.set("name", name);
		sec.set("uuid", uuid.toString());
		sec.set("loc", location);
	}

	public GeneratorData load(ConfigurationSection sec) {
		if (sec == null)
			return null;

		this.name = sec.getName();
		this.uuid = UUID.fromString(sec.getString("uuid"));
		this.name = sec.getString("name");
		this.location = sec.getLocation("loc");

		return this;
	}

	public static SQLQuery<GeneratorData> byChunk(Chunk chunk) {
		return new SafeSQLQuery<GeneratorData>() {
			@Override
			public String getPreparedQuerySQL(DataBaseTable<GeneratorData> table) {
				return null;
			}

			@Override
			public void updateQuerySQL(PreparedStatement stmt) throws SQLException {

			}
		};
	}

}
