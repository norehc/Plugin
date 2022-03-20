package fr.norehc.test.listenner.action;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakAction implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		
		player.sendMessage("Bloc cassé : " + block.getBlockData().getAsString());
		player.sendMessage("Objet drop : " + block.getDrops().toString());
		player.sendMessage(block.getMetadata("test").get(0).asString());
	}
}
