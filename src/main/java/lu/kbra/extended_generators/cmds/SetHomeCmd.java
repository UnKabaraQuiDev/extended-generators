package lu.kbra.extended_generators.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.items.GeneratorType;

public class SetHomeCmd implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		if (args.length < 1) {
			return false;
		}

		final Player player = (Player) sender;

		final String homeName = args[0];

		final Location loc = player.getLocation();

		PlayerManager.getPlayer(player).thenConsume(pd -> {
			if (pd.getHomes() == null) {
				pd.loadHomes();
			}

			if (pd.hasHome(homeName)) {
				player.sendMessage(ChatColor.RED + "Home already set: " + ChatColor.GOLD + homeName);
				return;
			}

			pd.addHome(homeName, loc).thenConsume(hd -> player.sendMessage(
					ChatColor.GREEN + "Home set: " + ChatColor.GOLD + homeName + ChatColor.GREEN + " at " + ChatColor.GOLD + " " + hd.getLocation().getBlockX() + ", " + hd.getLocation().getBlockY() + ", " + hd.getLocation().getBlockZ()))
					.run();
		}).catch_(Exception::printStackTrace).runAsync();

		return true;
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
