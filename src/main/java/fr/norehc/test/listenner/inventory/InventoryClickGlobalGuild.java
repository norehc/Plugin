package fr.norehc.test.listenner.inventory;

import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickGlobalGuild implements Listener {

    @EventHandler
    public void onClickInventoryGlobalGuild(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("§6Interface de gestion de guilde")) {
            e.setCancelled(true);

            String npcName = e.getView().getTitle().split(":")[0].split("-")[1].replace(" ", "");

            NPC npc = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
                return entry.getKey().getName().toString().equals(npcName);
            }).findFirst().get().getKey();

            if(e.getCurrentItem().getType() == Material.PLAYER_HEAD && e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Acces a l'interface admin")) {
                player.openInventory(GestionInv.adminInventory(npc, true));
            }else if(e.getCurrentItem().getType() == Material.WHITE_BANNER) {
                player.closeInventory();
                player.sendMessage("§6Donner le nom de la guilde");
                if(Main.getMain().getWaitingChatMessagePlayer().indexOf(player) != -1) {
                    Main.getMain().getWaitingChatMessageAction().remove(Main.getMain().getWaitingChatMessagePlayer().indexOf(player));
                    Main.getMain().getWaitingChatMessagePlayer().remove(player);
                    Main.getMain().getWaitingChatMessageNPC().remove(npc);
                }
                Main.getMain().getWaitingChatMessagePlayer().add(player);
                Main.getMain().getWaitingChatMessageNPC().add(Main.getMain().getDataNPCs().get(npc));
                Main.getMain().getWaitingChatMessageAction().add("guildName");
            }else if(e.getCurrentItem().getType() == Material.BARRIER) {
                choiseDeleteGuild(player, Main.getMain().getGuilds().getPlayerGuild(player).getName());
            }
        }else if(e.getView().getTitle().contains("§6Confirmer la suppression de la guild : ")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {

                String guildName = e.getView().getTitle().split(":")[1].replace(" ", "");

                player.closeInventory();

                Main.getMain().getGuilds().getGuild(guildName).deleteGuild();
                player.sendMessage("§2Vous avez supprimé la guilde");
            }else if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                player.closeInventory();
                player.sendMessage("§4Vous avez annulé la supression de la guilde");
            }
        }
    }

    private void choiseDeleteGuild(Player player, String guildName) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§6Confirmer la suppression de la guild : " + guildName);

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
}
