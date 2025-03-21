package lu.kbra.extended_generators.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
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
		if (!(sender instanceof Player))
			return false;

		final Player player = (Player) sender;

		try {
			if (args.length == 2) {
				player.getInventory().addItem(ItemManager.getItem(Integer.parseInt(args[0]), GeneratorType.valueOf(args[1].toUpperCase()), null));
			} else if (args.length == 3) {
				player.getInventory().addItem(ItemManager.getItem(Integer.parseInt(args[0]), GeneratorType.valueOf(args[1].toUpperCase()), Material.valueOf(args[2].toUpperCase())));
			}
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getMessage());
			return false;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 2) {
			return Arrays.stream(GeneratorType.values()).map(GeneratorType::name).filter(c -> c.startsWith(args[1].toUpperCase())).toList();
		} else if (args.length == 3) {
			if (Arrays.stream(GeneratorType.values()).map(GeneratorType::name).filter(args[1]::equalsIgnoreCase).count() == 0)
				return null;

			final GeneratorType type = GeneratorType.valueOf(args[1].toUpperCase());
			return type.getMaterials().stream().map(Material::name).filter(c -> c.startsWith(args[2].toUpperCase())).toList();
		}
		return null;
	}

}
