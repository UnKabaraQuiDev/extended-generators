package lu.kbra.extended_generators.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class PlayerManagerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerManager.joined(event.getPlayer());
	}
	
	private int i = 0;
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent event) {
		if(++i % Bukkit.getWorlds().size() == 0) {
			PlayerManager.save();
			ExtendedGenerators.INSTANCE.getLogger().info("Player datas saved (world save)");
		}
	}

}
