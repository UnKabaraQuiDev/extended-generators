package lu.kbra.extended_generators.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.GeneratorManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.items.GeneratorType;
import lu.kbra.extended_generators.utils.Cuboid;

public class PlayerWorldInteractionListener implements Listener {

	public static final String TITLE = ChatColor.UNDERLINE.toString() + ChatColor.AQUA.toString() + "Generator";

	public void onGeneratorSignPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();
		if (block.getState() instanceof final Sign sign) {
			String line1 = sign.getLine(0);

			final ItemStack itemStack = event.getItemInHand();
			final ItemMeta itemMeta = itemStack.getItemMeta();

			if (itemMeta.getDisplayName().equals(TITLE)) {
				final int tier = Integer.parseInt(ChatColor.stripColor(itemMeta.getLore().get(0)).replace("Tier: ", ""));
				final GeneratorType type = GeneratorType.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Type: ", ""));
				final Material affinity = itemMeta.getLore().get(2).contains("NONE") ? null : Material.valueOf(ChatColor.stripColor(itemMeta.getLore().get(1)).replace("Affinity: ", ""));

				final Player player = event.getPlayer();
				player.sendMessage("Loading ! Please wait...");;
				
				PlayerManager.getPlayer(player).thenConsume(pd -> {
					final ChunkData cm = ChunkManager.getOrCreateChunk(player.getLocation().getChunk()).run();
					final GeneratorData gd = GeneratorManager.createGenerator(new GeneratorData(pd, block.getLocation(), type, affinity, tier)).run();
				});
			}
		}
	}

	// Function that is called when the custom sign is broken
	public void onGeneratorSignBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();
			String line1 = sign.getLine(0);

			// Check if the sign is the "Generator T1"
			if (ChatColor.stripColor(line1).equalsIgnoreCase(TITLE)) {
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "You have broken a Generator T1 sign!");
				// Custom logic when the sign is broken
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
