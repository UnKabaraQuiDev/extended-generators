package lu.kbra.extended_generators.db.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

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
	private boolean confinedIsland, confinedHome;
	private List<String> blacklist = new ArrayList<>();
	private HashMap<String, Location> homes = new HashMap<>();

	public PlayerData() {
	}

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

	public boolean isConfinedHome() {
		return confinedHome;
	}

	public boolean isConfinedIsland() {
		return confinedIsland;
	}

	public void setConfinedHome(boolean confinedHome) {
		this.confinedHome = confinedHome;
	}

	public void setConfinedIsland(boolean confinedIsland) {
		this.confinedIsland = confinedIsland;
	}

	public void addIslandBlacklist(String name) {
		blacklist.add(PlayerManager.getPlayer(name).getUuid().toString());
	}

	public void removeIslandBlacklist(String name) {
		blacklist.remove(PlayerManager.getPlayer(name).getUuid().toString());
	}

	public boolean isBlacklisted(Player player) {
		System.err.println(name + " contains: " + blacklist);
		return blacklist.contains(player.getUniqueId().toString());
	}

	public List<String> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

	public HashMap<String, Location> getHomes() {
		return homes;
	}

	public void setHomes(HashMap<String, Location> homes) {
		this.homes = homes;
	}

	public void save(YamlConfiguration config) {
		ConfigurationSection sec = config.createSection(name);
		sec.set("name", name);
		sec.set("uuid", uuid.toString());
		sec.set("loc", islandLocation);
		sec.set("confinedHome", confinedHome);
		sec.set("confinedIsland", confinedIsland);
		sec.set("blacklist", blacklist);
		for (Entry<String, Location> home : homes.entrySet()) {
			sec.set("homes." + home.getKey(), home.getValue());
		}
	}

	public PlayerData load(ConfigurationSection sec) {
		if (sec == null)
			return null;

		this.uuid = UUID.fromString(sec.getString("uuid"));
		this.name = sec.getString("name");
		this.islandLocation = sec.getLocation("loc");
		this.confinedHome = sec.getBoolean("confinedHome", sec.getBoolean("confined", false));
		this.confinedIsland = sec.getBoolean("confinedIsland", sec.getBoolean("confined", false));
		this.blacklist = sec.getStringList("blacklist");

		if (sec.contains("homes")) {
			sec.getConfigurationSection("homes").getValues(false).forEach((String key, Object value) -> homes.put(key, (Location) value));
		}

		return this;
	}

	public List<String> autoCompleteHomeName(String beginning) {
		return homes.keySet().stream().filter(a -> a.toLowerCase().startsWith(beginning.toLowerCase())).collect(Collectors.toList());
	}

}
