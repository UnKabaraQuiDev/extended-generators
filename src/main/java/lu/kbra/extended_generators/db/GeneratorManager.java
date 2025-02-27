package lu.kbra.extended_generators.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static List<GeneratorData> activeGenerators = new ArrayList<>();

	public static void init() {
		runnable = new BukkitRunnable() {
			long tick = 0;

			@Override
			public void run() {
				for (GeneratorData gd : activeGenerators) {
					if (gd.getTier() == 0)
						continue;
					
					if (tick % (60 / gd.calculateSpeed()) == 0) {
						gd.generate();
					}
				}

				if (tick >= 60) {
					tick = 0;
					ExtendedGenerators.INSTANCE.getLogger().info("Current active generators: " + activeGenerators.size());
				}

				tick++;
			}
		};
		runnable.runTaskTimer(ExtendedGenerators.INSTANCE, 1, 1);
	}

	public static NextTask<Void, GeneratorData> getGenerator(final Location loc) {
		return ChunkManager.getChunkId(loc.getChunk()).thenCompose(NextTask.<Integer, GeneratorData>withArg((chunkId) -> {
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
		return GeneratorTable.INSTANCE.insertAndReload(gd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> generatorCache.put(data.getLocation(), data)).thenParallel(data -> activeGenerators.add(data));
	}

	public static NextTask<Void, GeneratorData> remove(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.delete(gd).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> generatorCache.remove(data.getLocation())).thenParallel(data -> activeGenerators.remove(data));
	}

	public static NextTask<Void, List<GeneratorData>> getGenerators(final ChunkData cd) {
		return GeneratorTable.INSTANCE.query(GeneratorData.byChunk(cd)).thenApply(PCUtils.single2SingleMultiMap()).thenParallel(data -> data.forEach(gd -> {
			gd.loadAll();
			generatorCache.put(gd.getLocation(), gd);
			activeGenerators.add(gd);
		}));
	}

	public static void unload(final GeneratorData gd) {
		generatorCache.remove(gd.getLocation());
		activeGenerators.remove(gd);
	}

}
