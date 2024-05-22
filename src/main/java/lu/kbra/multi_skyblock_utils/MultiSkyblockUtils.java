package lu.kbra.multi_skyblock_utils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import lu.kbra.multi_skyblock_utils.cmds.admin.CmdConfine;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdDelHome;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdHome;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdHomes;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdSetHome;
import lu.kbra.multi_skyblock_utils.cmds.island.CmdConfigIsland;
import lu.kbra.multi_skyblock_utils.cmds.island.CmdIsland;
import lu.kbra.multi_skyblock_utils.crafts.CustomCrafts;
import lu.kbra.multi_skyblock_utils.data.PlayerManager;
import lu.kbra.multi_skyblock_utils.listener.PlayerManagerListener;
import lu.kbra.multi_skyblock_utils.listener.PlayerWorldInteractionListener;
import lu.kbra.multi_skyblock_utils.listener.WorldWorldInteractionListener;

public class MultiSkyblockUtils extends JavaPlugin {

	public static MultiSkyblockUtils INSTANCE;

	@Override
	public void onLoad() {
		INSTANCE = this;

		getDataFolder().mkdirs();
		saveDefaultConfig();

		getLogger().info(this.getClass().getName() + " loaded !");
	}

	@Override
	public void onDisable() {
		PlayerManager.disable();

		getLogger().info(this.getClass().getName() + " disabled !");
	}

	@Override
	public void onEnable() {
		PlayerManager.enable();

		registerCommand("island", new CmdIsland());
		
		registerCommand("configisland", new CmdConfigIsland());

		registerCommand("confine", new CmdConfine());

		// getCommand("clearconfig").setExecutor(new CmdClearConfig());

		CmdHome home = new CmdHome();
		registerCommand("home", home);

		registerCommand("homes", new CmdHomes());

		registerCommand("delhome", new CmdDelHome(), home);

		registerCommand("sethome", new CmdSetHome());

		getServer().getPluginManager().registerEvents(new PlayerManagerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerWorldInteractionListener(), this);
		getServer().getPluginManager().registerEvents(new WorldWorldInteractionListener(), this);

		CustomCrafts.registerShapelessRecipes();
		CustomCrafts.registerShapedRecipes();
		CustomCrafts.registerFurnaceRecipes();

		getLogger().info(this.getClass().getName() + " enabled !");
	}

	private void registerCommand(String name, CommandExecutor exec) {
		registerCommand(name, exec, exec instanceof TabCompleter ? (TabCompleter) exec : null);
	}
	
	private void registerCommand(String name, CommandExecutor exec, TabCompleter tab) {
		getCommand(name).setExecutor(exec);
		if(tab != null) {
			getCommand(name).setTabCompleter(tab);
		}
	}

}
