package lu.kbra.extended_generators.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.items.GeneratorType;
import lu.kbra.extended_generators.utils.ItemManager;

public class CustomCraftsListener implements Listener {

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		final ItemStack[] matrix = inventory.getMatrix();

		if (matrix.length != 9)
			return;

		final ItemStack centerItem = matrix[4];
		if (centerItem == null)
			return;

		if (isSign(centerItem)) { // upgrade
			if (!centerItem.hasItemMeta())
				return;

			final ItemMeta itemMeta = centerItem.getItemMeta();
			if (itemMeta == null || itemMeta.getLore() == null)
				return;

			if (!itemMeta.getDisplayName().equals(ItemManager.TITLE))
				return;

			final int tier = Integer.parseInt(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", ""));
			final GeneratorType type = GeneratorType.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Type: ", ""));
			final Material affinity = itemMeta.getLore().get(2).contains("NONE") ? null : Material.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Affinity: ", ""));

			if (!isOuterRingUniform(matrix, 8))
				return;

			if (!type.getMaterials().contains(matrix[0].getType()))
				return;

			final ItemStack upgradedItem = ItemManager.getItem(tier + 1, type, affinity);
			inventory.setResult(upgradedItem);

		} else if (centerItem.getType().equals(Material.HOPPER)) { // actually craft a generator

			if (!isOuterRingUniform(matrix, 8))
				return;

			final GeneratorType type = GeneratorType.byItem(matrix[0].getType());

			if (type == null)
				return;

			final ItemStack outputItem = ItemManager.getItem(1, type, null);
			inventory.setResult(outputItem);

		}
	}

	@EventHandler
	public void onPrepareItemCraft(CraftItemEvent event) {
		final ItemStack craftedItem = event.getCurrentItem();

		System.out.println("CRAFTED4");
		if (!craftedItem.hasItemMeta())
			return;

		System.out.println("CRAFTED3");
		if (!isSign(craftedItem))
			return;

		System.out.println("CRAFTED2");
		final ItemMeta itemMeta = craftedItem.getItemMeta();
		if (itemMeta == null || itemMeta.getLore() == null)
			return;

		System.out.println("CRAFTED1");
		if (!itemMeta.getDisplayName().equals(ItemManager.TITLE))
			return;

		System.out.println("CRAFTED");
		
		final ItemStack[] matrix = event.getInventory().getMatrix();
		for (int i = 0; i < matrix.length; i++) {
			if (i == 4)
				continue;

			matrix[i].setAmount(matrix[i].getAmount() - 8);
		}
	}

	private boolean isOuterRingUniform(ItemStack[] matrix, int minCount) {
		if(matrix[0] == null)
			return false;
		
		final Material firstMaterial = matrix[0].getType();
		for (int i = 0; i < matrix.length; i++) {
			if (i == 4)
				continue;

			ItemStack item = matrix[i];
			if (item == null || item.getType() != firstMaterial || item.getAmount() < minCount) {
				return false;
			}
		}

		return true;
	}

	public static boolean isSign(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}

		Material material = itemStack.getType();

		return material == Material.OAK_SIGN || material == Material.SPRUCE_SIGN || material == Material.BIRCH_SIGN || material == Material.JUNGLE_SIGN || material == Material.DARK_OAK_SIGN || material == Material.ACACIA_SIGN
				|| material == Material.CHERRY_SIGN || material == Material.MANGROVE_SIGN;
	}

}
