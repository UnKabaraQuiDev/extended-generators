package lu.kbra.multi_skyblock_utils.cmds.island;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class CmdIsland implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		Location loc;

		if (args.length > 0) {

			if (PlayerManager.getPlayer(player).isConfined()) {
				sender.sendMessage(ChatColor.RED + "You have been confined to your island !");

				return false;
			}

			String playerName = args[0];

			if (!PlayerManager.knowsPlayer(playerName)) {
				sender.sendMessage(
						ChatColor.RED + "Player: " + ChatColor.GOLD + playerName + ChatColor.RED + ", unknown");

				return false;
			}

			PlayerData other = PlayerManager.getPlayer(args[0]);

			if (other.isBlacklisted(player)) {
				player.sendMessage(ChatColor.RED + "This player blacklisted you !");

				return false;
			}

			loc = other.getIslandLocation();
			player.sendTitle(ChatColor.GOLD + "Teleporting", ChatColor.GREEN + args[0], 10, 10, 10);

			if (other.isOnline()) {
				Player otherPlayer = other.getOnlinePlayer();
				otherPlayer.playSound(otherPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
				otherPlayer.sendMessage(
						ChatColor.GREEN + player.getDisplayName() + ChatColor.GOLD + " teleported to your island");
			}

		} else {

			loc = PlayerManager.getPlayer(player.getUniqueId()).getIslandLocation();
			player.sendTitle(ChatColor.GOLD + "Teleporting", "", 10, 10, 10);

		}

		player.teleport(loc);
		player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return PlayerManager.autoCompletePlayerName(args.length == 0 ? "" : args[0]);
	}

}
