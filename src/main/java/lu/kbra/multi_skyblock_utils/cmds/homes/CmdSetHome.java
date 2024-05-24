package lu.kbra.multi_skyblock_utils.cmds.homes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class CmdSetHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "sethome <name>");
			return false;
		}

		String name = args[0];

		PlayerData pd = PlayerManager.getPlayer(player);

		if (pd.getHomes().containsKey(name)) {
			sender.sendMessage(ChatColor.RED + "You already have a home named: " + ChatColor.GREEN + name);
			return false;
		}

		pd.getHomes().put(name, player.getLocation());
		
		sender.sendMessage(ChatColor.GOLD + "Set home: " + ChatColor.GREEN + name);
		
		return false;
	}

}
