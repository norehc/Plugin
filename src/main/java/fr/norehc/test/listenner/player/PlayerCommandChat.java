package fr.norehc.test.listenner.player;

import fr.norehc.test.gestion.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;

import java.net.ServerSocket;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class PlayerCommandChat implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
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
            }else if(action.equals("giveMoneyPlayer")) {
                e.setCancelled(true);
                String namePlayer = e.getMessage();

                if(!Main.getMain().getAccounts().stream().filter(a -> {
                    return a.getName().equals(namePlayer);
                }).findFirst().isPresent()) {
                    player.sendMessage("§4Le joueur n'est pas connecté, veuillez attendre sa connexion pour lui donner de l'argent !");
                    return;
                }

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessageAction().set(i, "giveMoneyPlayerNumber");

                Main.getMain().getWaitingChatMessageMoneyPlayer().remove(player);

                Main.getMain().getWaitingChatMessageMoneyPlayer().put(player, namePlayer);

                player.sendMessage("§5Veuillez renseigner la quantité voulu !");
            }else if(action.equals("giveMonePlayerNumber")) {
                e.setCancelled(true);

                int money;

                try {
                    money = Integer.parseInt(e.getMessage());
                }catch (NumberFormatException numberFormatException) {
                    player.sendMessage("Vous n'avez pas entré un nombre entier");
                    return;
                }

                Account accountGiver = Main.getMain().getAccount(player).get();

                if(!accountGiver.getDataMoney().hasBankMoney(money)) {
                    player.sendMessage("Vous n'avez pas l'argent nécessaire pour faire ce transfert !");
                    return;
                }

                String receiveNamePlayer = Main.getMain().getWaitingChatMessageMoneyPlayer().get(player);

                Optional<Account> accountOptional = Main.getMain().getAccounts().stream().filter(a -> {
                    return a.getName().equals(receiveNamePlayer);
                }).findFirst();

                if(!accountOptional.isPresent()) {
                    player.sendMessage("Le joueur s'est déconnecté entre-temps");
                }

                Account accountReceiver = accountOptional.get();

                accountGiver.getDataMoney().subBankMoney(money);
                accountReceiver.getDataMoney().addBankMoney(money);

                Main.getMain().getWaitingChatMessageMoneyPlayer().remove(player);

                int i = Main.getMain().getWaitingChatMessagePlayer().indexOf(player);

                Main.getMain().getWaitingChatMessagePlayer().remove(i);
                Main.getMain().getWaitingChatMessageNPC().remove(i);
                Main.getMain().getWaitingChatMessageAction().remove(i);

            }
        }
    }
}
