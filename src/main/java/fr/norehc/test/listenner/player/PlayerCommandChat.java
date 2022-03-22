package fr.norehc.test.listenner.player;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerCommandChat implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
            String action = Main.getMain().getWaitingChatMessageAction().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
            if(action.equals("name")) {
                e.setCancelled(true);
                String newName = e.getMessage();

                ServerPlayer npc = Main.getMain().getWaitingChatMessageNPC().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));

                NPC NPC = Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc));

                final boolean[] isNew = new boolean[1];

                Main.getMain().getDataNPC().stream().forEach(npc1 -> {
                    if(npc1.getName().equals(newName)) {
                        if(npc1.exist()) {
                            player.sendMessage("§4Ce nom est déjà pris !");
                        }else {
                            isNew[0] = npc1.isNew();
                            NPCManager.deleteNPC(Main.getMain().getNPC().get(Main.getMain().getDataNPC().indexOf(npc1)));
                        }
                    }
                });

                NPCManager.updateNameNPC(npc, newName, isNew[0]);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);

                player.sendMessage("§2Vous avez changé le nom du NPC");
            }else if(action.equals("skin")) {
                e.setCancelled(true);
                String newSkin = e.getMessage();

                ServerPlayer npc = Main.getMain().getWaitingChatMessageNPC().get(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));

                NPCManager.updateSkinNPC(npc, newSkin);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);

                player.sendMessage("§2Vous avez changé le skin du NPC ");
            }

        }
    }
}
