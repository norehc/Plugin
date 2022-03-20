package fr.norehc.test.gestion.unit;

import java.util.HashMap;

public enum TimeUnit {
	SECONDE("Secondes", "sec", 1),
	MINUTE("Minutes", "min", 60),
	HEURE("Heures", "h", 3600),
	JOUR("Jours", "j", 86400),
	MOIS("Mois", "m", 2592000);
	
	private String name;
	private String shortcut;
	private long toSecond;
	
	private static HashMap<String, TimeUnit> idShortcut = new HashMap<String, TimeUnit>();
	
	private TimeUnit(String name, String shortcut, long toSecond) {
		this.name = name;
		this.shortcut = shortcut;
		this.toSecond = toSecond;
	}
	
	static {
		for(TimeUnit unit : values()) {
			idShortcut.put(unit.shortcut, unit);
		}
	}
	
	public static TimeUnit getFromShortcut(String shortcut) {
		return idShortcut.get(shortcut);
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortcut() {
		return shortcut;
	}
	
	public long getToSecond() {
		return toSecond;
	}
	
	public static boolean existFromShortcut(String shorcut) {
		return idShortcut.containsKey(shorcut);
	}
}
