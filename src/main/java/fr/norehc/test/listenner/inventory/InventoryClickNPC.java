package fr.norehc.test.listenner.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;

public class InventoryClickNPC  implements Listener {

    @EventHandler
    public void onClickInventoryNPC(InventoryClickEvent e) {

        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("§4admin acces")) {
            e.setCancelled(true);
            String npcName = e.getView().getTitle().split(":")[1].split("§")[0].replace(" ", "");

            ServerPlayer npc = Main.getMain().getDataNPCs().values().stream().filter(npcs -> {
                return npcs.getName().toString().contains(npcName);
            }).findFirst().get();


            /*
            NAME_TAG -> change name
            BOOK -> change function
            BARRIER -> remove npc
             */
            if(e.getCurrentItem().getType() == Material.NAME_TAG) {
                e.getWhoClicked().closeInventory();
                player.sendMessage("§6Donner un nouveau nom au NPC");
                if(Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
                    Main.getMain().getWaitingChatMessageAction().remove(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
                    Main.getMain().getWaitingChatMessagePlayer().remove(player);
                    Main.getMain().getWaitingChatMessageNPC().remove(npc);
                }
                Main.getMain().getWaitingChatMessagePlayer().add(player);
                Main.getMain().getWaitingChatMessageNPC().add(npc);
                Main.getMain().getWaitingChatMessageAction().add("name");
            }else if(e.getCurrentItem().getType() == Material.BOOK) {

                e.getWhoClicked().closeInventory();
            }else if(e.getCurrentItem().getType() == Material.BARRIER) {
                choiseDeleteNPC(player, npcName);
            }else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD){
                e.getWhoClicked().closeInventory();
                player.sendMessage("§6Donner le nom du joueur à qui appartient le skin");
                if(Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
                    Main.getMain().getWaitingChatMessageAction().remove(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
                    Main.getMain().getWaitingChatMessagePlayer().remove(player);
                    Main.getMain().getWaitingChatMessageNPC().remove(npc);
                }
                Main.getMain().getWaitingChatMessagePlayer().add(player);
                Main.getMain().getWaitingChatMessageNPC().add(npc);
                Main.getMain().getWaitingChatMessageAction().add("skin");
            }
        }else if(e.getView().getTitle().contains("§6Confirmer la suppression du NPC")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {

                String npcName = e.getView().getTitle().split(":")[1].replace(" ", "");

                player.closeInventory();
                NPCManager.removeNPC(Main.getMain().getDataNPCs().values().stream().filter(npcs -> {
                    return npcs.getName().toString().contains(npcName);
                }).findFirst().get());
                player.sendMessage("§2Vous avez supprimé le NPC");
            }else if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
                player.sendMessage("§4Vous avez annulé la supression du NPC");
            }
        }
    }

    private void choiseDeleteNPC(Player player, String npcName) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§6Confirmer la suppression du NPC : " + npcName);
        ItemStack grayglass = GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ");
        ItemStack Gconcrete = GestionInv.newItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2CONFIRMER");
        ItemStack Rconcrete = GestionInv.newItem(Material.RED_STAINED_GLASS_PANE, 1, "§4ANNULER");

        inventory = GestionInv.createInventory(27, inventory, grayglass);

        inventory.setItem(10, Gconcrete);
        inventory.setItem(11, Gconcrete);
        inventory.setItem(12, Gconcrete);

        inventory.setItem(13, grayglass);

        inventory.setItem(14, Rconcrete);
        inventory.setItem(15, Rconcrete);
        inventory.setItem(16, Rconcrete);

        player.openInventory(inventory);
    }

}
