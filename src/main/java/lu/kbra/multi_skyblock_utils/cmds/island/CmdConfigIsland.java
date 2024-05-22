package lu.kbra.multi_skyblock_utils.cmds.island;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class CmdConfigIsland implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("set")) {
				if (!(sender instanceof Player))
					return false;

				PlayerManager.updateIslandLocation(player);

				player.sendMessage(ChatColor.GOLD + "Set island spawn point to: " + ChatColor.GREEN + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());

				return true;
			} else if (args[0].equalsIgnoreCase("spawn")) {
				player.setRespawnLocation(player.getLocation());

				return true;
			} else if (args[0].equalsIgnoreCase("show")) {
				player.sendMessage("Not supported :/");
				
				return false;
				
				/*PlayerManager.showIslands(player);

				return true;*/
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("list")) {
				PlayerData pd = PlayerManager.getPlayer(player);

				sender.sendMessage(ChatColor.GOLD + "Blacklist: " + ChatColor.GREEN + String.join(", ", pd.getBlacklist().stream().map((String a) -> PlayerManager.getPlayer(UUID.fromString(a)).getName()).collect(Collectors.toList())));

				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Option " + ChatColor.GREEN + args[1] + " not found (" + ChatColor.GOLD + "add" + ChatColor.RED + ", " + ChatColor.GOLD + "remove" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
						+ ChatColor.RED + ")");

				return false;
			}
		} else if (args.length == 3) {
			PlayerData pd = PlayerManager.getPlayer(player);

			if (args[0].equalsIgnoreCase("blacklist")) {
				if (!PlayerManager.knowsPlayer(args[2])) {
					sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GOLD + args[2] + ChatColor.RED + ", unknown");

					return false;
				}

				if (args[1].equalsIgnoreCase("add")) {
					pd.addIslandBlacklist(args[2]);

					sender.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.GREEN + args[2] + ChatColor.GOLD + " added to blacklist !");
				} else if (args[1].equalsIgnoreCase("remove")) {
					pd.removeIslandBlacklist(args[2]);

					sender.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.GREEN + args[2] + ChatColor.GOLD + " added to blacklist !");
				} else {
					sender.sendMessage(ChatColor.RED + "Option " + ChatColor.GREEN + args[1] + " not found (" + ChatColor.GOLD + "add" + ChatColor.RED + ", " + ChatColor.GOLD + "remove" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
							+ ChatColor.RED + ")");

					return false;
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length <= 1) {
			return Arrays.asList("set", "spawn", "blacklist", "show");
		}
		if (args.length <= 2 && args[0].equalsIgnoreCase("blacklist")) {
			return Arrays.asList("add", "remove", "list");
		}
		if (args.length <= 3 && args[0].equalsIgnoreCase("blacklist") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
			return PlayerManager.autoCompletePlayerName(args.length == 3 ? "" : args[2]);
		}

		return null;
	}

}
