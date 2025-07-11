package lu.kbra.extended_generators.db;

import java.util.ArrayList;
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

	public static NextTask<Void, Boolean> knowsPlayer(UUID uuid) {
		return PlayerTable.INSTANCE.query(PlayerData.byUUID(uuid)).thenApply(PCUtils.single2SingleMultiMap()).thenApply(c -> !c.isEmpty());
	}

	public static NextTask<Void, PlayerData> getPlayer(UUID uuid) {
		return PlayerTable.INSTANCE.query(PlayerData.byUUID(uuid)).thenApply(PCUtils.list2FirstMultiMap()).thenParallel(pd -> playerCache.put(pd.getId(), pd));
	}

	public static NextTask<Void, Boolean> knowsPlayer(String name) {
		return PlayerTable.INSTANCE.query(PlayerData.byName(name)).thenApply(PCUtils.single2SingleMultiMap()).thenApply(c -> !c.isEmpty());
	}

	public static NextTask<Void, PlayerData> getPlayer(String name) {
		return PlayerTable.INSTANCE.query(PlayerData.byName(name)).thenApply(PCUtils.list2FirstMultiMap()).thenParallel(pd -> playerCache.put(pd.getId(), pd));
	}

	public static NextTask<Void, Boolean> knowsPlayer(Player player) {
		return knowsPlayer(player.getUniqueId());
	}

	public static NextTask<Void, PlayerData> getPlayer(Player player) {
		return NextTask.create(() -> isCached(player)).thenCompose(b -> b ? NextTask.create(() -> playerCache.get(idCache.get(player))) : getPlayer(player.getUniqueId()).thenParallel(pd -> idCache.put(player, pd.getId())));
	}

	public static NextTask<Void, PlayerData> update(final PlayerData gd) {
		return PlayerTable.INSTANCE.update(gd).thenApply(PCUtils.single2SingleMultiMap()).thenApply(data -> playerCache.put(data.getId(), data));
	}

	public static void clear() {
		idCache.clear();
		playerCache.clear();
	}

	public static void quit(Player player) {
		if (idCache.containsKey(player)) {
			playerCache.remove(idCache.remove(player));
		}
	}

	public static void join(Player player) {
		knowsPlayer(player).thenConsume(b -> {
			if (b) {
				final PlayerData pd = getPlayer(player).run();
				if (!pd.getName().equals(player.getName())) {
					pd.setName(player.getName());
					update(pd).run();
				}
				pd.loadHomes();
			} else {
				PlayerTable.INSTANCE.insert(new PlayerData(player)).catch_(Exception::printStackTrace).thenCompose(c -> getPlayer(player)).thenParallel(c -> c.setHomes(new ArrayList<>())).run();
			}
		}).runAsync();
	}

	public static boolean isCached(Player player) {
		return idCache.containsKey(player) && playerCache.containsKey(idCache.get(player));
	}

}
