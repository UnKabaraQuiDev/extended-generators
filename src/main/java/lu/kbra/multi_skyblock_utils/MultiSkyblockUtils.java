package lu.kbra.multi_skyblock_utils;

import org.bukkit.plugin.java.JavaPlugin;

import lu.kbra.multi_skyblock_utils.cmds.admin.CmdConfine;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdDelHome;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdHome;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdHomes;
import lu.kbra.multi_skyblock_utils.cmds.homes.CmdSetHome;
import lu.kbra.multi_skyblock_utils.cmds.island.CmdConfigIsland;
import lu.kbra.multi_skyblock_utils.cmds.island.CmdIsland;
import lu.kbra.multi_skyblock_utils.cmds.island.CmdSetIsland;
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

		CmdHome home = new CmdHome();
		getCommand("home").setExecutor(home);
		getCommand("home").setTabCompleter(home);

		CmdHomes homes = new CmdHomes();
		getCommand("homes").setExecutor(homes);
		getCommand("homes").setTabCompleter(homes);

		CmdDelHome delHome = new CmdDelHome();
		getCommand("delhome").setExecutor(delHome);
		getCommand("delhome").setTabCompleter(home);

		CmdSetHome setHome = new CmdSetHome();
		getCommand("sethome").setExecutor(setHome);

		getServer().getPluginManager().registerEvents(new PlayerManagerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerWorldInteractionListener(), this);
		getServer().getPluginManager().registerEvents(new WorldWorldInteractionListener(), this);

		CustomCrafts.registerShapelessRecipe();

		getLogger().info(this.getClass().getName() + " enabled !");
	}

}
