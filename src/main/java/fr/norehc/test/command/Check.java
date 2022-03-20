package fr.norehc.test.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.norehc.test.gestion.BanPlayer;

public class Check implements CommandExecutor  {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {			
		if(!sender.hasPermission("succes.check")) {
			sender.sendMessage("§cVous n'avez pas la permission !");
			return false;
		}
		
		if(args.length != 1) {
			sender.sendMessage("§c/check <joueur>");
			return false;
		}
			
		String targetName = args[0];
		
		Player p = Bukkit.getPlayer(targetName);
		
		if(p == null) {
			sender.sendMessage("§cCe joueur n'existe pas");
			return false;
		}
			
		UUID targetUUID = p.getUniqueId();
		
		BanPlayer bp = new BanPlayer(targetUUID);
			
		bp.checkDuration();
			
		sender.sendMessage("§7----------------------------------------------------");
		sender.sendMessage("§ePseudo : §b" + args[0]);
		sender.sendMessage("§eUUID : §b" + targetUUID.toString());
		sender.sendMessage("§eBanni : " + (bp.isBanned() ? "§a§l✔”" : "§c§l✖"));
		
		if(bp.isBanned()) {
			sender.sendMessage("");
			sender.sendMessage("§6Raison : §c" + bp.getReason());
			sender.sendMessage("§6Temps restant : §f" + bp.getTimeLeft());
			sender.sendMessage("");
		}
		
		sender.sendMessage("§7----------------------------------------------------");
			
		return false;
	}

}
