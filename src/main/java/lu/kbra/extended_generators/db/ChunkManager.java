package lu.kbra.extended_generators.db;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;

import lu.kbra.extended_generators.db.data.ChunkData;
import lu.kbra.extended_generators.db.table.ChunkTable;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.async.NextTask;
import lu.pcy113.pclib.db.TableHelper;

public class ChunkManager {

	public static Map<Chunk, Integer> idCache = new HashMap<>();
	public static Map<Integer, ChunkData> chunkCache = new HashMap<>();

	public static NextTask<Void, ChunkData> getOrCreateChunk(final Chunk chunk) {
		return getOrCreateChunkId(chunk).thenCompose(ChunkManager::getChunk);
	}

	public static NextTask<Void, ChunkData> getChunk(final Chunk chunk) {
		return getChunkId(chunk).thenCompose(ChunkManager::getChunk);
	}

	public static NextTask<Void, Integer> getChunkId(final Chunk chunk) {
		return NextTask.create(() -> {
			if (!idCache.containsKey(chunk)) {
				final ChunkData data = ChunkTable.INSTANCE.query(ChunkData.byChunk(chunk)).thenApply(PCUtils.list2FirstMultiMap(() -> null)).run();
				if (data == null) {
					return null;
				}

				data.loadAll();
				chunkCache.put(data.getId(), data);
				idCache.put(chunk, data.getId());

				return data.getId();
			}

			return idCache.get(chunk);
		});
	}

	public static NextTask<Void, Integer> getOrCreateChunkId(final Chunk chunk) {
		return NextTask.create(() -> {
			if (!idCache.containsKey(chunk)) {
				final ChunkData data = TableHelper.<ChunkData>insertOrLoad(ChunkTable.INSTANCE, new ChunkData(chunk), () -> ChunkData.byChunk(chunk)).run();

				data.loadAll();
				chunkCache.put(data.getId(), data);
				idCache.put(chunk, data.getId());

				return data.getId();
			}

			return idCache.get(chunk);
		});
	}

	public static NextTask<Void, ChunkData> getChunk(final int chunkId) {
		return NextTask.create(() -> {
			if (!chunkCache.containsKey(chunkId)) {
				final ChunkData data = ChunkTable.INSTANCE.query(ChunkData.byId(chunkId)).thenApply(PCUtils.list2FirstMultiMap(() -> null)).run();
				if (data == null) {
					return null;
				}

				data.loadAll();
				chunkCache.put(chunkId, data);
				idCache.put(data.getChunk(), chunkId);

				return data;
			}

			return chunkCache.get(chunkId);
		});
	}

	public static NextTask<Void, ChunkData> update(final ChunkData gd) {
		return ChunkTable.INSTANCE.update(gd).thenApply(PCUtils.single2SingleMultiMap()).thenApply(data -> chunkCache.put(gd.getId(), data));
	}

	public static void load(Chunk chunk) {
		getChunk(chunk).thenConsume(cd -> {
			cd.setGenerators(GeneratorManager.getGenerators(cd).thenParallel(d -> System.out.println("Loaded chunk (" + chunk.getX() + ", " + chunk.getZ() + ") and " + d.size() + " generators.")).run());
		}).runAsync();
	}

	public static void unload(Chunk chunk) {
		if (idCache.containsKey(chunk)) {
			chunkCache.get(idCache.get(chunk)).getGenerators().forEach(GeneratorManager::unload);
			System.out.println("Unloaded chunk (" + chunk.getX() + ", " + chunk.getZ() + ") and " + chunkCache.get(idCache.get(chunk)).getGenerators().size() + " generators.");
			
			chunkCache.remove(idCache.get(chunk));
			idCache.remove(chunk);
		}
	}

}
