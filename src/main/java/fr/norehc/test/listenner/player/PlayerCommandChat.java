package fr.norehc.test.listenner.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;

import java.net.ServerSocket;

@SuppressWarnings("deprecation")
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

                NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
                    if(entry.getValue() == npc) {
                        return true;
                    }
                    return false;
                }).findFirst().get().getKey();

                final boolean[] exist = {false};
                final boolean[] isNew = {true};

                Main.getMain().getDataNPCs().entrySet().forEach(entry -> {
                        if(entry.getKey().getName().equals(newName)) {
                            if(entry.getKey().exist()) {
                                player.sendMessage("§4Ce nom est déjà pris !");
                                exist[0] = true;
                            }else {
                                isNew[0] = entry.getKey().isNew();
                                NPCManager.deleteNPC(entry.getValue());
                            }
                        }
                });

                if(exist[0] == false) {
                    NPCManager.updateNameNPC(npc, newName, isNew[0] && NPC.isNew());
                    player.sendMessage("§2Vous avez changé le nom du NPC");
                }else {
                    player.sendMessage("§4Vous n'avez pas changé le nom du NPC");
                }

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);

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
            }else if(action.equals("guildName")) {
                e.setCancelled(true);
                String guildName = e.getMessage();

                Main.getMain().getGuilds().addGuild(guildName, guildName, player);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);

                player.sendMessage("Vous avez choisi comme nom de guild : " + guildName);
            }

        }
    }
}
