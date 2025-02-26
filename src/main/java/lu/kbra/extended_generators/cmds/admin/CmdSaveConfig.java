package lu.kbra.extended_generators.cmds.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.data.PlayerManager;

public class CmdSaveConfig implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ExtendedGenerators.INSTANCE.saveConfig();
		sender.sendMessage(ChatColor.GOLD + "Plugin config saved !");

		PlayerManager.save();
		sender.sendMessage(ChatColor.GOLD + "Player datas saved !");
		
		return false;
	}

}
