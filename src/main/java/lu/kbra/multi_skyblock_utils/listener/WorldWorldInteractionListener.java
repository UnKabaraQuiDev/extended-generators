package lu.kbra.multi_skyblock_utils.listener;

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

			block.setType(chooseType());

		}
	}

	private Material chooseType() {
		double rand = Math.random();
		return rand < 0.05 ? Material.DIAMOND_ORE : (rand < 0.1 ? Material.GOLD_ORE : (rand < 0.2 ? Material.IRON_ORE : (rand < 0.3 ? Material.COAL_ORE : Material.COBBLESTONE)));
	}

}
