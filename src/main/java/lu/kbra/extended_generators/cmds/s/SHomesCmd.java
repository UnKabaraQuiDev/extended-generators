package lu.kbra.extended_generators.cmds.s;

import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.PlayerManager;

public class SHomesCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		final Player player = (Player) sender;

		PlayerManager.getPlayer(player).thenConsume(pd -> {
			if (pd.getHomes() == null) {
				pd.loadHomes();
			}

			ExtendedGenerators.INSTANCE.run(() -> {
				player.sendMessage(ChatColor.GREEN + "Your homes: \n" + pd.getHomes().stream().map(c -> ChatColor.GRAY + " - " + ChatColor.GOLD + c.getName()).collect(Collectors.joining("\n")));
			});
		}).catch_(Exception::printStackTrace).runAsync();

		return true;
	}

}
