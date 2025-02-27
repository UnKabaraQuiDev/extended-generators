package lu.kbra.extended_generators.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;

import lu.pcy113.pclib.PCUtils;

public enum GeneratorType {

	WOODS("Woods", PCUtils.hashMap(0.25, Material.OAK_LOG, 0.15, Material.BIRCH_LOG, 0.1, Material.SPRUCE_LOG, 0.1, Material.JUNGLE_LOG, 0.1, Material.DARK_OAK_LOG, 0.1, Material.ACACIA_LOG, 0.1, Material.MANGROVE_LOG)),

	STONES("Stones",
			PCUtils.hashMap(0.4, Material.COBBLESTONE, 0.05, Material.STONE, 0.1, Material.DIORITE, 0.1, Material.ANDESITE, 0.05, Material.GRANITE, 0.05, Material.MOSSY_COBBLESTONE, 0.2, Material.COBBLED_DEEPSLATE, 0.02, Material.BASALT, 0.05,
					Material.NETHERRACK)),

	ORGANICS("Organics", PCUtils.hashMap(0.35, Material.DIRT, 0.25, Material.GRASS_BLOCK, 0.15, Material.SAND, 0.05, Material.CLAY, 0.05, Material.GRAVEL, 0.05, Material.STONE, 0.05, Material.PODZOL, 0.05, Material.MYCELIUM)),

	PLANTS("Plants",
			PCUtils.hashMap(0.4, Material.SHORT_GRASS, 0.2, Material.DANDELION, 0.1, Material.POPPY, 0.05, Material.BLUE_ORCHID, 0.1, Material.TALL_GRASS, 0.05, Material.FERN, 0.05, Material.ALLIUM, 0.05, Material.OXEYE_DAISY, 0.05,
					Material.LILY_OF_THE_VALLEY)),

	ORES("Ores",
			PCUtils.hashMap(0.625, Material.COAL_ORE, 0.42, Material.IRON_ORE, 0.20, Material.COPPER_ORE, 0.125, Material.GOLD_ORE, 0.04, Material.REDSTONE_ORE, 0.02, Material.LAPIS_ORE, 0.008, Material.DIAMOND_ORE, 0.001, Material.EMERALD_ORE, 0.9,
					Material.NETHER_QUARTZ_ORE, 0.6, Material.NETHER_GOLD_ORE, 0.001, Material.ANCIENT_DEBRIS)),

	ORE_ITEMS("Ore items", PCUtils.hashMap(0.625, Material.COAL, 0.42, Material.RAW_IRON, 0.20, Material.RAW_COPPER, 0.125, Material.RAW_GOLD, 0.04, Material.REDSTONE, 0.02, Material.LAPIS_LAZULI, 0.008, Material.DIAMOND, 0.001, Material.EMERALD,
			0.9, Material.QUARTZ, 0.6, Material.NETHER_GOLD_ORE, 0.001, Material.NETHERITE_SCRAP));

	private String name;
	private HashMap<Double, Material> items;
	private double totalProbability;
	private List<Material> materials;

	private GeneratorType(String name, HashMap<Double, Material> items) {
		this.name = name;
		this.items = items;

		for (Double probability : items.keySet()) {
			totalProbability += probability;
		}

		this.materials = items.values().stream().collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public HashMap<Double, Material> getItems() {
		return items;
	}

	public Material generateRandom() {
		double randomValue = Math.random() * totalProbability;

		double currentProbability = 0.0;
		for (Map.Entry<Double, Material> entry : items.entrySet()) {
			currentProbability += entry.getKey();
			if (randomValue <= currentProbability) {
				return entry.getValue();
			}
		}

		return null;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public static GeneratorType byItem(Material type) {
		return Arrays.stream(values()).filter((GeneratorType c) -> c.materials.contains(type)).findFirst().orElse(null);
	}

}
