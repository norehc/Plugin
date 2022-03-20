package fr.norehc.test.gestion;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.norehc.test.gestion.unit.RoleUnit;

public class DataPlayerGuild extends AbstractData {
	
	private Player player;
	private String name;
	private RoleUnit role;
	
	public DataPlayerGuild(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setPlayer(Player player, RoleUnit role) {
		this.player = player;
		name = player.getName();
		this.role = role;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getName() {
		return name;
	}
	
	public RoleUnit getRole() {
		return role;
	}
	
	public void setRole(RoleUnit role) {
		this.role = role;
	}

}
