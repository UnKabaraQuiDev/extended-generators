package lu.kbra.extended_generators.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerMiscListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.getEntity().sendMessage(ChatColor.RED + "You died at: " + ChatColor.GOLD + event.getEntity().getLocation().getBlockX() + ", " + event.getEntity().getLocation().getBlockY() + ", " + event.getEntity().getLocation().getBlockZ());
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Player " + event.getEntity().getName() + " died at: " + ChatColor.GOLD + event.getEntity().getLocation().getBlockX() + ", "
				+ event.getEntity().getLocation().getBlockY() + ", " + event.getEntity().getLocation().getBlockZ());
	}

}
