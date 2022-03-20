package fr.norehc.test.gestion.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public enum RoleUnit {
	MEMBRE("membre", 4),
	TRESORIER("tresorier", 3),
	SECOND("second", 2),
	CHEF("chef", 1);
	
	private String name;
	private int power;
	
	private RoleUnit(String name, int power) {
		this.name = name;
		this.power = power;
	}
	
	public static RoleUnit getByName(final String name) {
		return Arrays.stream(values()).filter(new Predicate<RoleUnit>() {
			public boolean test(RoleUnit r) {
				return r.getName().equalsIgnoreCase(name);
			}
		}).findAny().orElse(getLowestRole());
	}
	
	public static RoleUnit getByPower(final int power) {
		return Arrays.stream(values()).filter(new Predicate<RoleUnit>() {
			public boolean test(RoleUnit r) {
				return r.getPower() == power;
			}
		}).findAny().orElse(getLowestRole());
	}
	
	public String getName() {
		return name;
	}
	
	public int getPower() {
		return power;
	}
	
	public static RoleUnit getLowestRole() {
		int tPower = -1;
		RoleUnit role = null;
		for(RoleUnit r : values()) {
			if(tPower == -1 || tPower < r.getPower()) {
				tPower = r.getPower();
				role = r;
			}
		}
		return role;
	}
	
	public static RoleUnit getHighestRole() {
		int tPower = -1;
		RoleUnit role = null;
		for(RoleUnit r : values()) {
			if(tPower == -1 || tPower > r.getPower()) {
 				tPower = r.getPower();
 				role = r;
			}
		}
		return role;
	}
	
	public static RoleUnit getBeforeRole(RoleUnit role) {		
		List<Integer> powers = new ArrayList<Integer>();
		
		for(RoleUnit r : values()) {
			powers.add(r.getPower());
		}
		
		Collections.sort(powers);
		
		int i = powers.indexOf(role.getPower());
		
		return (i == powers.size() - 1) ? role : RoleUnit.getByPower(i + 1);
	}
	
	public static RoleUnit getAfterRole(RoleUnit role) {
		List<Integer> powers = new ArrayList<Integer>();
		
		for(RoleUnit r : values()) {
			powers.add(r.getPower());
		}
		
		Collections.sort(powers);
		
		int i = powers.indexOf(role.getPower());
		
		return (i == 0) ? role : RoleUnit.getByPower(i - 1);
	}
}
