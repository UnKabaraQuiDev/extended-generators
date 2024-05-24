package lu.kbra.multi_skyblock_utils.cmds.admin.homes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class CmdSuperSetHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "sethome <playerName> <name>");
			return false;
		}

		String playerName = args[0];
		String name = args[1];

		if (!PlayerManager.knowsPlayer(playerName)) {
			sender.sendMessage(ChatColor.RED + "Unknown player: " + ChatColor.GOLD + playerName);
			return false;
		}

		PlayerData pd = PlayerManager.getPlayer(playerName);

		if (pd.getHomes().containsKey(name)) {
			sender.sendMessage(ChatColor.GREEN + playerName + "'s " + ChatColor.RED + "already has a home named: " + ChatColor.GREEN + name);
			return false;
		}

		pd.getHomes().put(name, player.getLocation());

		sender.sendMessage(ChatColor.GOLD + "Set home: " + ChatColor.GREEN + name);

		return false;
	}

}
