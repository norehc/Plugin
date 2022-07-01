package fr.norehc.test.listenner.inventory;

import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.enums.InventoryNameEnums;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryClickPlayerBank implements Listener {

    @EventHandler
    public void onClickInventoryPlayerBank(InventoryClickEvent e) {

        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains(InventoryNameEnums.PLAYERBANK.getTitle())) {
            e.setCancelled(true);

            String npcName = e.getView().getTitle().split(":")[0].split("-")[1].replace(" ", "");

            NPC npc = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
               return entry.getKey().getName().toString().equals(npcName);
            }).findFirst().get().getKey();

            if(e.getCurrentItem().getType() == Material.PLAYER_HEAD && e.getCurrentItem().getItemMeta().getDisplayName().equals(InventoryNameEnums.ACCESADMIN.getTitle())) {
                player.openInventory(GestionInv.adminInventory(npc, true));
            }else if(e.getCurrentItem().getType() == Material.EMERALD) {
                player.openInventory(openInventoryPlayerHeadMoney(1));
                /*player.closeInventory();
                player.sendMessage("Donner le nom du joueur à qui donner de l'argent");
                if(Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
                    Main.getMain().getWaitingChatMessageAction().remove(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
                    Main.getMain().getWaitingChatMessagePlayer().remove(player);
                    Main.getMain().getWaitingChatMessageNPC().remove(npc);
                }
                Main.getMain().getWaitingChatMessagePlayer().add(player);
                Main.getMain().getWaitingChatMessageNPC().add(Main.getMain().getDataNPCs().get(npc));
                Main.getMain().getWaitingChatMessageAction().add("giveMoneyPlayer");*/
            }
        }else if(e.getView().getTitle().contains("§8Liste des joueurs")) {
            e.setCancelled(true);

            int ActualPage = Integer.parseInt(e.getView().getTitle().split("-")[1].replace(" ", ""));

            if(e.getCurrentItem().getType() == Material.PAPER) {
                int ClickedPage = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().split("-")[1].replace(" ", ""));

                player.openInventory(openInventoryPlayerHeadMoney(ClickedPage));
            }else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                String namePlayer = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e", "");

            }
        }
    }

    private Inventory openInventoryPlayerHeadMoney(int page) {
        int size = 28;
        final Inventory inventory = GestionInv.createInventory(54, Bukkit.createInventory(null, 54, "§8Liste des joueurs - " + page), GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

        AtomicInteger count = new AtomicInteger();
        Bukkit.getOnlinePlayers().stream().skip((page-1)*28).forEach(player -> {
            count.getAndIncrement();
            if(count.get() <= 28) {
                inventory.addItem(GestionInv.newSkullItem(player.getName()));
            }
        });

        inventory.setItem(47, GestionInv.newItem(Material.PAPER, 1, "Page - " + ((page == 1) ? page : page-1)));
        inventory.setItem(55, GestionInv.newItem(Material.PAPER, 1, "Page - " + ((page == (int)(Bukkit.getOnlinePlayers().size()/size) + 1) ? 1 : page+1)));

        return inventory;
    }

    private Inventory openInventoryPlayerGiveMoney(String name, int quantity) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8Quantite d'argent à donner : " + name);
        inventory = GestionInv.createInventory(27, inventory, GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));



        return inventory;
    }
}
