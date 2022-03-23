package fr.norehc.test.listenner.inventory;

import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
                System.out.println("here");
                player.openInventory(GestionInv.adminInventory(npc, true));
            }
        }
    }
}
