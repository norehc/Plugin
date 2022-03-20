package fr.norehc.test.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPCManager;

public class GuildCommand implements CommandExecutor {
	
	Main main = Main.getMain();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;

		//args[0] -> name du npc
		//args[1] -> name du joueur auquel appartient la texture
		if(args.length == 0) {
			NPCManager.createNPC((Player) sender);
		}else {
			NPCManager.createNPC((Player) sender, args[0], (args.length == 2) ? args[1] : ((Player) sender).getName());
		}

		/*
		if(!sender.hasPermission("succes.guild")) {
			sender.sendMessage("§cVous n'avez pas la permission !");
			return false;
		}
		
		Player player = (Player) sender;
		
		//args[0] => money / create / delete
		
		if(args.length == 0) {
			helpMessage(sender);
			return false;
		}
		
		if(args[0] == "create") {
			if(args.length >= 2) {
				String name = args[1];
				Player playerLeader;
				
				if(main.getGuilds().getGuildsName().contains(name)) {
					player.sendMessage("§cCe nom de guilde est déjà pris");
					return false;
				}
				
				if(args.length >= 3) {
					playerLeader = Bukkit.getOfflinePlayer(args[2]).getPlayer();
				}else {
					playerLeader = player;
				}
				
				main.getGuilds().addGuild(name, name, playerLeader);
				
			}else {
				helpMessage(sender);
				return false;
			}
		}*/
		
		return false;
	}
	
	public void helpMessage(CommandSender sender) {
		sender.sendMessage("§c/guild create <nom> §6§l=> §2Création d'une nouvelle guilde");
		sender.sendMessage("§c/ban <joueur> <durée>:<unité> <raison> §6§l=> §2Ban temporaire du joueur");
	}

}
