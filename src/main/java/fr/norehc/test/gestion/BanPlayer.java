package fr.norehc.test.gestion;

import java.sql.SQLException;
import java.util.UUID;

import fr.norehc.test.gestion.unit.TimeUnit;
import fr.norehc.test.main.Main;

public class BanPlayer {
	
	private Main main = Main.getMain();
	private UUID uuid;
	private boolean banned;
	private long end;
	private String reason;
	
	public BanPlayer(UUID uuid) {
		this.uuid = uuid;
		setup();
	}
	
	public void setup() {
		main.getMySQL().query(String.format("SELECT * FROM ban WHERE UUID='%s' AND actif=1", uuid.toString()), rs -> {
			try {
				if(!rs.next()) {
					banned = false;
				} else {
					banned = true;
					end = rs.getLong("end");
					reason = rs.getString("reason");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	public boolean isBanned() {
		return banned;
	}
	
	public void checkDuration() {
		
		if(!banned) return;
		
		main.getLogger().info("Le joueur est actuellement banni");
		
		main.getLogger().info("End :" + end + " Temps actuel : " + System.currentTimeMillis() + " End plus petit que le temps actuel :" + (end < System.currentTimeMillis()));
		
		if(end == -1) return;
		
		if(end < System.currentTimeMillis()) {
			main.bm.unban(uuid);
			banned = false;
		}
	}
	
	public long getEnd() {		
		return (banned) ? end : 0;
	}
	
	public String getTimeLeft() {
		if(!banned) return "§cNon banni";
		
		main.getLogger().info("Banni");
		
		if(end == -1) return "§4§lVous avez été banni jusqu'à nouvel ordre !";
		
		long timeLeftSecond = (end - System.currentTimeMillis()) / 1000;
		
		main.getLogger().info(timeLeftSecond + "");
		
		int mois = 0;
		int jours = 0;
		int heures = 0;
		int minutes = 0;
		int secondes = 0;
		
		while(timeLeftSecond >= TimeUnit.MOIS.getToSecond()) {
			mois++;
			timeLeftSecond -= TimeUnit.MOIS.getToSecond();
		}
		
		while(timeLeftSecond >= TimeUnit.JOUR.getToSecond()) {
			jours++;
			timeLeftSecond -= TimeUnit.JOUR.getToSecond();
		}
		
		while(timeLeftSecond >= TimeUnit.HEURE.getToSecond()) {
			heures++;
			timeLeftSecond -= TimeUnit.HEURE.getToSecond();
		}
		
		while(timeLeftSecond >= TimeUnit.MINUTE.getToSecond()) {
			minutes++;
			timeLeftSecond -= TimeUnit.MINUTE.getToSecond();
		}
		
		while(timeLeftSecond >= TimeUnit.SECONDE.getToSecond()) {
			secondes++;
			timeLeftSecond -= TimeUnit.SECONDE.getToSecond();
		}
		
		main.getLogger().info("S:" + secondes + " M:" + minutes + " H:" + heures + " J:" + jours + " M:" + mois);
		
		return (mois > 0 ? mois + " " + TimeUnit.MOIS.getName() + " " : "") + 
				(jours > 0 ? jours + " " + TimeUnit.JOUR.getName() + " " : "") + 
				(heures > 0 ? heures + " " + TimeUnit.HEURE.getName() + " " : "") + 
				(minutes > 0 ? minutes + " " + TimeUnit.MINUTE.getName() + " " : "") + 
				(secondes > 0 ? secondes + " " + TimeUnit.SECONDE.getName() + " " : "");
		
	}
	
	public String getReason() {		
		return (banned) ? reason : "§cNon banni";
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public void setEnd(long end) {
		this.end = end;
	}
	
}
