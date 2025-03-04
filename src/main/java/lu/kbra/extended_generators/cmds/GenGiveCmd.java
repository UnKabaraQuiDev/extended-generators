package lu.kbra.extended_generators.cmds;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.items.GeneratorType;
import lu.kbra.extended_generators.utils.ItemManager;

public class GenGiveCmd implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		final Player player = (Player) sender;
		
		if(args.length == 2) {
			player.getInventory().addItem(ItemManager.getItem(Integer.parseInt(args[0]), GeneratorType.valueOf(args[1].toUpperCase()), null));
		}else if(args.length == 3) {
			player.getInventory().addItem(ItemManager.getItem(Integer.parseInt(args[0]), GeneratorType.valueOf(args[1].toUpperCase()), Material.valueOf(args[2])));
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}

}
