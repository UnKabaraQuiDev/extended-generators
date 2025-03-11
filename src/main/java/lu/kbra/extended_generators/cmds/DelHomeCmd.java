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
import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.items.GeneratorType;

public class DelHomeCmd implements CommandExecutor, TabCompleter {

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
			

			final HomeData home = pd.getHome(homeName);
			pd.removeHome(homeName).run();

			ExtendedGenerators.INSTANCE.run(() -> {
				player.sendMessage(ChatColor.GREEN + "Home deleted: " + ChatColor.GOLD + homeName);
			});
		}).catch_(Exception::printStackTrace).runAsync();

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return null;

		final Player player = (Player) sender;

		if (!PlayerManager.isCached(player)) {
			PlayerManager.getPlayer(player).thenConsume(pd -> {
				if (pd.getHomes() == null) {
					pd.loadHomes();
				}
			}).runAsync();

			return Arrays.asList("loading data...");
		}

		final PlayerData pd = PlayerManager.getPlayer(player).run();
		if (pd.getHomes() == null) {
			PlayerManager.getPlayer(player).thenConsume(pd2 -> {
				pd2.loadHomes();
			}).catch_(Exception::printStackTrace).runAsync();

			return Arrays.asList("loading homes...");
		}

		if (args.length == 1) {
			return pd.getHomes().stream().map(HomeData::getName).filter(c -> c.toLowerCase().startsWith(args[0].toLowerCase())).toList();
		}
		return null;
	}

}
