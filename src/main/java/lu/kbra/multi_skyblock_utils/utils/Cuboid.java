package lu.kbra.multi_skyblock_utils.utils;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Cuboid {

	private World world;
	private Location loc1, loc2;

	public Cuboid(World world, Location l1, Location l2) {
		this.world = world;
		this.loc1 = l1;
		this.loc2 = l2;
	}

	public void fill(Material mat) {
		forEach((b) -> {
			b.setType(mat);
		});
	}

	public void forEach(Consumer<Block> object) {
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x < Math.max(loc1.getBlockX(), loc2.getBlockX()); x++) {
			for (int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y < Math.max(loc1.getBlockY(), loc2.getBlockY()); y++) {
				for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z < Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++) {

					object.accept(world.getBlockAt(new Location(world, x, y, z)));

				}
			}
		}
	}

	public World getWorld() {
		return world;
	}

	public Location getLoc1() {
		return loc1;
	}

	public Location getLoc2() {
		return loc2;
	}

}
