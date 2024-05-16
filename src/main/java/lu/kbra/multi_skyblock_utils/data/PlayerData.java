package lu.kbra.multi_skyblock_utils.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

	private UUID uuid;
	private String name;
	private Location islandLocation;

	public PlayerData() {}
	public PlayerData(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Location getIslandLocation() {
		return islandLocation;
	}

	public void setIslandLocation(Location islandLocation) {
		this.islandLocation = islandLocation;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public boolean isOnline() {
		return getOnlinePlayer() != null;
	}

	public void save(YamlConfiguration config) {
		ConfigurationSection sec = config.createSection(name);
		sec.set("name", name);
		sec.set("uuid", uuid.toString());
		sec.set("loc", islandLocation);
	}

	public PlayerData load(ConfigurationSection sec) {
		if(sec == null)
			return null;
		
		this.uuid = UUID.fromString(sec.getString("uuid"));
		this.name = sec.getString("name");
		this.islandLocation = sec.getLocation("loc");
		return this;
	}

}
