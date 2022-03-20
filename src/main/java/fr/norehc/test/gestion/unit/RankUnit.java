package fr.norehc.test.gestion.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public enum RankUnit {
	O("0", 5, "§70§r "),
	I("i", 4, "§6I§r "),
	II("ii", 3, "§dII§r "),
	III("iii", 2, "§2III§r "),
	IV("iv", 1, "§1V§r "),
	V("v", 0, "§1V§r ");
	
	private String name;
	private int power;
	private String prefix;
	
	private RankUnit(String name, int power, String prefix) {
		this.name = name;
		this.power = power;
		this.prefix = prefix;
	}
	
	public static RankUnit getByName(final String name) {
		return Arrays.stream(values()).filter(new Predicate<RankUnit>() {
			public boolean test(RankUnit r) {
				return r.getName().equalsIgnoreCase(name);
			}
		}).findAny().orElse(getLowestRank());
	}
	
	public static RankUnit getByPower(final int power) {
		return Arrays.stream(values()).filter(new Predicate<RankUnit>() {
			public boolean test(RankUnit r) {
				return r.getPower() == power;
			}
		}).findAny().orElse(getLowestRank());
	}
	
	public String getName() {
		return name;
	}
	
	public int getPower() {
		return power;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public static RankUnit getLowestRank() {
		int tPower = -1;
		RankUnit rank = null;
		for(RankUnit r : values()) {
			if(tPower == -1 || tPower < r.getPower()) {
				tPower = r.getPower();
				rank = r;
			}
		}
		return rank;
	}
	
	public static RankUnit getHighestRank() {
		int tPower = -1;
		RankUnit rank = null;
		for(RankUnit r : values()) {
			if(tPower == -1 || tPower > r.getPower()) {
				tPower = r.getPower();
				rank = r;
			}
		}
		return rank;
	}

	public static RankUnit getBeforeRank(RankUnit role) {
		List<Integer> powers = new ArrayList<Integer>();

		for(RankUnit r : values()) {
			powers.add(r.getPower());
		}

		Collections.sort(powers);

		int i = powers.indexOf(role.getPower());

		return (i == powers.size() - 1) ? role : RankUnit.getByPower(i + 1);
	}

	public static RankUnit getAfterGrade(RankUnit rank) {
		List<Integer> powers = new ArrayList<Integer>();

		for(RankUnit r : values()) {
			powers.add(r.getPower());
		}

		Collections.sort(powers);

		int i = powers.indexOf(rank.getPower());

		return (i == 0) ? rank : RankUnit.getByPower(i - 1);
	}
}
