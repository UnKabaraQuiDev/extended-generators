package lu.kbra.multi_skyblock_utils.cmds.homes;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.multi_skyblock_utils.data.PlayerData;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;

public class CmdHome implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Command syntax: " + ChatColor.GOLD + "home <name>");
			return false;
		}

		String name = args[0];

		PlayerData pd = PlayerManager.getPlayer(player);

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

		return PlayerManager.getPlayer(((Player) sender).getUniqueId()).autoCompleteHomeName(args.length < 1 ? "" : args[0]);
	}

}
