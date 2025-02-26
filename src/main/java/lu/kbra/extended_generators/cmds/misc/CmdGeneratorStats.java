package lu.kbra.extended_generators.cmds.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lu.kbra.extended_generators.listener.WorldWorldInteractionListener;

public class CmdGeneratorStats implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		WorldWorldInteractionListener.INSTANCE.printProbabilitiesStats(sender::sendMessage);
		
		return false;
	}

}
