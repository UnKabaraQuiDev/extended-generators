package lu.kbra.multi_skyblock_utils.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CookingBookCategory;

import lu.kbra.multi_skyblock_utils.MultiSkyblockUtils;

public class CustomCrafts {

	public static void registerShapelessRecipes() {
		registerGravel();
		registerBigGravel();
		registerSand();
		registerBigSand();
		registerCobbledDeepslate();
		registerSapling();
		registerDirt();
		registerBigDirt();
		registerQuartz();
	}

	private static void registerBigGravel() {
		ItemStack customItem = new ItemStack(Material.GRAVEL, 9);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_big_gravel");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		for (int i = 0; i < 9; i++)
			recipe.addIngredient(1, Material.COBBLESTONE);

		Bukkit.addRecipe(recipe);
	}

	private static void registerBigDirt() {
		ItemStack customItem = new ItemStack(Material.DIRT, 4);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_big_dirt");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		for (int i = 0; i < 4; i++) {
			recipe.addIngredient(1, Material.GRAVEL);
			recipe.addIngredient(1, Material.SAND);
		}

		Bukkit.addRecipe(recipe);
	}

	private static void registerBigSand() {
		ItemStack customItem = new ItemStack(Material.SAND, 9);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_big_sand");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		for (int i = 0; i < 9; i++)
			recipe.addIngredient(1, Material.GRAVEL);

		Bukkit.addRecipe(recipe);
	}

	private static void registerQuartz() {
		ItemStack customItem = new ItemStack(Material.QUARTZ);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_quartz");

		ShapelessRecipe recipe = new ShapelessRecipe(key, customItem);

		recipe.addIngredient(1, Material.DIORITE);

		Bukkit.addRecipe(recipe);
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

	public static void registerShapedRecipes() {
		registerLavaBucket();
		registerGrassBlock();
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

	private static void registerGrassBlock() {
		ItemStack customItem = new ItemStack(Material.GRASS_BLOCK);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_grass_block");

		ShapedRecipe recipe = new ShapedRecipe(key, customItem);

		recipe.shape("SSS", "SDS", "BBB");

		recipe.setIngredient('S', Material.WHEAT_SEEDS);
		recipe.setIngredient('D', Material.DIRT);
		recipe.setIngredient('B', Material.BONE_MEAL);

		Bukkit.addRecipe(recipe);
	}

	public static void registerFurnaceRecipes() {
		registerLeather();
		registerPolishedAndesite();
		registerPolishedDiorite();
		registerPolishedGranite();
	}

	public static void registerLeather() {
		ItemStack customItem = new ItemStack(Material.LEATHER);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_leather");

		FurnaceRecipe recipe = new FurnaceRecipe(key, customItem, Material.ROTTEN_FLESH, 0, 150);

		recipe.setCategory(CookingBookCategory.MISC);

		Bukkit.addRecipe(recipe);
	}

	public static void registerPolishedDiorite() {
		ItemStack customItem = new ItemStack(Material.POLISHED_DIORITE);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_polished_diorite");

		FurnaceRecipe recipe = new FurnaceRecipe(key, customItem, Material.DIORITE, 0, 150);

		recipe.setCategory(CookingBookCategory.MISC);

		Bukkit.addRecipe(recipe);
	}

	public static void registerPolishedAndesite() {
		ItemStack customItem = new ItemStack(Material.POLISHED_ANDESITE);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_polished_andesite");

		FurnaceRecipe recipe = new FurnaceRecipe(key, customItem, Material.ANDESITE, 0, 150);

		recipe.setCategory(CookingBookCategory.MISC);

		Bukkit.addRecipe(recipe);
	}

	public static void registerPolishedGranite() {
		ItemStack customItem = new ItemStack(Material.POLISHED_GRANITE);

		NamespacedKey key = new NamespacedKey(MultiSkyblockUtils.INSTANCE, "msu_polished_granite");

		FurnaceRecipe recipe = new FurnaceRecipe(key, customItem, Material.GRANITE, 0, 150);

		recipe.setCategory(CookingBookCategory.MISC);

		Bukkit.addRecipe(recipe);
	}

}
