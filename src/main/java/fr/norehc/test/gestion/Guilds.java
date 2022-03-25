package fr.norehc.test.gestion;

import fr.norehc.test.gestion.unit.RoleUnit;
import fr.norehc.test.main.Main;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class Guilds {

	private Main main = Main.getMain();

	private Map<String, Guild> guildsMap = new HashMap<>();
	
	public Guilds() {
		List<String> guildsName = getNameOfGuildFromMySQL();
		for(String guildName : guildsName) {
			guildsMap.put(guildName, new Guild(guildName));
		}
	}
	
	public void onLogout() {
		guildsMap.entrySet().forEach(entry -> {
			entry.getValue().saveGuild();
		});
	}
	
	public void addGuild(String name, String prefix, Player playerLeader) {
		Map<Player, RoleUnit> playerMember = new HashMap<>();
		playerMember.put(playerLeader, RoleUnit.getHighestRole());

		guildsMap.put(name, new Guild(name, prefix, 0, playerMember));
	}
	
	public void removeGuild(String name) {
		guildsMap.entrySet().forEach(entry -> {
			if(entry.getValue().getName().equals(name)) {
				if(entry.getValue().isNew()) {
					guildsMap.remove(name);
				}else {
					entry.getValue().deleteGuild();
				}
			}
		});
	}
	
	public Guild getGuild(String name) {
		for(Guild guild : guildsMap.values()) {
			if(guild.getName() == name && guild.stillExist()) {
				return guild;
			}
		}
		
		return null;
	}
	
	public List<Guild> getGuilds() {
		return new ArrayList<>(guildsMap.values());
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
		return new ArrayList<>(guildsMap.keySet());
	}

	public boolean isInGuild(Player player) {
		return guildsMap.entrySet().stream().filter(entry -> {
			return entry.getValue().isInGuild(player);
		}).findFirst().isPresent();
	}

	public Guild getPlayerGuild(Player player) {
		return guildsMap.entrySet().stream().filter(entry -> {
			return entry.getValue().isInGuild(player);
		}).findFirst().get().getValue();
	}
}
