package lu.kbra.multi_skyblock_utils.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import lu.kbra.multi_skyblock_utils.MultiSkyblockUtils;

public class CustomCrafts {

	public static void registerShapelessRecipe() {
		registerGravel();
		registerSand();
		registerCobbledDeepslate();
		registerSapling();
		registerDirt();
		registerQuartz();
	}
	
	private static void registerQuartz() {
		ItemStack customItem = new ItemStack(Material.QUARTZ);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_quartz");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.DIORITE);

		Bukkit.addRecipe(recipe);
	}

	public static void registerShapedRecipe() {
		registerLavaBucket();
	}

	private static void registerDirt() {
		ItemStack customItem = new ItemStack(Material.DIRT);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_dirt");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.GRAVEL);
		recipe.addIngredient(1, Material.SAND);

		Bukkit.addRecipe(recipe);
	}
	
	private static void registerGravel() {
		ItemStack customItem = new ItemStack(Material.GRAVEL);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_gravel");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.COBBLESTONE);

		Bukkit.addRecipe(recipe);
	}
	
	private static void registerSapling() {
		ItemStack customItem = new ItemStack(Material.OAK_SAPLING, 2);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_oak_sapling");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.OAK_SAPLING);
		recipe.addIngredient(8, Material.WHEAT_SEEDS);

		Bukkit.addRecipe(recipe);
	}
	
	private static void registerSand() {
		ItemStack customItem = new ItemStack(Material.SAND);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_sand");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.GRAVEL);

		Bukkit.addRecipe(recipe);
	}
	
	private static void registerCobbledDeepslate() {
		ItemStack customItem = new ItemStack(Material.COBBLED_DEEPSLATE, 8);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_cobbled_deepslate");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(8, Material.COBBLESTONE);
		recipe.addIngredient(1, Material.COAL);

		Bukkit.addRecipe(recipe);
	}

	private static void registerLavaBucket() {
		ItemStack customItem = new ItemStack(Material.LAVA_BUCKET);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_lava_bucket");

		ShapedRecipe recipe = new ShapedRecipe(key, customItem);

		recipe.shape("DBD", "xEx");
		
		recipe.setIngredient('B', Material.DIAMOND_BLOCK);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('E', Material.BUCKET);

		Bukkit.addRecipe(recipe);
	}

}
