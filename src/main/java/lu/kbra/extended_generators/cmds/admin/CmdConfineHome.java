package lu.kbra.extended_generators.cmds.admin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdConfineHome implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Missing player name !");
			return false;
		}

		String playerName = args[0];

		if (PlayerManager.knowsPlayer(playerName)) {
			PlayerManager.getPlayer(playerName).setConfinedHome(!PlayerManager.getPlayer(playerName).isConfinedHome());

			sender.sendMessage(ChatColor.GOLD + "Confined home " + ChatColor.GREEN + playerName + ChatColor.GOLD + ": " + ChatColor.RED + PlayerManager.getPlayer(playerName).isConfinedHome());

			return true;
		}

		sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GOLD + playerName + ChatColor.RED + ", unknown");

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return PlayerManager.autoCompletePlayerName(args.length == 0 ? "" : args[0]);
	}

}
