package lu.kbra.extended_generators;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.config.ConfigLoader;
import lu.pcy113.pclib.db.DataBaseConnector;
import lu.pcy113.pclib.pointer.prim.LongPointer;

import lu.kbra.extended_generators.crafts.CustomCrafts;
import lu.kbra.extended_generators.db.EGDataBase;
import lu.kbra.extended_generators.db.GeneratorManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.table.ChunkTable;
import lu.kbra.extended_generators.db.table.GeneratorTable;
import lu.kbra.extended_generators.db.table.PlayerTable;
import lu.kbra.extended_generators.listener.CobbleGeneratorListener;
import lu.kbra.extended_generators.listener.CustomCraftsListener;
import lu.kbra.extended_generators.listener.GeneratorsListener;
import lu.kbra.extended_generators.listener.PlayerManagerListener;

public class ExtendedGenerators extends JavaPlugin {

	public static ExtendedGenerators INSTANCE;

	private DataBaseConnector connector;

	@Override
	public void onLoad() {
		INSTANCE = this;

		getDataFolder().mkdirs();
		saveDefaultConfig();

		getLogger().info(this.getClass().getName() + " loaded !");
	}

	@Override
	public void onDisable() {
		PlayerManager.clear();

		try {
			connector.reset();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// CobbleGeneratorListener.INSTANCE.printProbabilitiesStats(getLogger()::info);

		GeneratorManager.runnable.cancel();
		
		getLogger().info(this.getClass().getName() + " disabled !");
	}

	@Override
	public void onEnable() {
		try {
			((LongPointer) Class.forName(LongPointer.class.getName()).newInstance()).increment();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			getLogger().severe("PCLib not available !");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}

		getServer().getPluginManager().registerEvents(new PlayerManagerListener(), this);
		getServer().getPluginManager().registerEvents(new GeneratorsListener(), this);
		getServer().getPluginManager().registerEvents(new CustomCraftsListener(), this);

		if (getConfig().getBoolean("generator.enabled")) {
			getServer().getPluginManager().registerEvents(new CobbleGeneratorListener(), this);
		}

		CustomCrafts.registerRecipes(getConfig().getConfigurationSection("crafts"));

		try {
			Class.forName("org.sqlite.JDBC");
			connectDB();
		} catch (IOException | SQLException | ClassNotFoundException e) {
			getLogger().severe("Couldn't connect to database:");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}

		GeneratorManager.init();

		getLogger().info(this.getClass().getName() + " enabled !");
	}

	private void connectDB() throws IOException, SQLException {
		final File connectorFile = new File(super.getDataFolder(), "db_connector.json");
		PCUtils.extractFile(ExtendedGenerators.class, "db_connector.json", connectorFile);

		connector = ConfigLoader.loadFromJSONFile(new DataBaseConnector() {
			@Override
			protected Connection createConnection() throws SQLException {
				final String url = "jdbc:sqlite://" + getDataFolder().getAbsolutePath() + "/" + host;
				return DriverManager.getConnection(url);
			}
		}, connectorFile);

		final EGDataBase db = new EGDataBase(connector);
		// db.createDB();
		db.create(new PlayerTable(db));
		db.create(new ChunkTable(db));
		db.create(new GeneratorTable(db));
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

	public void run(Runnable object) {
		new BukkitRunnable() {
			@Override
			public void run() {
				object.run();
			}
		}.runTask(this);
	}

}
