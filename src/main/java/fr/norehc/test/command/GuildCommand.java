package fr.norehc.test.command;

import fr.norehc.test.npc.NPC;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPCManager;

import java.util.Map;
import java.util.Optional;

public class GuildCommand implements CommandExecutor {
	
	Main main = Main.getMain();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;

		//args[0] -> name du npc
		//args[1] -> name du joueur auquel appartient la texture
		if(args.length == 0) {
			Optional<Map.Entry<NPC, ServerPlayer>> npc = main.getDataNPCs().entrySet().stream().filter(entry -> {
				return entry.getKey().getName().equals(((Player) sender).getName());
			}).findFirst();
			if(npc.isPresent()) {
				if(!npc.get().getKey().exist()) {
					NPCManager.deleteNPC(npc.get().getValue());
					NPCManager.createNPC((Player) sender);
				}else {
					((Player) sender).sendMessage("§4Ce nom est déjà pris !");
				}
			}else {
				NPCManager.createNPC((Player) sender);
			}
		}else {
			Optional<Map.Entry<NPC, ServerPlayer>> npc = main.getDataNPCs().entrySet().stream().filter(entry -> {
				return entry.getKey().getName().equals(((Player) sender).getName());
			}).findFirst();
			if(npc.isPresent()) {
				if(!npc.get().getKey().exist()) {
					NPCManager.deleteNPC(npc.get().getValue());
					NPCManager.createNPC((Player) sender, args[0], (args.length == 2) ? args[1] : ((Player) sender).getName());
				}else {
					((Player) sender).sendMessage("§4Ce nom est déjà pris !");
				}
			}else {
				NPCManager.createNPC((Player) sender, args[0], (args.length == 2) ? args[1] : ((Player) sender).getName());
			}
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
