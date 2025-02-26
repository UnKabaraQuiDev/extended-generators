package lu.kbra.extended_generators.listener;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.datastructure.triplet.ReadOnlyTriplet;

import lu.kbra.extended_generators.utils.Cuboid;

public class PlayerWorldInteractionListener implements Listener {

	// private static final String PROBABILITY_STICK_NAME = ChatColor.GOLD + "Probability " + ChatColor.WHITE + "Stick";

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

		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && Material.COBBLESTONE.equals(event.getItem().getType()) /* && PROBABILITY_STICK_NAME.equals(event.getItem().getItemMeta().getDisplayName()) */) {
			ReadOnlyTriplet<Boolean, Material, List<Pair<Material, Double>>> probabilities = WorldWorldInteractionListener.INSTANCE.getProbabilities(event.getClickedBlock());

			event.getPlayer().sendMessage(ChatColor.GOLD + "Cobblestone generator probabilities: ");
			if (probabilities.getFirst()) {
				event.getPlayer().sendMessage(ChatColor.WHITE + "- Advanced generator (" + ChatColor.YELLOW + probabilities.getSecond() + "): ");

				probabilities.getThird().forEach((Pair<Material, Double> pair) -> {
					Pair<Material, Double> basic = WorldWorldInteractionListener.INSTANCE.basicProbabilities.stream().filter(c -> c.getKey().equals(pair.getKey())).findFirst().orElseGet(() -> Pairs.empty());

					event.getPlayer().sendMessage(ChatColor.WHITE + " |- " + ChatColor.AQUA + pair.getKey() + ChatColor.WHITE + ": " + ChatColor.GREEN + basic.getValue() + ChatColor.WHITE + " -> "
							+ (!basic.getValue().equals(pair.getValue()) ? ChatColor.YELLOW : ChatColor.GREEN) + pair.getValue() + "%");
				});
			} else {
				event.getPlayer().sendMessage(ChatColor.WHITE + "- Basic generator: ");

				probabilities.getThird().forEach((Pair<Material, Double> pair) -> {
					event.getPlayer().sendMessage(ChatColor.WHITE + " |- " + ChatColor.AQUA + pair.getKey() + ChatColor.WHITE + ": " + ChatColor.YELLOW + pair.getValue() + "%");
				});
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
