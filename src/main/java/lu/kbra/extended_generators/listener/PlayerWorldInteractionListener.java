package lu.kbra.extended_generators.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.GeneratorManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.items.GeneratorType;
import lu.kbra.extended_generators.utils.Cuboid;

public class PlayerWorldInteractionListener implements Listener {

	public static final String TITLE = ChatColor.AQUA.toString() + "Generator";

	public void onGeneratorSignPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			final Sign sign = (Sign) block.getState();

			final Player player = event.getPlayer();
			final ItemStack itemStack = event.getItemInHand();
			final ItemMeta itemMeta = itemStack.getItemMeta();

			if (itemMeta.getDisplayName().equals(TITLE)) {
				if (!(event.getBlockAgainst().getState() instanceof Container)) {
					player.sendMessage("Not a container !");
					event.setCancelled(true);
					return;
				}

				final int tier = Integer.parseInt(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", ""));
				final GeneratorType type = GeneratorType.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Type: ", ""));
				final Material affinity = itemMeta.getLore().get(2).contains("NONE") ? null : Material.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Affinity: ", ""));

				player.sendMessage("Loading ! Please wait...");

				PlayerManager.getPlayer(player).catch_(Exception::printStackTrace).thenConsume(pd -> {
					final ChunkData cm = ChunkManager.getOrCreateChunk(block.getLocation().getChunk()).catch_(Exception::printStackTrace).run();
					
					if(GeneratorManager.hasGenerator(block.getLocation()).run()) {
						player.sendMessage("There is already a generator registered at this location, please contact you server administrator.");
						return;
					}
					
					final GeneratorData gd = GeneratorManager.createGenerator(new GeneratorData(pd, block.getLocation(), type, affinity, tier)).catch_(Exception::printStackTrace).run();

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

	public void onGeneratorSignBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();
			String line1 = sign.getSide(Side.FRONT).getLine(0);

			if (ChatColor.stripColor(line1).equalsIgnoreCase(TITLE)) {
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "You have broken a Generator T1 sign!");
			}
		}
	}

	// Event handler for when a block is placed
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		onGeneratorSignPlace(event);
	}

	// Event handler for when a block is broken
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		onGeneratorSignBreak(event);
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
