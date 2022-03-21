package fr.norehc.test.listenner.player;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerAsyncChat implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
            String action = Main.getMain().getWaitingChatMessageAction().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
            if(action.equals("name")) {
                String newName = e.getMessage();

                ServerPlayer npc = Main.getMain().getWaitingChatMessageNPC().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));

                NPCManager.updateNameNPC(npc, newName);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);
            }else if(action.equals("skin")) {
                String newSkin = e.getMessage();

                ServerPlayer npc = Main.getMain().getWaitingChatMessageNPC().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));

                NPCManager.updateSkinNPC(npc, newSkin);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);
            }

        }
    }
}
