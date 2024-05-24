package lu.kbra.multi_skyblock_utils.cmds.admin.homes;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class CmdSuperHomes implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "homes <playerName> list");
			return false;
		}

		String playerName = args[0];

		if (!PlayerManager.knowsPlayer(playerName)) {
			sender.sendMessage(ChatColor.RED + "Unknown player: " + ChatColor.GOLD + playerName);
			return false;
		}

		if (args[1].equalsIgnoreCase("list")) {
			PlayerData pd = PlayerManager.getPlayer(playerName);

			sender.sendMessage(ChatColor.GREEN + playerName + "'s " + ChatColor.GOLD + "homes:");

			if (pd.getHomes().size() > 0) {
				pd.getHomes().entrySet().forEach(s -> {
					sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + s.getKey() + ChatColor.GRAY + " (" + s.getValue().getBlockX() + ", " + s.getValue().getBlockY() + ", " + s.getValue().getBlockZ() + ")");
				});
			} else {
				sender.sendMessage(ChatColor.RED + "This player doesn't have any homes :'(");
			}

		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length <= 1) {
			return PlayerManager.autoCompletePlayerName(args.length < 1 ? "" : args[0].toLowerCase());
		}

		if (PlayerManager.knowsPlayer(args[0])) {
			return Arrays.asList("list");
		}

		return null;
	}

}
