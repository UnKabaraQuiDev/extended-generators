package lu.kbra.multi_skyblock_utils.cmds.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lu.kbra.multi_skyblock_utils.listener.WorldWorldInteractionListener;

public class CmdGeneratorStats implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		WorldWorldInteractionListener.INSTANCE.printProbabilitiesStats(sender::sendMessage);
		
		return false;
	}

}
