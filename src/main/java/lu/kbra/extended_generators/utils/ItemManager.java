package lu.kbra.extended_generators.utils;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.items.GeneratorType;

public class ItemManager {

	public static final String TITLE = ChatColor.AQUA.toString() + "Generator";

	public static ItemStack getItem(int tier, GeneratorType type, Material affinity) {
		final ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ItemManager.TITLE);
		itemMeta.setLore(Arrays.asList("Tier: " + tier, "Type: " + type.name(), "Affinity: " + (affinity == null ? "NONE" : affinity.name())));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static GeneratorData getGeneratorData(final PlayerData pd, final ChunkData cd, final Location loc, final ItemStack itemStack) {
		final ItemMeta itemMeta = itemStack.getItemMeta();
		final int tier = Integer.parseInt(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", ""));
		System.err.println(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", "") + " gives " + tier);
		final GeneratorType type = GeneratorType.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Type: ", ""));
		final Material affinity = itemMeta.getLore().get(2).contains("NONE") ? null : Material.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Affinity: ", ""));
		return new GeneratorData(pd, cd, loc, type, affinity, tier);
	}

	public static ItemStack getItem(final GeneratorData gd) {
		final ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ItemManager.TITLE);
		itemMeta.setLore(Arrays.asList("Tier: " + gd.getTier(), "Type: " + gd.getType().name(), "Affinity: " + (gd.getAffinity() == null ? "NONE" : gd.getAffinity().name())));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

}
