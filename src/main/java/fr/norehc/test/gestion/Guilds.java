package fr.norehc.test.gestion;

import fr.norehc.test.gestion.unit.RoleUnit;
import fr.norehc.test.main.Main;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guilds {

	private Main main = Main.getMain();
	
	private List<Guild> guilds = new ArrayList<>();
	
	private List<String> guildsName = new ArrayList<>();
	
	public Guilds() {
		guildsName = getNameOfGuildFromMySQL();
		for(String guildName : guildsName) {
			guilds.add(new Guild(guildName));
		}
	}
	
	public void onLogout() {
		guilds.forEach(dg -> {
			dg.saveGuild();
		});
	}
	
	public void addGuild(String name, String prefix, Player playerLeader) {
		Map<Player, RoleUnit> playerMember = new HashMap<>();
		playerMember.put(playerLeader, RoleUnit.getHighestRole());
		
		guildsName.add(name);
		guilds.add(new Guild(name, prefix, 0, playerMember));
	}
	
	public void removeGuild(String name) {
		guilds.forEach(dg -> {
			if(dg.getName() == name) {
				guildsName.remove(name);
				if(dg.isNew()) {
					guilds.remove(dg);
				}else {
					dg.deleteGuild();
				}
			}
		});
	}
	
	public Guild getGuild(String name) {
		for(Guild guild : guilds) {
			if(guild.getName() == name && guild.stillExist()) {
				return guild;
			}
		}
		
		return null;
	}
	
	public List<Guild> getGuilds() {
		return guilds;
	}
	
	private List<String> getNameOfGuildFromMySQL() {
		List<String> data = new ArrayList<>();
		
		main.getMySQL().query(String.format("SELECT nameGuild FROM guild"), rs-> {
			try {
				while(rs.next()) {
					data.add(rs.getString("nameGuild"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return data;
	}
	
	public List<String> getGuildsName() {
		return guildsName;
	}
}
