package lu.kbra.extended_generators.listener;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lu.kbra.extended_generators.db.ChunkManager;
import lu.kbra.extended_generators.db.PlayerManager;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.table.ChunkTable;
import lu.kbra.extended_generators.db.table.GeneratorTable;
import lu.kbra.extended_generators.db.table.PlayerTable;
import lu.kbra.extended_generators.items.GeneratorType;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.db.DataBaseTable;
import lu.pcy113.pclib.db.impl.SQLQuery.UnsafeSQLQuery;

public class PlayerManagerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerManager.join(event.getPlayer());

		PlayerTable.INSTANCE.query(new UnsafeSQLQuery<PlayerData>() {
			@Override
			public String getQuerySQL(DataBaseTable table) {
				return "select * from players;";
			}

			@Override
			public PlayerData clone() {
				return new PlayerData();
			}
		}).thenApply(PCUtils.single2SingleMultiMap()).thenConsume(System.out::println).run();

		ChunkTable.INSTANCE.query(new UnsafeSQLQuery<ChunkData>() {
			@Override
			public String getQuerySQL(DataBaseTable table) {
				return "select * from chunks;";
			}

			@Override
			public ChunkData clone() {
				return new ChunkData();
			}
		}).thenApply(PCUtils.single2SingleMultiMap()).thenConsume(System.out::println).run();

		GeneratorTable.INSTANCE.query(new UnsafeSQLQuery<GeneratorData>() {
			@Override
			public String getQuerySQL(DataBaseTable table) {
				return "select * from generators;";
			}

			@Override
			public GeneratorData clone() {
				return new GeneratorData();
			}
		}).thenApply(PCUtils.single2SingleMultiMap()).thenConsume(System.out::println).run();

		final ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(PlayerWorldInteractionListener.TITLE);
		itemMeta.setLore(Arrays.asList("Tier: 1", "Type: " + GeneratorType.ORES, "Affinity: NONE"));
		itemStack.setItemMeta(itemMeta);

		event.getPlayer().getInventory().addItem(itemStack);
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		ChunkManager.load(event.getChunk());
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		ChunkManager.unload(event.getChunk());
	}

}
