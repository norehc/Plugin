package fr.norehc.test.listenner.inventory;

import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InventoryClickNPC  implements Listener {

    @EventHandler
    public void onClickInventoryNPC(InventoryClickEvent e) {

        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("§4admin acces")) {
            e.setCancelled(true);
            String npcName = e.getView().getTitle().split(":")[1].split("§")[0].replace(" ", "");

            ServerPlayer npc = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
                return entry.getKey().getName().equals(npcName);
            }).findFirst().get().getValue();

            NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
               return entry.getKey().getName().equals(npcName);
            }).findFirst().get().getKey();

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
                choiseFunctionNPC(player, NPC);
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
            }else if(e.getCurrentItem().getType() == Material.PAPER) {
                if(NPC.getFunction().equals("globalGuild")) {
                    player.openInventory(GestionInv.globalGuildInventory(player, NPC));
                }
            }
        }else if(e.getView().getTitle().contains("§6Confirmer la suppression du NPC")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {

                String npcName = e.getView().getTitle().split(":")[1].replace(" ", "");

                player.closeInventory();
                NPCManager.removeNPC(Main.getMain().getDataNPCs().entrySet().stream().filter(npcs -> {
                    return npcs.getKey().getName().toString().equals(npcName);
                }).findFirst().get().getValue());
                player.sendMessage("§2Vous avez supprimé le NPC");
            }else if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
                player.sendMessage("§4Vous avez annulé la supression du NPC");
            }
        }else if(e.getView().getTitle().contains("§6Choisisser la nouvelle fonction du NPC")) {
            e.setCancelled(true);
            String npcName = e.getView().getTitle().split(":")[1].replace(" ", "");
            if(e.getCurrentItem().getType() == Material.WHITE_BANNER) {
                player.closeInventory();

                Main.getMain().getDataNPCs().entrySet().stream().filter(entry ->  {
                    return entry.getKey().getName().equals(npcName);
                }).findFirst().get().getKey().setFunction("globalGuild");

                player.sendMessage("§4Vous avez changé la fonctionnalité du NPC il est désormé en gestion des guildes");

            }else if(e.getCurrentItem().getType() == Material.GOLD_INGOT) {
                player.closeInventory();

                Main.getMain().getDataNPCs().entrySet().stream().filter(entry ->  {
                    return entry.getKey().getName().equals(npcName);
                }).findFirst().get().getKey().setFunction("playerBank");

                player.sendMessage("§4Vous avez changé la fonctionnalité du NPC il est désormé en gestion de l'argent des joueurs");
            }else if(e.getCurrentItem().getType() == Material.GOLD_BLOCK) {
                player.closeInventory();

                Main.getMain().getDataNPCs().entrySet().stream().filter(entry ->  {
                    return entry.getKey().getName().equals(npcName);
                }).findFirst().get().getKey().setFunction("guildBank");

                player.sendMessage("§4Vous avez changé la fonctionnalité du NPC il est désormé en gestion de l'argent d'une guilde");
            }else if(e.getCurrentItem().getType() == Material.BARRIER) {

                player.closeInventory();

                Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
                    return entry.getKey().getName().equals(npcName);
                }).findFirst().get().getKey().setFunction("none");
            }
        }
    }

    private void choiseDeleteNPC(Player player, String npcName) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§6Confirmer la suppression du NPC : " + npcName);

        ItemStack grayGlass = GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ");
        ItemStack greenGlass = GestionInv.newItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2CONFIRMER");
        ItemStack redGlass = GestionInv.newItem(Material.RED_STAINED_GLASS_PANE, 1, "§4ANNULER");

        inventory = GestionInv.createInventory(27, inventory, grayGlass);

        inventory.setItem(10, greenGlass);
        inventory.setItem(11, greenGlass);
        inventory.setItem(12, greenGlass);

        inventory.setItem(13, grayGlass);

        inventory.setItem(14, redGlass);
        inventory.setItem(15, redGlass);
        inventory.setItem(16, redGlass);

        player.openInventory(inventory);
    }

    private void choiseFunctionNPC(Player player, NPC npc) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§6Choisisser la nouvelle fonction du NPC : " + npc.getName());

        ItemStack grayGlass = GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ");

        ItemStack barrer = GestionInv.newItem(Material.BARRIER, 1, "§7Enlever la fonction du NPC", Arrays.asList("§6Enleve la fonction du NPC (setup to none)"));

        inventory = GestionInv.createInventory(27, inventory, grayGlass);

        Map<String, ItemStack> items = new HashMap<>();

        items.put("globalGuild",
                GestionInv.newItem(Material.WHITE_BANNER, 1, "§7Devenir un NPC en gestion des guildes", Arrays.asList("§6En choisissant cette fonction le NPC", "§6pourra désormer gérer les guildes", "§6en ayant les fonctions de base")));
        items.put("playerBank",
                GestionInv.newItem(Material.GOLD_INGOT, 1, "§7Devenir un NPC en gestion de l'argent des joueurs", Arrays.asList("§6En choisissant cette fonction le NPC", "§6pourra désomer gérer l'argent des joueurs")));
        items.put("guildBank",
                GestionInv.newItem(Material.GOLD_BLOCK, 1, "§7Devenir un NPC en gestion de l'argent d'une guilde", Arrays.asList("§6En choisissant cette fonction le NPC", "§6pourra désomer gérer l'argent d'une guilde")));

        int j = 10; //First place of an item
        //inventory.getSize()-18-((inventory.getSize()/9)-2)*2;
        //Place per page
        int max = (7*inventory.getSize())/9 - 22;
        for(int i = 0; i < items.size(); i++) {
            if(npc.getFunction().equals(items.keySet().toArray()[i])) {
                inventory.setItem(j, barrer);
            }else {
                inventory.setItem(j, items.get(items.keySet().toArray()[i]));
            }
        }

        player.openInventory(inventory);
    }

}
