package lu.kbra.extended_generators.cmds.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdClearConfig implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ExtendedGenerators.INSTANCE.getLogger().severe("Clearing config");
		PlayerManager.clearConfig();
		return true;
	}

}
