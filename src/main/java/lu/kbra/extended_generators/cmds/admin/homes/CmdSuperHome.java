package lu.kbra.extended_generators.cmds.admin.homes;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdSuperHome implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "home <playerName> <name>");
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

		player.teleport(pd.getHomes().get(name));
		player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);

		sender.sendMessage(ChatColor.GOLD + "Teleported to home: " + ChatColor.GREEN + name);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return null;
		
		if (args.length <= 1) {
			return PlayerManager.autoCompletePlayerName(args.length < 1 ? "" : args[0].toLowerCase());
		}

		if (PlayerManager.knowsPlayer(args[0])) {
			return PlayerManager.getPlayer(args[0]).autoCompleteHomeName(args.length < 2 ? "" : args[1]);
		}

		return Arrays.asList("Unknown_player");
	}

}
