package lu.kbra.extended_generators.db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import lu.kbra.extended_generators.db.data.PlayerData;
import lu.kbra.extended_generators.db.table.PlayerTable;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.async.NextTask;

public class PlayerManager {

	public static Map<Player, Integer> idCache = new HashMap<>();
	public static Map<Integer, PlayerData> playerCache = new HashMap<>();

	public static NextTask<Void, Boolean> knowsPlayer(int id) {
		return PlayerTable.INSTANCE.query(PlayerData.byId(id)).thenApply(PCUtils.single2SingleMultiMap()).thenApply(c -> !c.isEmpty());
	}

	public static NextTask<Void, PlayerData> getPlayer(int id) {
		return PlayerTable.INSTANCE.query(PlayerData.byId(id)).thenApply(PCUtils.list2FirstMultiMap()).thenParallel(pd -> playerCache.put(id, pd));
	}

	public static NextTask<Void, Boolean> knowsPlayer(String name) {
		return PlayerTable.INSTANCE.query(PlayerData.byName(name)).thenApply(PCUtils.single2SingleMultiMap()).thenApply(c -> !c.isEmpty());
	}

	public static NextTask<Void, PlayerData> getPlayer(String name) {
		return PlayerTable.INSTANCE.query(PlayerData.byName(name)).thenApply(PCUtils.list2FirstMultiMap()).thenParallel(pd -> playerCache.put(pd.getId(), pd));
	}

	public static NextTask<Void, Boolean> knowsPlayer(UUID uuid) {
		return PlayerTable.INSTANCE.query(PlayerData.byUUID(uuid)).thenApply(PCUtils.single2SingleMultiMap()).thenApply(c -> !c.isEmpty());
	}

	public static NextTask<Void, PlayerData> getPlayer(UUID uuid) {
		return PlayerTable.INSTANCE.query(PlayerData.byUUID(uuid)).thenApply(PCUtils.list2FirstMultiMap()).thenParallel(pd -> playerCache.put(pd.getId(), pd));
	}

	public static NextTask<Void, Boolean> knowsPlayer(Player player) {
		return knowsPlayer(player.getUniqueId());
	}

	public static NextTask<Void, PlayerData> getPlayer(Player player) {
		return getPlayer(player.getUniqueId()).thenParallel(pd -> idCache.put(player, pd.getId()));
	}

	public static NextTask<Void, PlayerData> update(final PlayerData gd) {
		return PlayerTable.INSTANCE.update(gd).thenApply(PCUtils.single2SingleMultiMap()).thenApply(data -> playerCache.put(data.getId(), data));
	}

	public static void clear() {
		idCache.clear();
		playerCache.clear();
	}

	public static void join(Player player) {
		knowsPlayer(player).thenConsume(b -> {
			System.out.println(b);
			if (b) {
				final PlayerData pd = getPlayer(player).run();
				if (!pd.getName().equals(player.getName())) {
					pd.setName(player.getName());
					update(pd).run();
				}
			} else {
				PlayerTable.INSTANCE.insert(new PlayerData(player)).catch_(Exception::printStackTrace).thenConsume(c -> getPlayer(player)).run();
			}
		}).runAsync();
	}

}
