package fr.norehc.test.command;

import fr.norehc.test.main.Main;

public class CommandsManager {
	
	Main main = Main.getMain();
	
	public void registerCommands() {
		main.getCommand("ban").setExecutor(new Ban());
		main.getCommand("unban").setExecutor(new Unban());
		main.getCommand("check").setExecutor(new Check());
		main.getCommand("guild").setExecutor(new GuildCommand());
		main.getCommand("listGuild").setExecutor(new ListGuild());
	}
}
