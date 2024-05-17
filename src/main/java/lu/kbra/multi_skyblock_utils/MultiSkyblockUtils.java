package lu.kbra.multi_skyblock_utils;

import org.bukkit.plugin.java.JavaPlugin;

import lu.kbra.multi_skyblock_utils.cmds.CmdConfigIsland;
import lu.kbra.multi_skyblock_utils.cmds.CmdConfine;
import lu.kbra.multi_skyblock_utils.cmds.CmdIsland;
import lu.kbra.multi_skyblock_utils.cmds.CmdSetIsland;
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
		
		getLogger().info(this.getClass().getName()+" loaded !");
	}

	@Override
	public void onDisable() {
		PlayerManager.disable();
		
		getLogger().info(this.getClass().getName()+" disabled !");
	}

	@Override
	public void onEnable() {
		PlayerManager.enable();
		
		CmdIsland island = new CmdIsland();
		getCommand("island").setExecutor(island);
		getCommand("island").setTabCompleter(island);
		
		CmdConfigIsland configIsland = new CmdConfigIsland();
		getCommand("configisland").setExecutor(configIsland);
		getCommand("configisland").setTabCompleter(configIsland);
		
		CmdConfine confine = new CmdConfine();
		getCommand("confine").setExecutor(confine);
		getCommand("confine").setTabCompleter(confine);
		
		// getCommand("clearconfig").setExecutor(new CmdClearConfig());
		
		getServer().getPluginManager().registerEvents(new PlayerManagerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerWorldInteractionListener(), this);
		getServer().getPluginManager().registerEvents(new WorldWorldInteractionListener(), this);
		
		CustomCrafts.registerShapelessRecipe();
		
		getLogger().info(this.getClass().getName()+" enabled !");
	}

}
