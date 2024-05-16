package lu.kbra.multi_skyblock_utils.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class PlayerManagerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerManager.joined(event.getPlayer());
	}
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent event) {
		PlayerManager.save();
	}

}
