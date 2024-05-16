package lu.kbra.multi_skyblock_utils.listener;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class WorldWorldInteractionListener implements Listener {

	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		Block block = event.getBlock();

		// Check if cobblestone is generated from water and lava
		if (Material.COBBLESTONE.equals(event.getNewState().getType())) {
			Material newType = chooseType();

			event.setCancelled(true);
			block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(newType, true);

		}
	}

	private static Material chooseType() {
		double rand = Math.random();
		return rand < 0.0001 ? Material.ANCIENT_DEBRIS
				: (rand < 0.005 ? Material.DIAMOND_ORE
				: (rand < 0.01 ? Material.GOLD_ORE
				: (rand < 0.03 ? Material.IRON_ORE
				: (rand < 0.08 ? Material.COAL_ORE
				: (rand < 0.16 ? Material.REDSTONE_ORE
				: (rand < 0.2 ? Material.LAPIS_ORE
				: Material.COBBLESTONE))))));
	}

	public static void main(String[] args) {
		HashMap<Material, Integer> counter = new HashMap<>();
		final long MAX = 10_000_000_000L;
		for (long i = 0; i < MAX; i++) {
			Material type = chooseType();

			counter.put(type, counter.getOrDefault(type, 0) + 1);
		}

		System.out.println("Probabilities on: " + MAX);
		counter.entrySet().forEach(s -> System.out.println(s.getKey() + " > " + ((float) s.getValue() / MAX) * 100 + "%"));
	}

}
