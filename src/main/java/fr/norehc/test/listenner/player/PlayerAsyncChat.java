package fr.norehc.test.listenner.player;

import fr.norehc.test.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerAsyncChat implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Main.getMain().getWaitingChatMessage().indexOf(player) != -1) {

            String newName = e.getMessage();



            Main.getMain().getWaitingChatMessage().remove(player);

        }
    }
}
