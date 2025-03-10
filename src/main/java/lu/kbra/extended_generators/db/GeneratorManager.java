package lu.kbra.extended_generators.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.async.NextTask;

import lu.kbra.extended_generators.ExtendedGenerators;
import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.db.table.GeneratorTable;

public class GeneratorManager {

	public static BukkitRunnable runnable;

	public static Map<Location, GeneratorData> generatorCache = new HashMap<>();
	public static volatile Object lock = new Object();
	public static List<GeneratorData> activeGenerators = new CopyOnWriteArrayList<>();

	public static void init() {
		runnable = new BukkitRunnable() {
			int latest = -1;
			long tick = 0;

			@Override
			public void run() {
				synchronized (lock) {
					for (GeneratorData gd : activeGenerators) {
						if (gd.getTier() == 0 || gd.calculateSpeed() == 0) {
							ExtendedGenerators.INSTANCE.getLogger().warning("Invalid tier/speed: " + gd);
							activeGenerators.remove(gd);
							continue;
						}

						if (tick % (60 / PCUtils.clampLessOrEquals(gd.calculateSpeed(), 60)) == 0) {
							gd.generate();
						}
					}
				}

				if (tick >= 60) {
					tick = 0;
					if (latest != activeGenerators.size()) {
						ExtendedGenerators.INSTANCE.getLogger().info("Currently active generators: " + activeGenerators.size());
						latest = activeGenerators.size();
					}
				}

				tick++;
			}
		};
		runnable.runTaskTimer(ExtendedGenerators.INSTANCE, 20, 20);
	}

	public static NextTask<Void, GeneratorData> getGenerator(final Location loc) {
		return ChunkManager.getChunkId(loc.getChunk()).thenCompose(NextTask.<Integer, GeneratorData>withArg((chunkId) -> {
			if (generatorCache.containsKey(loc)) {
				return generatorCache.get(loc);
			}

			final GeneratorData data = GeneratorTable.INSTANCE.query(GeneratorData.byLocation(chunkId, loc)).thenApply(PCUtils.list2FirstMultiMap(() -> null)).run();
			if (data == null) {
				return null;
			}

			data.loadAll();
			generatorCache.put(loc, data);

			return data;
		}));
	}

	public static NextTask<Void, Boolean> hasGenerator(final Location loc) {
		return getGenerator(loc).thenApply(e -> e != null);
	}

	public static NextTask<Void, GeneratorData> update(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.update(gd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> generatorCache.put(data.getLocation(), data));
	}

	public static NextTask<Void, GeneratorData> createGenerator(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.insertAndReload(gd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> data.getChunkData().getGenerators().add(data)).thenParallel(data -> generatorCache.put(data.getLocation(), data))
				.thenParallel(data -> {
					synchronized (lock) {
						activeGenerators.add(data);
					}
				});
	}

	public static NextTask<Void, GeneratorData> remove(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.delete(gd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> generatorCache.remove(data.getLocation())).thenParallel(data -> {
			synchronized (lock) {
				activeGenerators.remove(data);
			}
		});
	}

	public static NextTask<Void, List<GeneratorData>> getGenerators(final ChunkData cd) {
		return GeneratorTable.INSTANCE.query(GeneratorData.byChunk(cd)).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> data.forEach(gd -> {
			gd.loadAll();
			generatorCache.put(gd.getLocation(), gd);
			synchronized (lock) {
				activeGenerators.add(gd);
			}
		}));
	}

	public static void unload(final GeneratorData gd) {
		generatorCache.remove(gd.getLocation());
		synchronized (lock) {
			activeGenerators.remove(gd);
		}
	}

}
