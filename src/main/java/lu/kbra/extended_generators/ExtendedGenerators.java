package lu.kbra.extended_generators;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import lu.pcy113.pclib.pointer.prim.LongPointer;

import lu.kbra.extended_generators.cmds.admin.CmdClearConfig;
import lu.kbra.extended_generators.cmds.admin.CmdConfineHome;
import lu.kbra.extended_generators.cmds.admin.CmdConfineIsland;
import lu.kbra.extended_generators.cmds.admin.CmdReloadConfig;
import lu.kbra.extended_generators.cmds.admin.CmdSaveConfig;
import lu.kbra.extended_generators.cmds.admin.homes.CmdSuperDelHome;
import lu.kbra.extended_generators.cmds.admin.homes.CmdSuperHome;
import lu.kbra.extended_generators.cmds.admin.homes.CmdSuperHomes;
import lu.kbra.extended_generators.cmds.admin.homes.CmdSuperSetHome;
import lu.kbra.extended_generators.cmds.admin.island.CmdSuperConfigIsland;
import lu.kbra.extended_generators.cmds.homes.CmdDelHome;
import lu.kbra.extended_generators.cmds.homes.CmdHome;
import lu.kbra.extended_generators.cmds.homes.CmdHomes;
import lu.kbra.extended_generators.cmds.homes.CmdSetHome;
import lu.kbra.extended_generators.cmds.island.CmdConfigIsland;
import lu.kbra.extended_generators.cmds.island.CmdIsland;
import lu.kbra.extended_generators.cmds.misc.CmdGeneratorStats;
import lu.kbra.extended_generators.crafts.CustomCrafts;
import lu.kbra.extended_generators.db.data.PlayerManager;
import lu.kbra.extended_generators.listener.PlayerManagerListener;
import lu.kbra.extended_generators.listener.PlayerWorldInteractionListener;
import lu.kbra.extended_generators.listener.WorldWorldInteractionListener;

public class ExtendedGenerators extends JavaPlugin {

	public static ExtendedGenerators INSTANCE;

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

		WorldWorldInteractionListener.INSTANCE.printProbabilitiesStats(getLogger()::info);

		getLogger().info(this.getClass().getName() + " disabled !");
	}

	@Override
	public void onEnable() {
		try {
			((LongPointer) Class.forName(LongPointer.class.getName()).newInstance()).increment();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException("PCLib not available !", e);
		}

		PlayerManager.enable();

		registerCommand("island", new CmdIsland());
		registerCommand("configisland", new CmdConfigIsland());

		CmdHome home = new CmdHome();
		registerCommand("home", home);
		registerCommand("homes", new CmdHomes());
		registerCommand("delhome", new CmdDelHome(), home);
		registerCommand("sethome", new CmdSetHome(), home);

		registerCommand("confineis", new CmdConfineIsland());
		registerCommand("confinehome", new CmdConfineHome());

		registerCommand("reloadconfig", new CmdReloadConfig());
		registerCommand("clearconfig", new CmdClearConfig());
		registerCommand("saveconfig", new CmdSaveConfig());

		registerCommand("superconfigisland", new CmdSuperConfigIsland());

		CmdSuperHome superHome = new CmdSuperHome();
		registerCommand("superhome", superHome);
		registerCommand("superhomes", new CmdSuperHomes());
		registerCommand("superdelhome", new CmdSuperDelHome(), superHome);
		registerCommand("supersethome", new CmdSuperSetHome(), superHome);
		
		registerCommand("genstats", new CmdGeneratorStats());

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
		if (tab != null) {
			getCommand(name).setTabCompleter(tab);
		}
	}

}
