package fr.norehc.test.packet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.norehc.test.event.RightClickEvent;
import fr.norehc.test.main.Main;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;

public class PacketReader {

    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();
    private int count = 0;

    public void inject(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().connection.connection.channel;
        channels.put(player.getUniqueId(), channel);

        if(channel.pipeline().get("PacketInjector") != null)
            return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<ServerboundInteractPacket>() {
            @Override
            protected void decode(ChannelHandlerContext channel, ServerboundInteractPacket packet, List<Object> arg) throws Exception {
                arg.add(packet);
                readPacket(player, packet);
            }
        });
     }

     public void unInject(Player player) {
        channel = channels.get(player.getUniqueId());
        if(channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
     }

     /*
     entityId -> a
     action -> b
     usingSecondaryAction -> c
     ATTACK_ACTION -> d
      */

     public void readPacket(Player player, Packet<?> packet) {
        count++;
        if(count == 4) {
            count = 0;

            int id = (int) getValue(packet, "a");

            for(ServerPlayer npc : Main.getMain().getNPC()) {
                if(npc.getId() == id) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(new RightClickEvent(player, npc));
                        }
                    }, 0);
                }
            }
        }
     }

     private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);

            result = field.get(instance);

            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
     }
}
