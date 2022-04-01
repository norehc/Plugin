package fr.norehc.test.gestion;

import fr.norehc.test.gestion.unit.RoleUnit;
import fr.norehc.test.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class Guild {
	
	private final static Main main = Main.getMain();
	
	private String name;
	private final String prefix;
	private int money;
	private final List<DataPlayerGuild> dataPlayersGuild = new ArrayList<>();
	private boolean exist = true;
	private boolean newGuild;
	
	// Uniquement pour les guildes enregistrer dans la bdd
	Guild(String name) {
		this.name = name;
		String[] data = getDataGuildFromMySQL();
		prefix = data[0];
		money = Integer.parseInt(data[1]);
		newGuild = false;
		
		for(Map.Entry<Player, RoleUnit> entry : getDataGuildPlayerFromMySQL().entrySet()) {
			addNewPlayer(entry.getKey(), entry.getValue(), false);
		}
	}
	
	// Uniquement pour les nouvelles guildes
	Guild(String name, String prefix, int money, Map<Player, RoleUnit> playerMember) {
		this.name = name;
		this.prefix = prefix;
		this.money = money;
		newGuild = true;
		
		for(Map.Entry<Player, RoleUnit> entry : playerMember.entrySet()) {
			addNewPlayer(entry.getKey(), entry.getValue(), true);
		}
	}
	
	public void addNewPlayer(Player player, RoleUnit role, boolean newPlayer) {
		DataPlayerGuild temp = new DataPlayerGuild(player.getUniqueId());
		temp.setPlayer(player, role, newPlayer);
		dataPlayersGuild.add(temp);
	}
	
	public void removePlayer(Player player) {
		dataPlayersGuild.forEach(p -> {
			if(p.getPlayer() == player) {
				if(p.getRole() == RoleUnit.getHighestRole()) {
					
				}if(!(dataPlayersGuild.size() == 1)) {
						dataPlayersGuild.get((new Random().nextInt(dataPlayersGuild.size()))).setRole(RoleUnit.getHighestRole());
				}else {
					exist = false;
				}
				dataPlayersGuild.remove(p);
			}
		});
	}
	
	public void deleteGuild() {
		exist = false;
	}
	
	public void saveGuild() {
		setDataGuildToMySQL();
		setDataGuildPlayerToMySQL();
	}
	
	private void setDataGuildToMySQL() {
		if(exist) {
			if(newGuild) {
				main.getMySQL().update(String.format("INSERT INTO guild (nameGuild, prefixGuild, moneyGuild) VALUES ('%s', '%s', '%s')", name, prefix, money));
			} else {
				main.getMySQL().update(String.format("UPDATE guild SET nameGuild='%s', prefixGuild='%s', moneyGuild='%s' WHERE nameGuild='%s'", name, prefix, money, name));
			}
		}else {
			if(!newGuild) {
				main.getMySQL().update(String.format("DELETE FROM guild WHERE nameGuild='%s'", name));
			}
		}
	}

	private void setDataGuildPlayerToMySQL() {
		dataPlayersGuild.forEach(dataPlayerGuild -> {
			if(exist) {
				if(dataPlayerGuild.isNewPlayer()) {
					main.getMySQL().update(String.format("INSERT INTO guildPlayer (nameGuild, UUID, role) VALUES ('%s', '%s', '%s')", name, dataPlayerGuild.getUUID(), dataPlayerGuild.getRole().getName()));
				}else if(dataPlayerGuild.isPresent()) {
					main.getMySQL().update(String.format("UPDATE guildPlayer SET nameGuild='%s', UUID='%s', role='%s' WHERE UUID='%s'", name, dataPlayerGuild.getUUID(), dataPlayerGuild.getRole().getName(), dataPlayerGuild.getUUID()));
				}else {
					main.getMySQL().update(String.format("DELETE FROM guildPlayer WHERE UUID='%s' AND nameGuild='%s'", dataPlayerGuild.getUUID(), name));
				}
			}else {
				if(!dataPlayerGuild.isNewPlayer()) {
					main.getMySQL().update(String.format("DELETE FROM guildPlayer WHERE UUID='%s' AND nameGuild='%s'", dataPlayerGuild.getUUID(), name));
				}
			}
		});
	}
	
	private String[] getDataGuildFromMySQL() {
		String[] data = new String[2];
		
		main.getMySQL().query(String.format("SELECT * FROM guild WHERE name='?'", name), rs -> {
			try {
				if(rs.next()) {
					data[0] = rs.getString("prefixGuild");
					data[1] = rs.getString("moneyGuild");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return data;
	}
	
	private Map<Player, RoleUnit> getDataGuildPlayerFromMySQL() {
		Map<Player, RoleUnit> data = new HashMap<>();
		List<String> dataPlayer = new ArrayList<>();
		List<String> dataRole = new ArrayList<>();
		
		main.getMySQL().query(String.format("SELECT * FROM guildPlayer WHERE nameGuild='?'", name), rs-> {
			try {
				while(rs.next()) {
					dataPlayer.add(rs.getString("UUID"));
					dataRole.add(rs.getString("role"));
				}
				if(dataPlayer.isEmpty()) newGuild = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		for(int i = 0; i < dataPlayer.size(); i++) {
			data.put(Bukkit.getOfflinePlayer(UUID.fromString(dataPlayer.get(i))).getPlayer(), RoleUnit.getByName(dataRole.get(i)));
		}
		
		return data;
	}
	
	public int getNumberOfPlayer() {
		return dataPlayersGuild.size();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setLeader(Player player) {
		dataPlayersGuild.forEach(p -> {
			if(p.getPlayer() == player) {
				p.setRole(RoleUnit.getHighestRole());
			}else if(p.getRole() == RoleUnit.getHighestRole()) {
				p.setRole(RoleUnit.getBeforeRole(RoleUnit.getHighestRole()));
			}
		});
	}
	
	public Player getLeader() {
		Optional<DataPlayerGuild> data = dataPlayersGuild.stream().filter(p -> p.getRole() == RoleUnit.getHighestRole()).findFirst();
		if(!data.isEmpty()) {
			return data.get().getPlayer();
		}

		return null;
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		for(DataPlayerGuild data : dataPlayersGuild) {
			players.add(data.getPlayer());
		}
		
		return players;
	}

	public boolean isInGuild(Player player) {
		return dataPlayersGuild.stream().filter(dataPlayerGuild -> {
			return dataPlayerGuild.getPlayer() == player;
		}).findFirst().isPresent();
	}
	
	public List<Player> getPlayersFromRole(RoleUnit role) {
		List<Player> players = new ArrayList<>();
		for(DataPlayerGuild data : dataPlayersGuild) {
			if(data.getRole() == role)
				players.add(data.getPlayer());
		}
		
		return players;
	}

	public RoleUnit getPlayerRole(Player player) {
		for(DataPlayerGuild data : dataPlayersGuild) {
			if(data.getPlayer() == player) {
				return data.getRole();
			}
		}

		return RoleUnit.getLowestRole();
	}
	
	
	public boolean stillExist() {
		return exist;
	}
	
	public boolean isNew() {
		return newGuild;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
}
