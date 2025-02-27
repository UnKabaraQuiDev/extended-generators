package lu.kbra.extended_generators.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.datastructure.triplet.ReadOnlyTriplet;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.datastructure.triplet.Triplets;
import lu.pcy113.pclib.pointer.prim.LongPointer;

public class WorldWorldInteractionListener implements Listener {

	public static WorldWorldInteractionListener INSTANCE;

	public final List<Pair<Material, Double>> basicProbabilities;
	public final Map<Material, List<Pair<Material, Double>>> upgradedProbabilities;

	public final Map<Material, Map<Material, LongPointer>> upgradedGenerators = new HashMap<>();
	public final Map<Material, LongPointer> basicGenerators = new HashMap<>();

	public WorldWorldInteractionListener() {
		INSTANCE = this;

		basicProbabilities = new ArrayList<Pair<Material, Double>>();
		ExtendedGenerators.INSTANCE.getConfig().getConfigurationSection("generator.basic").getValues(false).entrySet().forEach((Entry<String, Object> entry) -> {
			System.out.println("Generator for: " + entry.getKey() + ": " + entry.getValue());

			basicProbabilities.add(Pairs.<Material, Double>readOnly(Material.valueOf(entry.getKey().toUpperCase()), Double.valueOf(entry.getValue().toString())));
		});

		Collections.sort(basicProbabilities, (a, b) -> (int) Math.signum(a.getValue() - b.getValue()));

		upgradedProbabilities = new HashMap<Material, List<Pair<Material, Double>>>();
		for (String materialKey : ExtendedGenerators.INSTANCE.getConfig().getConfigurationSection("generator.upgrades").getKeys(false)) {
			String material = ExtendedGenerators.INSTANCE.getConfig().getString("generator.upgrades." + materialKey);

			Material matTop = Material.valueOf(materialKey.toUpperCase());
			Material matGet = Material.valueOf(material.toUpperCase());

			List<Pair<Material, Double>> nList = new ArrayList<Pair<Material, Double>>();

			if (ExtendedGenerators.INSTANCE.getConfig().contains("generator.upgraded." + material)) {
				double upgradeForMat = ExtendedGenerators.INSTANCE.getConfig().getDouble("generator.upgraded." + material);

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

			Triplet<Boolean, Material, List<Pair<Material, Double>>> probabilities = getProbabilities(block);

			Material newType = selectWithProbability(probabilities.getThird(), Material.COBBLESTONE);

			if (probabilities.getFirst()) {
				upgradedGenerators.computeIfAbsent(probabilities.getSecond(), (m) -> new HashMap<Material, LongPointer>());
				upgradedGenerators.get(probabilities.getSecond()).computeIfAbsent(newType, (m) -> new LongPointer(0L));
				upgradedGenerators.get(probabilities.getSecond()).get(newType).increment();
			} else {
				basicGenerators.computeIfAbsent(newType, (m) -> new LongPointer(0L));
				basicGenerators.get(newType).increment();
			}

			event.setCancelled(true);
			block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(newType, true);

			return;
		}

	}

	public ReadOnlyTriplet<Boolean, Material, List<Pair<Material, Double>>> getProbabilities(Block block) {
		Location loc = block.getLocation();

		Material supType = block.getWorld().getBlockAt(loc.add(0, 1, 0)).getType(), botType = block.getWorld().getBlockAt(loc.subtract(0, 1, 0)).getType();

		if (upgradedProbabilities.containsKey(supType)) {
			return Triplets.readOnly(true, supType, upgradedProbabilities.get(supType));
		} else if (upgradedProbabilities.containsKey(botType)) {
			return Triplets.readOnly(true, botType, upgradedProbabilities.get(botType));
		} else {
			return Triplets.readOnly(false, null, basicProbabilities);
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

	public void printProbabilitiesStats(Consumer<String> cons) {
		long totalUpgraded = upgradedGenerators.values().stream().flatMap((s) -> s.values().stream()).mapToLong(LongPointer::getValue).sum();
		long totalBasic = basicGenerators.values().stream().mapToLong(LongPointer::getValue).sum();
		long totalAll = totalUpgraded + totalBasic;

		cons.accept("Upgraded generators (total=" + totalUpgraded + "):");
		upgradedGenerators.forEach((Material m, Map<Material, LongPointer> k) -> {
			long totalThisUpgrade = k.values().stream().mapToLong((s) -> s.getValue()).sum();
			cons.accept("- Upgrade " + m + " (total=" + totalThisUpgrade + ") :");
			k.forEach((Material m2, LongPointer lp) -> cons.accept(" |- " + m2 + " -> " + lp.getValue() + " (" + PCUtils.round(((double) lp.getValue() / totalThisUpgrade) * 100, 2) + "% of " + m + ", "
					+ PCUtils.round(((double) lp.getValue() / totalUpgraded) * 100, 2) + "% of upgraded generators, " + PCUtils.round(((double) lp.getValue() / totalAll) * 100, 2) + "% of total)"));
		});

		cons.accept("Basic generators (total=" + totalBasic + "):");
		basicGenerators.forEach((Material m, LongPointer lp) -> cons
				.accept(" |- " + m + " -> " + lp.getValue() + " (" + PCUtils.round(((double) lp.getValue() / totalBasic) * 100, 2) + "% of basic generators, " + PCUtils.round(((double) lp.getValue() / totalAll) * 100, 2) + "% of total)"));

	}

}
