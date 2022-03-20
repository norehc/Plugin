package fr.norehc.test.listenner.player;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.norehc.test.gestion.Account;
import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
import fr.norehc.test.main.Main;

public class PlayerChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		GradeUnit grade = GradeUnit.getLowestGrade();
		RankUnit rank = RankUnit.getLowestRank();
		
		Optional<Account> account = Main.getMain().getAccount(p);
		if(account.isPresent()) {
			grade = account.get().getDataGrade().getGrade();
			rank = account.get().getDataRank().getRank();
		}
		
		String format = "<grade-prefix><rank-prefix>§r<player>§r: <message>";
		
		format = format.replace("<player>", "%1$s"); //the player name will be automatically replaced by player.getDisplayName() you could write "%s" too but if you do it like that, you can place the message before the player's name
		format = format.replace("<grade-prefix>", grade.getPrefix()); //something like that
		format = format.replace("<rank-prefix>", rank.getPrefix());
		format = format.replace("<message>", "%2$s");
		
		e.setFormat(format);		
		
	}
}
