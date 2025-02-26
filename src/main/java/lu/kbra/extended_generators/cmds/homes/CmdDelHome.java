package lu.kbra.extended_generators.cmds.homes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdDelHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "delhome <name>");
			return false;
		}

		String name = args[0];

		PlayerData pd = PlayerManager.getPlayer(player);

		if (!pd.getHomes().containsKey(name)) {
			sender.sendMessage(ChatColor.RED + "No home named: " + ChatColor.GREEN + name);
			return false;
		}

		pd.getHomes().remove(name);
		sender.sendMessage(ChatColor.GOLD + "Deleted home: " + ChatColor.GREEN + name);

		return true;
	}

}
