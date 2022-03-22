package fr.norehc.test.listenner.player;

import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.norehc.test.gestion.Account;
import fr.norehc.test.main.Main;
import fr.norehc.test.packet.PacketReader;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		Main.getMain().getTablist().refresh();

		Main.getMain().getNPC().stream().forEach(npc -> {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;

			connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
		});

		PacketReader reader = new PacketReader();
		reader.unInject(e.getPlayer());
		
		Main.getMain().getAccount(p).ifPresent(Account::onLogout);
	}
}
