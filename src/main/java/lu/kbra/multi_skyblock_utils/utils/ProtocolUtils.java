package lu.kbra.multi_skyblock_utils.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

public final class ProtocolUtils {

	private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

	public static Entity send(Player player, Entity armorStand) {

		PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
		packet.getIntegers().write(0, armorStand.getEntityId());
		packet.getUUIDs().write(0, armorStand.getUniqueId());
		packet.getEntityTypeModifier().write(0, armorStand.getType()); // Correctly set the entity type
		packet.getDoubles().write(0, armorStand.getLocation().getX()).write(1, armorStand.getLocation().getY()).write(2, armorStand.getLocation().getZ());
		packet.getBytes().write(0, (byte) ((armorStand.getLocation().getYaw() * 256.0F) / 360.0F)).write(1, (byte) ((armorStand.getLocation().getPitch() * 256.0F) / 360.0F)).write(2,
				(byte) ((armorStand.getLocation().getYaw() * 256.0F) / 360.0F));

		// DataWatcher for entity metadata
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		watcher.setObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), (byte) 0x20); // Invisible
		watcher.setObject(new WrappedDataWatcherObject(2, Registry.getChatComponentSerializer()), Optional.of(WrappedChatComponent.fromText(armorStand.getCustomName()).getHandle())); // Custom name
		watcher.setObject(new WrappedDataWatcherObject(3, Registry.get(Boolean.class)), true); // Custom name visible
		watcher.setObject(new WrappedDataWatcherObject(5, Registry.get(Boolean.class)), false); // No gravity
		watcher.setObject(new WrappedDataWatcherObject(6, Registry.get(Boolean.class)), true); // Invulnerable

		packet.getDataWatcherModifier().write(0, watcher);

		try {
			protocolManager.sendServerPacket(player, packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return armorStand;
	}

}
