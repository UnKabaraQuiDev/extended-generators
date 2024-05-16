package lu.kbra.multi_skyblock_utils;

import org.bukkit.plugin.java.JavaPlugin;

import lu.kbra.multi_skyblock_utils.cmds.CmdIsland;
import lu.kbra.multi_skyblock_utils.cmds.CmdSetIsland;
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
		
		getCommand("setisland").setExecutor(new CmdSetIsland());
		
		// getCommand("clearconfig").setExecutor(new CmdClearConfig());
		
		getServer().getPluginManager().registerEvents(new PlayerManagerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerWorldInteractionListener(), this);
		getServer().getPluginManager().registerEvents(new WorldWorldInteractionListener(), this);
		
		getLogger().info(this.getClass().getName()+" enabled !");
	}

}
