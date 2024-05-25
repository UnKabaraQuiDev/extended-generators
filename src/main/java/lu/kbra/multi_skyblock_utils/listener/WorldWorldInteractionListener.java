package lu.kbra.multi_skyblock_utils.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import lu.pcy113.pclib.Pair;
import lu.pcy113.pclib.Pairs;

import lu.kbra.multi_skyblock_utils.MultiSkyblockUtils;

public class WorldWorldInteractionListener implements Listener {

	private List<Pair<Material, Double>> basicProbabilities;
	private Map<Material, List<Pair<Material, Double>>> upgradedProbabilities;

	public WorldWorldInteractionListener() {
		basicProbabilities = new ArrayList<Pair<Material, Double>>();
		MultiSkyblockUtils.INSTANCE.getConfig().getConfigurationSection("generator.basic").getValues(false).entrySet().forEach((Entry<String, Object> entry) -> {
			System.out.println("Generator for: " + entry.getKey() + ": " + entry.getValue());

			basicProbabilities.add(Pairs.<Material, Double>readOnly(Material.valueOf(entry.getKey().toUpperCase()), Double.valueOf(entry.getValue().toString())));
		});

		Collections.sort(basicProbabilities, (a, b) -> (int) Math.signum(a.getValue() - b.getValue()));

		upgradedProbabilities = new HashMap<Material, List<Pair<Material, Double>>>();
		for (String materialKey : MultiSkyblockUtils.INSTANCE.getConfig().getConfigurationSection("generator.upgrades").getKeys(false)) {
			String material = MultiSkyblockUtils.INSTANCE.getConfig().getString("generator.upgrades." + materialKey);

			Material matTop = Material.valueOf(materialKey.toUpperCase());
			Material matGet = Material.valueOf(material.toUpperCase());

			List<Pair<Material, Double>> nList = new ArrayList<Pair<Material, Double>>();

			if (MultiSkyblockUtils.INSTANCE.getConfig().contains("generator.upgraded." + material)) {
				double upgradeForMat = MultiSkyblockUtils.INSTANCE.getConfig().getDouble("generator.upgraded." + material);

				System.out.println("Upgrade for: " + matGet + " -> " + matTop + ": " + upgradeForMat);

				basicProbabilities.forEach((Pair<Material, Double> pair) -> {
					nList.add(Pairs.<Material, Double>readOnly(pair.getKey(), pair.getKey().equals(matGet) ? upgradeForMat : pair.getValue()));
				});

				Collections.sort(nList, (a, b) -> (int) Math.signum(a.getValue() - b.getValue()));

				upgradedProbabilities.put(matTop, nList);
				System.out.println(matTop + " -> " + nList);
			}
		}

		System.out.println(basicProbabilities);

	}

	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (Material.COBBLESTONE.equals(event.getNewState().getType())) {
			Block block = event.getBlock();
			Location loc = event.getBlock().getLocation();

			Material supType = block.getWorld().getBlockAt(loc.add(0, 1, 0)).getType(), botType = block.getWorld().getBlockAt(loc.subtract(0, 1, 0)).getType();

			if (!upgradedProbabilities.containsKey(supType)) {
				if (!upgradedProbabilities.containsKey(botType)) {
					Material newType = selectWithProbability(basicProbabilities, Material.COBBLESTONE);

					event.setCancelled(true);
					block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(newType, true);

					return;
				} else {
					supType = botType;

					Material newType = selectWithProbability(upgradedProbabilities.get(supType), Material.COBBLESTONE);

					event.setCancelled(true);
					block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(newType, true);
				}
			}

			return;
		}

	}

	public static Material selectWithProbability(List<Pair<Material, Double>> probabilities, Material defaultObject) {
		double randomNumber = Math.random() * 100.0;

		Material type = null;

		double cumulative = 0;
		for (Pair<Material, Double> pair : probabilities) {
			cumulative += pair.getValue();
			if (randomNumber <= cumulative) {
				type = pair.getKey();
				break;
			}
		}

		return type == null ? defaultObject : type;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.GOLD + "Cobblestone generator probabilities: ");

		event.getPlayer().sendMessage(ChatColor.WHITE + "- Basic generator: ");
		basicProbabilities.forEach((Pair<Material, Double> pair) -> {
			event.getPlayer().sendMessage(ChatColor.WHITE + " |- " + ChatColor.GREEN + pair.getKey() + ChatColor.WHITE + " -> " + ChatColor.YELLOW + pair.getValue() + "%");
		});

		event.getPlayer().sendMessage(ChatColor.WHITE + "- Advanced generators: ");
		upgradedProbabilities.keySet().forEach((Material key) -> {
			upgradedProbabilities.get(key).forEach((Pair<Material, Double> pair) -> {
				double basicValue = basicProbabilities.stream().filter(a -> a.getKey().equals(pair.getKey())).findFirst().orElseGet(() -> Pairs.<Material, Double>readOnly(null, 0.0)).getValue();

				if (pair.getValue() != basicValue) {
					event.getPlayer().sendMessage(ChatColor.WHITE + " |- " + ChatColor.AQUA + key + ChatColor.WHITE + ": " + ChatColor.GREEN + pair.getKey() + ChatColor.WHITE + " -> " + ChatColor.YELLOW + pair.getValue() + "%");
				}
			});
		});
	}

	@Deprecated
	private static Material chooseType() {
		double rand = Math.random();
		return rand < 0.0001 ? Material.ANCIENT_DEBRIS
				: (rand < 0.005 ? Material.DIAMOND_ORE
						: (rand < 0.01 ? Material.GOLD_ORE
								: (rand < 0.03 ? Material.IRON_ORE
										: (rand < 0.08 ? Material.COAL_ORE : (rand < 0.10 ? Material.REDSTONE_ORE : (rand < 0.12 ? Material.LAPIS_ORE : (rand < 0.20 ? Material.COBBLED_DEEPSLATE : Material.COBBLESTONE)))))));
	}

	public static void main(String[] args) {
		HashMap<Material, Integer> counter = new HashMap<>();
		final long MAX = 100_000_000L;
		for (long i = 0; i < MAX; i++) {
			Material type = chooseType();

			counter.put(type, counter.getOrDefault(type, 0) + 1);
		}

		System.out.println("Probabilities on: " + MAX);
		counter.entrySet().forEach(s -> System.out.println(s.getKey() + " > " + ((float) s.getValue() / MAX) * 100 + "%"));
	}

}
