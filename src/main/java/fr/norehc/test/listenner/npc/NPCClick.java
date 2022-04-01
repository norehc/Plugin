package fr.norehc.test.listenner.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
                player.openInventory(GestionInv.adminInventory(NPC, false));
            }
        }

        if(NPC.getFunction().equalsIgnoreCase("globalGuild")) {
            player.openInventory(GestionInv.globalGuildInventory(player, NPC));
        }
    }
}
