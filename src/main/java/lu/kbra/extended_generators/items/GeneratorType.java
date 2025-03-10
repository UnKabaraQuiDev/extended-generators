package lu.kbra.extended_generators.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;

import lu.pcy113.pclib.PCUtils;

public enum GeneratorType {

	WOODS("Woods", PCUtils.hashMap(Material.OAK_LOG, 0.25, Material.BIRCH_LOG, 0.15, Material.SPRUCE_LOG, 0.1, Material.JUNGLE_LOG, 0.1, Material.DARK_OAK_LOG, 0.1, Material.ACACIA_LOG, 0.1, Material.MANGROVE_LOG, 0.1)),

	STONES("Stones",
			PCUtils.hashMap(Material.COBBLESTONE, 0.4, Material.STONE, 0.05, Material.DIORITE, 0.1, Material.ANDESITE, 0.1, Material.GRANITE, 0.05, Material.MOSSY_COBBLESTONE, 0.05, Material.COBBLED_DEEPSLATE, 0.2, Material.BASALT, 0.02,
					Material.NETHERRACK, 0.05)),
	
	ORGANICS("Organics", PCUtils.hashMap(Material.DIRT, 0.35, Material.GRASS_BLOCK, 0.25, Material.SAND, 0.15, Material.CLAY, 0.05, Material.GRAVEL, 0.05, Material.STONE, 0.05, Material.PODZOL, 0.05, Material.MYCELIUM, 0.05)),

	PLANTS("Plants",
			PCUtils.hashMap(Material.SHORT_GRASS, 0.4, Material.DANDELION, 0.2, Material.POPPY, 0.1, Material.BLUE_ORCHID, 0.05, Material.TALL_GRASS, 0.1, Material.FERN, 0.05, Material.ALLIUM, 0.05, Material.OXEYE_DAISY, 0.05,
					Material.LILY_OF_THE_VALLEY, 0.05)),

	ORES("Ores",
			PCUtils.hashMap(Material.COAL_ORE, 0.625, Material.IRON_ORE, 0.42, Material.COPPER_ORE, 0.20, Material.GOLD_ORE, 0.125, Material.REDSTONE_ORE, 0.04, Material.LAPIS_ORE, 0.02, Material.DIAMOND_ORE, 0.008, Material.EMERALD_ORE,
					0.001, Material.NETHER_QUARTZ_ORE, 0.9, Material.NETHER_GOLD_ORE, 0.6, Material.ANCIENT_DEBRIS, 0.001)),

	ORE_ITEMS("Ore items", PCUtils.hashMap(Material.COAL, 0.625, Material.RAW_IRON, 0.42, Material.RAW_COPPER, 0.20, Material.RAW_GOLD, 0.125, Material.REDSTONE, 0.04, Material.LAPIS_LAZULI, 0.02, Material.DIAMOND, 0.008, Material.EMERALD,
			0.001, Material.QUARTZ, 0.625, Material.GOLD_NUGGET, 0.2, Material.NETHERITE_SCRAP, 0.001));

	private String name;
	private HashMap<Material, Double> items;
	private double totalProbability;
	private List<Material> materials;

	private GeneratorType(String name, HashMap<Material, Double> items) {
		this.name = name;
		this.items = items;

		this.totalProbability = items.values().stream().mapToDouble(Double::doubleValue).sum();

		this.materials = items.keySet().stream().collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public HashMap<Material, Double> getItems() {
		return items;
	}

	public Material generateRandom() {
		double randomValue = Math.random() * totalProbability;

		double currentProbability = 0.0;
		for (Map.Entry<Material, Double> entry : items.entrySet()) {
			currentProbability += entry.getValue();
			if (randomValue <= currentProbability) {
				return entry.getKey();
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
