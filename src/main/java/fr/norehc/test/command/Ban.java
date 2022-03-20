package fr.norehc.test.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.norehc.test.gestion.BanPlayer;
import fr.norehc.test.gestion.unit.TimeUnit;
import fr.norehc.test.main.Main;

public class Ban implements CommandExecutor {
	
	private final Main main = Main.getMain();

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("succes.ban")) {
			sender.sendMessage("§cVous n'avez pas la permission !");
			return false;
		}
			
		if(args.length < 3) {
			helpMessage(sender);
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
			
		if(bp.isBanned()) {
			sender.sendMessage("§cCe joueur est déjà banni !");
			return false;
		}
			
		String reason = "";
			
		for(int i = 2; i < args.length; i++) {
			reason += args[i] + " ";
		}
			
		if(args[1].equalsIgnoreCase("perm")) {
			main.bm.ban(targetUUID, -1, reason);
			sender.sendMessage("§2Vous avez banni §6" + targetName + " §2de manière permanente pour §e" + reason);
			return false;
		}
			
		if(!args[1].contains(":")) {
			helpMessage(sender);
			return false;
		}
			
		int duration = 0;
			
		try {
			duration = Integer.parseInt(args[1].split(":")[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage("§cLa durée entré n'est pas un nombre");
		}
			
		if(!TimeUnit.existFromShortcut(args[1].split(":")[1])) {
			sender.sendMessage("L'unité de temps renseigné n'est pas reconnue");
			for(TimeUnit unit : TimeUnit.values()) {
				sender.sendMessage("§b" + unit.getName() + " §f: §e" + unit.getShortcut());
			}
			return false;
		}
			
		TimeUnit unit = TimeUnit.getFromShortcut(args[1].split(":")[1]);
			
		long banTime = unit.getToSecond() * duration;
		
		main.bm.ban(targetUUID, banTime, reason);
		sender.sendMessage("§2Vous avez banni §6" + targetName + " §2pendant "+ duration + " " + unit.getName() + " pour §e" + reason);
		
		return true;
	}
	
	public void helpMessage(CommandSender sender) {
		sender.sendMessage("§c/ban <joueur> perm <raison> §6§l=> §2Ban permanant du joueur");
		sender.sendMessage("§c/ban <joueur> <durée>:<unité> <raison> §6§l=> §2Ban temporaire du joueur");
	}
}
