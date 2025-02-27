package lu.kbra.extended_generators.listener;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.GeneratorManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.utils.Cuboid;
import lu.kbra.extended_generators.utils.ItemManager;

public class PlayerWorldInteractionListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			final Sign sign = (Sign) block.getState();

			final Player player = event.getPlayer();
			final ItemStack itemStack = event.getItemInHand();
			final ItemMeta itemMeta = itemStack.getItemMeta();

			if (itemMeta.getDisplayName().equals(ItemManager.TITLE)) {
				if (!(event.getBlockAgainst().getState() instanceof Container)) {
					player.sendMessage("Not a container !");
					event.setCancelled(true);
					return;
				}

				player.sendMessage("Loading ! Please wait...");

				PlayerManager.getPlayer(player).catch_(Exception::printStackTrace).thenConsume(pd -> {
					final ChunkData cm = ChunkManager.getOrCreateChunk(block.getLocation().getChunk()).catch_(Exception::printStackTrace).run();

					if (GeneratorManager.hasGenerator(block.getLocation()).run()) {
						player.sendMessage("There is already a generator registered at this location, please contact you server administrator.");
						return;
					}

					final GeneratorData gd = GeneratorManager.createGenerator(ItemManager.getGeneratorData(pd, cm, block.getLocation(), itemStack)).catch_(Exception::printStackTrace).run();

					ExtendedGenerators.INSTANCE.run(() -> {
						player.closeInventory();

						sign.getSide(Side.FRONT).setLine(0, itemMeta.getDisplayName());
						sign.getSide(Side.FRONT).setLine(1, itemMeta.getLore().get(0));
						sign.getSide(Side.FRONT).setLine(2, itemMeta.getLore().get(1));
						sign.getSide(Side.FRONT).setLine(3, itemMeta.getLore().get(2));
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
		Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();

			if (sign.getSide(Side.FRONT).getLine(0).equals(ItemManager.TITLE)) {
				Player player = event.getPlayer();

				event.setDropItems(false);
				PlayerManager.getPlayer(player).catch_(Exception::printStackTrace).thenConsume(pd -> {
					if (!GeneratorManager.hasGenerator(block.getLocation()).run()) {
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
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			Player player = event.getPlayer();

			Cuboid cb = new Cuboid(player.getWorld(), player.getLocation().clone().add(5, 2, 5), player.getLocation().clone().subtract(5, 2, 5));
			cb.forEach((b) -> {
				if (b.getBlockData() instanceof Sapling) {
					b.applyBoneMeal(BlockFace.NORTH);
				}
			});
		}
	}

}
