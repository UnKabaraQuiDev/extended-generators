package lu.kbra.extended_generators.db;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.async.NextTask;

import lu.kbra.extended_generators.db.data.GeneratorData;
import lu.kbra.extended_generators.db.table.GeneratorTable;

public class GeneratorManager {

	public static Map<Location, GeneratorData> generatorCache = new HashMap<>();

	public static NextTask<Void, GeneratorData> getGenerator(final Location loc) {
		return ChunkManager.getChunkId(loc.getChunk()).thenCompose(NextTask.<Integer, GeneratorData>withArg((chunkId) -> {
			final GeneratorData data = GeneratorTable.INSTANCE.query(GeneratorData.byLocation(chunkId, loc)).thenApply(PCUtils.list2FirstMultiMap(() -> null)).run();
			if (data == null) {
				return null;
			}

			data.loadBukkit();
			generatorCache.put(loc, data);

			return data;
		}));
	}

	public static NextTask<Void, GeneratorData> update(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.update(gd).thenApply(PCUtils.single2SingleMultiMap()).thenApply(data -> generatorCache.put(data.getLocation(), data));
	}

	public static NextTask<Void, GeneratorData> createGenerator(final GeneratorData gd) {
		return GeneratorTable.INSTANCE.insertAndReload(gd).thenApply(PCUtils.single2SingleMultiMap()).thenApply(data -> generatorCache.put(data.getLocation(), data));
	}

}
