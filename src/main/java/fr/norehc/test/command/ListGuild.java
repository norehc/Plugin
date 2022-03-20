package fr.norehc.test.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.norehc.test.gestion.Guild;
import fr.norehc.test.main.Main;

public class ListGuild implements CommandExecutor {
	
	Main main = Main.getMain();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		List<Guild> guilds = main.getGuilds().getGuilds();
		
		if(guilds.isEmpty()) {
			sender.sendMessage("§4Il n'y a pas de guilde enregistre !");
			return false;
		}
		
		List<String> guildsName = new ArrayList<>();
		List<String> guildsPrefix = new ArrayList<>();
		List<Integer> guildsNumberPlayer = new ArrayList<>();
		List<String> guildsLeader = new ArrayList<>();
		
		for(Guild g : guilds) {
			guildsName.add(g.getName());
			guildsPrefix.add(g.getPrefix());
			guildsNumberPlayer.add(g.getNumberOfPlayer());
			guildsLeader.add(g.getLeader().getName());
		}
		
		/*//5 guildes par pages
		int page = guilds.size() % 5 + 1;*/
		
		for(int i = 0; i < guildsName.size(); i++) {
			sender.sendMessage(guildsName.get(i) + " ; prefix : "  + guildsPrefix.get(i) + " ; Leader : " + guildsLeader.get(i) + " ; " + guildsNumberPlayer.get(i) + " membre(s)");
		}
		
		return false;
	}

}
