package lu.kbra.extended_generators.cmds.homes;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdHomes implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "homes list");
			return false;
		}

		if (args[0].equalsIgnoreCase("list")) {
			PlayerData pd = PlayerManager.getPlayer(player);

			sender.sendMessage(ChatColor.GOLD + "Your homes:");

			if (pd.getHomes().size() > 0) {
				pd.getHomes().keySet().forEach(s -> {
					sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + s);
				});
			} else {
				sender.sendMessage(ChatColor.RED + "You have no homes :'(");
				sender.sendMessage(ChatColor.GREEN + "/sethome <name>" + ChatColor.GOLD + ", to set a new " + ChatColor.GREEN + "home ");
			}

		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length <= 1) {
			return Arrays.asList("list");
		}
		return null;
	}

}
