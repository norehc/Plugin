package fr.norehc.test.gestion;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.norehc.test.main.Main;

public class BanManager {
	
	private Main main = Main.getMain();
	
	public void ban(UUID uuid, long endInSecond, String reason) {
		
		BanPlayer bp = new BanPlayer(uuid);
		
		if(bp.isBanned()) return;
		
		
		long end = (endInSecond == -1) ? -1 : endInSecond * 1000 + System.currentTimeMillis();
		
		main.getMySQL().update(String.format("INSERT INTO ban (`UUID`, `end`, reason, actif) VALUES ('%s', '%s', '%s', '%s')", uuid.toString(), end, reason, 1));;
		
		if(Bukkit.getPlayer(uuid) != null) {
			Player p = Bukkit.getPlayer(uuid);
			
			bp.setup();
			
			p.kickPlayer("§cVous avez été banni du serveur !\n " + "\n " + "§6Raison : §f" + bp.getReason() + "\n " + "\n " + "§aTemps restant : §f" + bp.getTimeLeft());
		}
		
	}
	
	public void unban(UUID uuid) {
		
		BanPlayer bp = new BanPlayer(uuid);
		
		if(!bp.isBanned()) return;
		
		main.getMySQL().update(String.format("UPDATE ban SET actif=0 WHERE UUID='%s'", uuid.toString()));
		
	}
}
