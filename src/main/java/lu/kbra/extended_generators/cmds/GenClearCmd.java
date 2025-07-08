package lu.kbra.extended_generators.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.GeneratorManager;

public class GenClearCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be used by players.");
			return false;
		}

		if (args.length < 2) {
			sender.sendMessage("Usage: /genclear <chunkX> <chunkZ>");
			return false;
		}

		ChunkManager.getChunk(((Player) sender).getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1])).thenConsume(ch -> {
			if (ch == null) {
				sender.sendMessage("No generators found in this chunk.");
				return;
			}

			final int count = ch.getGenerators().size();

			ch.getGenerators().forEach(gen -> {
				GeneratorManager.remove(gen);
			});

			ch.getGenerators().clear();

			sender.sendMessage("All generators in chunk (" + args[0] + ", " + args[1] + ") have been cleared (" + count + ").");
		}).catch_(Exception::printStackTrace).runAsync();
		return false;
	}

}
