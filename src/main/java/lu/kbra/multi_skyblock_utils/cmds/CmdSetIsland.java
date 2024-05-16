package lu.kbra.multi_skyblock_utils.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class CmdSetIsland implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		PlayerManager.updateIslandLocation(player);

		player.sendMessage(ChatColor.GOLD + "Set island spawn point to: " + ChatColor.GREEN + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());

		return true;
	}

}
