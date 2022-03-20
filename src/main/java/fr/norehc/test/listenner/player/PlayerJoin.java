package fr.norehc.test.listenner.player;

import java.util.List;
import java.util.UUID;

import fr.norehc.test.packet.PacketReader;
import fr.norehc.test.permission.Permission;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.norehc.test.gestion.Account;
import fr.norehc.test.gestion.BanPlayer;
import fr.norehc.test.main.Main;

public class PlayerJoin implements Listener {
	
	private Main main = Main.getMain();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		Account account = new Account(p.getUniqueId());
		account.onLogin(p);
		
		main.getTablist().refresh();

		main.getNPC().stream().forEach(npc -> {
			if(main.getDataNPC().get(main.getNPC().indexOf(npc)).exist()) {
				ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;

				connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));

				connection.send(new ClientboundAddPlayerPacket(npc));
			}
		});

		PacketReader reader = new PacketReader();
		reader.inject(e.getPlayer());
		
		e.setJoinMessage(account.getDataGrade().getGrade().getPrefix() + account.getDataRank().getRank().getPrefix() + p.getName() + " §fa rejoint le serveur");
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		BanPlayer bp = new BanPlayer(uuid);
		
		
		bp.checkDuration();
		
		main.getLogger().info("Apres check Temps de ban si ban");
		
		main.getLogger().info("Est-il ban ? " + bp.isBanned());
		
		if(bp.isBanned()) {
			main.getLogger().info("Tu es banni normalement !");
			e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
			e.setKickMessage("§cVous êtes banni du serveur !\n " + "\n " + "§6Raison : §f" + bp.getReason() + "\n " + "\n " + "§aTemps restant : §f" + bp.getTimeLeft());
		}
	}
}
