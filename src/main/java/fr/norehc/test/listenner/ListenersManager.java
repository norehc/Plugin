package fr.norehc.test.listenner;

import fr.norehc.test.listenner.npc.NPCClick;
import fr.norehc.test.listenner.player.PlayerMove;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.norehc.test.listenner.action.BreakAction;
import fr.norehc.test.listenner.action.PlaceAction;
import fr.norehc.test.listenner.player.PlayerChat;
import fr.norehc.test.listenner.player.PlayerJoin;
import fr.norehc.test.listenner.player.PlayerQuit;
import fr.norehc.test.main.Main;

public class ListenersManager {
	
	private Main main = Main.getMain();
	
	public void registerListeners() {		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerJoin(), main);
		pm.registerEvents(new PlayerQuit(), main);
		pm.registerEvents(new PlayerChat(), main);
		pm.registerEvents(new PlayerMove(), main);

		pm.registerEvents(new NPCClick(), main);

		pm.registerEvents(new BreakAction(), main);
		pm.registerEvents(new PlaceAction(), main);
	}

}
