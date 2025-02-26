package lu.kbra.extended_generators.cmds.admin.homes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdSuperDelHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "delhome <player> <name>");
			return false;
		}

		String playerName = args[0];
		String name = args[1];
		
		if(!PlayerManager.knowsPlayer(playerName)) {
			sender.sendMessage(ChatColor.RED + "Unknown player: " + ChatColor.GOLD + playerName);
			return false;
		}

		PlayerData pd = PlayerManager.getPlayer(playerName);

		if (!pd.getHomes().containsKey(name)) {
			sender.sendMessage(ChatColor.RED + "No home named: " + ChatColor.GREEN + name);
			return false;
		}

		pd.getHomes().remove(name);
		sender.sendMessage(ChatColor.GOLD + "Deleted home: " + ChatColor.GREEN + name);

		return true;
	}

}
