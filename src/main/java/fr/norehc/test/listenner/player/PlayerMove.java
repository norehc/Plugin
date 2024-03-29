package fr.norehc.test.listenner.player;

import fr.norehc.test.main.Main;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Main.getMain().getNPC().stream().forEach(npc -> {

            ServerGamePacketListenerImpl connection = ((CraftPlayer) e.getPlayer()).getHandle().connection;

            Location location = npc.getBukkitEntity().getLocation();
            location.setDirection(e.getPlayer().getLocation().subtract(location).toVector());

            float yaw = location.getYaw();
            float pitch = location.getPitch();

            connection.send(new ClientboundRotateHeadPacket(npc , (byte) ((yaw%360)*256/360)));
            connection.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte) ((yaw%360)*256/360), (byte) ((pitch%360)*256/360), false));
        });
    }
}
