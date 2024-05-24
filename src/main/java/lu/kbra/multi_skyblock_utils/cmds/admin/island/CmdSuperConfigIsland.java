package lu.kbra.multi_skyblock_utils.cmds.admin.island;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class CmdSuperConfigIsland implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String playerName = args[0];

		if (!PlayerManager.knowsPlayer(playerName)) {
			sender.sendMessage(ChatColor.RED + "Unknown player: " + ChatColor.GOLD + playerName);
			return false;
		}

		PlayerData pd = PlayerManager.getPlayer(playerName);

		if (args.length == 2) {
			if (!(sender instanceof Player))
				return false;

			Player player = (Player) sender;
			
			if (args[1].equalsIgnoreCase("set")) {

				pd.setIslandLocation(player.getLocation());

				player.sendMessage(ChatColor.GOLD + "Set island spawn point to: " + ChatColor.GREEN + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());

				return true;
			} else if (args[1].equalsIgnoreCase("spawn")) {
				Player target = Bukkit.getPlayerExact(playerName);

				if (target != null) {
					target.setRespawnLocation(player.getLocation());
					player.sendMessage(ChatColor.GOLD + "Set respawn point to: " + ChatColor.GREEN + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GOLD + playerName + ChatColor.RED + ", not found/offline");
					return false;
				}

			} else if (args[1].equalsIgnoreCase("show")) {
				player.sendMessage("Not supported :/");

				return false;
			}
		} else if (args.length == 3) {
			if (args[1].equalsIgnoreCase("blacklist") && args[2].equalsIgnoreCase("list")) {

				sender.sendMessage(ChatColor.GOLD + "Blacklist: " + ChatColor.GREEN + String.join(", ", pd.getBlacklist().stream().map((String a) -> PlayerManager.getPlayer(UUID.fromString(a)).getName()).collect(Collectors.toList())));

				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Option " + ChatColor.GREEN + args[2] + " not found (" + ChatColor.GOLD + "add" + ChatColor.RED + ", " + ChatColor.GOLD + "remove" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
						+ ChatColor.RED + ")");

				return false;
			}
		} else if (args.length == 4) {
			if (args[1].equalsIgnoreCase("blacklist")) {
				String otherPlayer = args[3];

				if (!PlayerManager.knowsPlayer(otherPlayer)) {
					sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GOLD + otherPlayer + ChatColor.RED + ", unknown");

					return false;
				}

				if (args[2].equalsIgnoreCase("add")) {
					pd.addIslandBlacklist(otherPlayer);

					sender.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.GREEN + otherPlayer + ChatColor.GOLD + " added to blacklist !");
				} else if (args[2].equalsIgnoreCase("remove")) {
					pd.removeIslandBlacklist(otherPlayer);

					sender.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.GREEN + otherPlayer + ChatColor.GOLD + " added to blacklist !");
				} else {
					sender.sendMessage(ChatColor.RED + "Option " + ChatColor.GREEN + args[2] + " not found (" + ChatColor.GOLD + "add" + ChatColor.RED + ", " + ChatColor.GOLD + "remove" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
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
			return PlayerManager.autoCompletePlayerName(args.length < 1 ? "" : args[0].toLowerCase());
		}

		if (args.length <= 2) {
			return Arrays.asList("set", "spawn", "blacklist", "show");
		}
		if (args.length <= 3 && args[1].equalsIgnoreCase("blacklist")) {
			return Arrays.asList("add", "remove", "list");
		}
		if (args.length <= 4 && args[1].equalsIgnoreCase("blacklist") && (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove"))) {
			return PlayerManager.autoCompletePlayerName(args.length < 4 ? "" : args[3]);
		}

		return null;
	}

}
