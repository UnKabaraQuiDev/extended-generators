package lu.kbra.multi_skyblock_utils.listener;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import lu.kbra.multi_skyblock_utils.utils.Cuboid;

public class PlayerWorldInteractionListener implements Listener {

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (!event.hasItem() || !event.hasBlock())
			return;

		if (Material.BUCKET.equals(event.getItem().getType()) && Material.OBSIDIAN.equals(event.getClickedBlock().getType())) {
			event.setCancelled(true);

			event.getClickedBlock().setType(Material.AIR);
			int bucketItemIndex = event.getPlayer().getInventory().first(Material.BUCKET);
			event.getPlayer().getInventory().getItem(bucketItemIndex).setAmount(event.getPlayer().getInventory().getItem(bucketItemIndex).getAmount() - 1);
			event.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET, 1));
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
