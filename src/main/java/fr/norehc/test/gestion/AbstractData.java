package fr.norehc.test.gestion;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

abstract class AbstractData {
	
	public UUID uuid;
	
	public String getUUID() {
		return uuid.toString();
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid).getPlayer();
	}
	
	public String getName() {
		return getPlayer().getName();
	}

}
