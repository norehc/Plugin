package fr.norehc.test.listenner.npc;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import fr.norehc.test.event.RightClickEvent;
import fr.norehc.test.gestion.GestionInv;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import net.minecraft.server.level.ServerPlayer;

public class NPCClick implements Listener {

    @EventHandler
    public void onClick(RightClickEvent e) {
        Player player = e.getPlayer();
        ServerPlayer npc = e.getNPC();

        NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
            if(entry.getValue() == npc) {
                return true;
            }
            return false;
        }).findFirst().get().getKey();

        if(NPC.getFunction().equalsIgnoreCase("none") && !player.hasPermission("succes.npcAdmin")) return;

        if(player.hasPermission("succes.npcAdmin")) {
            if(NPC.getFunction().equalsIgnoreCase("none")) {
                /*
                slot 10 à 16 libre
                Change name npc
                Change function npc
                Remove npc
                 */
                Inventory inventory = Bukkit.createInventory(null, 27, "§7NPC : " + NPC.getName() + " §4admin access");
                inventory = GestionInv.createInventory(27, inventory, GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

                inventory.setItem(10, GestionInv.newItem(Material.NAME_TAG, 1, "§8Changer le nom du NPC", Arrays.asList("§7Nom actuel : " + NPC.getName())));
                inventory.setItem(12, GestionInv.newItem(GestionInv.newSkullItem(NPC.getSkinName()), "§8Changer le skin du NPC", Arrays.asList("§7Skin actuel : " + NPC.getSkinName())));
                inventory.setItem(14, GestionInv.newItem(Material.BOOK, 1, "§8Changer la fonction du NPC", Arrays.asList("§7Fonction actuel : " + NPC.getFunction())));
                inventory.setItem(16, GestionInv.newItem(Material.BARRIER, 1, "§4Supprimer le NPC"));

                player.openInventory(inventory);
            }
        }
    }
}
