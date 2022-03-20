package fr.norehc.test.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.norehc.test.gestion.BanPlayer;
import fr.norehc.test.main.Main;

public class Unban implements CommandExecutor  {
	
	private Main main = Main.getMain();

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("succes.unban")) {
			sender.sendMessage("§cVous n'avez pas la permission !");
			return false;
		}
			
		if(args.length != 1) {
			sender.sendMessage("§c/unban <joueur>");
			return false;
		}
			
		String targetName = args[0];
		
		Player p = null;
		
		if(Bukkit.getOfflinePlayer(targetName).hasPlayedBefore()) {
			p = Bukkit.getOfflinePlayer(targetName).getPlayer();
		}
		
		if(p == null) {
			sender.sendMessage("§cCe joueur n'existe pas");
			return false;
		}
			
		UUID targetUUID = p.getUniqueId();
		
		BanPlayer bp = new BanPlayer(targetUUID);
		
		if(!bp.isBanned()) {
			sender.sendMessage("§cCe joueur n'est pas banni !");
			return false;
		}
			
		main.bm.unban(targetUUID);
		sender.sendMessage("§2Vous avez débanni §6" + targetName);		
		
		return false;
	}
}
