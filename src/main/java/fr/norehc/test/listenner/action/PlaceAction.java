package fr.norehc.test.listenner.action;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import fr.norehc.test.main.Main;

public class PlaceAction implements Listener {
	
	private Main main = Main.getMain();
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Block block = e.getBlock();
		
		block.setMetadata("test", new FixedMetadataValue(main, "Hola"));
	}

}
