package lu.kbra.extended_generators.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.GeneratorManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.items.GeneratorType;
import lu.kbra.extended_generators.utils.ItemManager;

public class GeneratorsListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			final Sign sign = (Sign) block.getState();

			final Player player = event.getPlayer();
			final ItemStack itemStack = event.getItemInHand();
			
			if(!itemStack.hasItemMeta()) {
				return;
			}
			final ItemMeta itemMeta = itemStack.getItemMeta();

			final String displayName = itemMeta.getDisplayName(), lore0 = itemMeta.getLore().get(0), lore1 = itemMeta.getLore().get(1), lore2 = itemMeta.getLore().get(2);
			
			if (displayName.equals(ItemManager.TITLE)) {
				if (!(event.getBlockAgainst().getState() instanceof Container)) {
					player.sendMessage("Not a container !");
					event.setCancelled(true);
					return;
				}

				player.sendMessage("Loading ! Please wait...");

				final int tier = Integer.parseInt(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", ""));
				final GeneratorType type = GeneratorType.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Type: ", ""));
				final Material affinity = itemMeta.getLore().get(2).contains("NONE") ? null : Material.valueOf(ChatColor.stripColor(itemMeta.getLore().get(2)).replace("Affinity: ", ""));
				
				PlayerManager.getPlayer(player).catch_(Exception::printStackTrace).thenConsume(pd -> {
					final ChunkData cm = ChunkManager.getOrCreateChunk(block.getLocation().getChunk()).catch_(Exception::printStackTrace).run();

					if (GeneratorManager.hasGenerator(block.getLocation()).run()) {
						player.sendMessage("There is already a generator registered at this location, please contact you server administrator.");
						return;
					}

					final GeneratorData gd = GeneratorManager.createGenerator(ItemManager.getGeneratorData(pd, cm, block.getLocation(), tier, type, affinity)).catch_(Exception::printStackTrace).run();

					ExtendedGenerators.INSTANCE.run(() -> {
						player.closeInventory();

						sign.getSide(Side.FRONT).setLine(0, displayName);
						sign.getSide(Side.FRONT).setLine(1, lore0);
						sign.getSide(Side.FRONT).setLine(2, lore1);
						sign.getSide(Side.FRONT).setLine(3, lore2);
						sign.setWaxed(true);
						sign.update();

						player.sendMessage("Ok !");
					});
				}).runAsync();
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		final Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			final Sign sign = (Sign) block.getState();

			if (sign.getSide(Side.FRONT).getLine(0).equals(ItemManager.TITLE)) {
				final Player player = event.getPlayer();

				event.setDropItems(false);
				GeneratorManager.hasGenerator(block.getLocation()).thenConsume(exists -> {
					if (!exists) {
						player.sendMessage("There is no generator registered at this location, please contact you server administrator.");
						return;
					}

					final GeneratorData gd = GeneratorManager.getGenerator(block.getLocation()).catch_(Exception::printStackTrace).run();
					GeneratorManager.remove(gd).run();

					ExtendedGenerators.INSTANCE.run(() -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), ItemManager.getItem(gd)));
				}).runAsync();
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		if (!event.getPlayer().isSneaking()) {
			return;
		}

		if (block.getState() instanceof Sign) {
			final Sign sign = (Sign) block.getState();

			if (sign.getSide(Side.FRONT).getLine(0).equals(ItemManager.TITLE)) {
				final Player player = event.getPlayer();

				GeneratorManager.hasGenerator(block.getLocation()).thenConsume(exists -> {
					if (!exists) {
						player.sendMessage("There is no generator registered at this location, please contact you server administrator.");
						return;
					}

					final GeneratorData gd = GeneratorManager.getGenerator(block.getLocation()).catch_(Exception::printStackTrace).run();

					if (!gd.getType().getMaterials().contains(event.getMaterial())) {
						return;
					}

					gd.setAffinity(event.getMaterial());

					GeneratorManager.update(gd).run();

					ExtendedGenerators.INSTANCE.run(() -> {
						sign.getSide(Side.FRONT).setLine(3, event.getMaterial().name());
						sign.update();
						player.sendMessage("Changed affinity to: " + event.getMaterial());
					});
				}).runAsync();
			}
		}
	}

}
