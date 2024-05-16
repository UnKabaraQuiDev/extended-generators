package lu.kbra.multi_skyblock_utils.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lu.kbra.multi_skyblock_utils.MultiSkyblockUtils;
import lu.kbra.multi_skyblock_utils.utils.Cuboid;

public class PlayerManager {

	private static final File FILE = new File(MultiSkyblockUtils.INSTANCE.getDataFolder(), "playerData.yml");

	private static YamlConfiguration config;
	private static List<PlayerData> playerData = new ArrayList<>();

	public static void enable() {

		config = new YamlConfiguration();
		try {
			if (!FILE.exists()) {
				FILE.createNewFile();
			}

			config.load(FILE);

			config.addDefault("usedIslands", new ArrayList<Location>());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		load();

	}

	public static void load() {
		for (String s : config.getKeys(false)) {
			PlayerData pd = new PlayerData().load(config.getConfigurationSection(s));
			if (pd != null)
				playerData.add(pd);
		}
	}

	public static void disable() {
		save();
		playerData.clear();
	}

	public static void save() {
		for (PlayerData pd : playerData) {
			pd.save(config);
		}

		try {
			config.save(FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void joined(Player player) {
		if (knowsPlayer(player))
			return;

		// new player
		playerData.add(new PlayerData(player.getUniqueId(), player.getName()));

		genIsland(player);

		save();
	}

	private static void genIsland(Player player) {

		Location islandLocation = getFreeIslandLocation(5);

		if (islandLocation == null) {
			MultiSkyblockUtils.INSTANCE.getLogger().severe("No free island location found for: " + player.getName());
			player.kickPlayer("No free island location found for: " + player.getName());
			return;
		}

		System.out.println("Generating island at: " + islandLocation);

		World w = islandLocation.getWorld();
		Cuboid cb = new Cuboid(w, islandLocation.clone().subtract(3, 3, 1), islandLocation.clone().add(4, 1, 2));
		cb.fill(Material.GRASS_BLOCK);
		w.getBlockAt(islandLocation.clone().subtract(0, 2, 0)).setType(Material.BEDROCK);
		w.getBlockAt(islandLocation.clone().add(2, 1, 0)).setType(Material.OAK_SAPLING);
		Location chestLocation = islandLocation.clone().add(-2, 1, 0);
		w.getBlockAt(chestLocation).setType(Material.CHEST);

		genFillChest((Chest) w.getBlockState(chestLocation));

		getPlayer(player).setIslandLocation(islandLocation.clone().add(0.5, 1, 0.5));
		addUsedLocation(islandLocation);

		player.teleport(getPlayer(player).getIslandLocation());
	}

	private static void genFillChest(Chest chest) {
		chest.getBlockInventory().addItem(new ItemStack(Material.APPLE, 64));
		chest.getBlockInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		chest.getBlockInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		chest.getBlockInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		chest.getBlockInventory().addItem(new ItemStack(Material.LAVA_BUCKET, 1));
		chest.getBlockInventory().addItem(new ItemStack(Material.CARROT, 4));
		chest.getBlockInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 8));
		chest.getBlockInventory().addItem(new ItemStack(Material.MELON_SLICE, 4));
		chest.getBlockInventory().addItem(new ItemStack(Material.PUMPKIN, 2));
		chest.getBlockInventory().addItem(new ItemStack(Material.SUGAR_CANE, 4));
		chest.getBlockInventory().addItem(new ItemStack(Material.OAK_SAPLING, 3));
	}

	private static void addUsedLocation(Location loc) {
		List<Location> ll = (List<Location>) config.getList("usedIslands");
		ll.add(loc);
		config.set("usedIslands", ll);
	}

	private static Location getFreeIslandLocation(int leftTries) {
		Location loc = new Location(Bukkit.getServer().getWorld("world"), (int) ((Math.random() * 20 - 10) * 20), 100, (int) ((Math.random() * 20 - 10) * 20));

		if (config.getList("usedIslands", new ArrayList<Location>()).isEmpty()) {
			return loc;
		}

		for (Object l2 : config.getList("usedIslands", new ArrayList<Location>())) {
			if (loc.distance((Location) l2) > 50)
				return loc;
		}

		if (leftTries > 0) {
			return getFreeIslandLocation(leftTries - 1);
		}

		return null;
	}

	public static boolean knowsPlayer(String name) {
		return playerData.stream().filter((a) -> a.getName().equals(name)).count() > 0;
	}

	public static PlayerData getPlayer(String name) {
		return playerData.stream().filter((a) -> a.getName().equals(name)).findFirst().orElse(null);
	}

	public static boolean knowsPlayer(UUID uuid) {
		return playerData.stream().filter((a) -> a.getUuid().equals(uuid)).count() > 0;
	}

	public static PlayerData getPlayer(UUID uuid) {
		return playerData.stream().filter((a) -> a.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	public static boolean knowsPlayer(Player player) {
		return playerData.stream().filter((a) -> a.getUuid().equals(player.getUniqueId())).count() > 0;
	}

	public static PlayerData getPlayer(Player player) {
		return playerData.stream().filter((a) -> a.getUuid().equals(player.getUniqueId())).findFirst().orElse(null);
	}

	public static void clearConfig() {
		config = null;
		config = new YamlConfiguration();
		config.addDefault("usedIslands", new ArrayList<Location>());

		playerData.clear();
	}

	public static List<PlayerData> getPlayerData() {
		return playerData;
	}

}
