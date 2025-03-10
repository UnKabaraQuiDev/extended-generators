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

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.HomeData;
import lu.kbra.extended_generators.items.GeneratorType;

public class HomeCmd implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		if (args.length < 1) {
			return false;
		}

		final Player player = (Player) sender;

		final String homeName = args[0];

		PlayerManager.getPlayer(player).thenConsume(pd -> {
			if (pd.getHomes() == null) {
				pd.loadHomes();
			}
			
			if (!pd.hasHome(homeName)) {
				player.sendMessage(ChatColor.RED + "No home named: " + ChatColor.GOLD + homeName);
				return;
			}

			ExtendedGenerators.INSTANCE.run(() -> {
				final HomeData home = pd.getHome(homeName);
				home.loadAll();
				player.sendMessage(ChatColor.GREEN + "Transporting to: " + ChatColor.GOLD + homeName);
				player.teleport(home.getLocation());
			});
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
