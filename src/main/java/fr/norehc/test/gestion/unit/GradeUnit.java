package fr.norehc.test.gestion.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public enum GradeUnit {
	JOUEUR("Joueur", 70, ""),
	RITTER("Ritter", 60, "§6§l[Ritter]§r "),
	HERZOG("Herzog", 50, "§3§l[Herzog]§r "),
	ELU("Elu", 40, "§5§l[Elu]§r "),
	BUILDER("Builder", 30, "§b§l[Builder]§r "),
	GUIDE("Guide", 20, "§a§l[Guide]§r "),
	MODERATEUR("Modo", 10, "§1§l[Modo]§r "),
	ADMINISTRATEUR("Admin", 0, "§4§l[Admin]§r ");
	
	private String name;
	private int power;
	private String prefix;
	
	private GradeUnit(String name, int power, String prefix) {
		
		this.name = name;
		this.power = power;
		this.prefix = prefix;
	}
	
	public static GradeUnit getByName(final String name) {
		return Arrays.stream(values()).filter(new Predicate<GradeUnit>() {
			public boolean test(GradeUnit g) {
				return g.getName().equalsIgnoreCase(name);
			}
		}).findAny().orElse(getLowestGrade());
	}
	
	public static GradeUnit getByPower(final int power) {
		return Arrays.stream(values()).filter(new Predicate<GradeUnit>() {
			public boolean test(GradeUnit g) {
				return g.getPower() == power;
			}
		}).findAny().orElse(getLowestGrade());
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
	
	public static GradeUnit getLowestGrade() {
		int tPower = -1;
		GradeUnit grade = null;
		for(GradeUnit g : values()) {
			if(tPower == -1 || tPower < g.getPower()) {
				tPower = g.getPower();
				grade = g;
			}
		}
		return grade;
	}
	
	public static GradeUnit getHighestGrade() {
		int tPower = -1;
		GradeUnit grade = null;
		for(GradeUnit g : values()) {
			if(tPower == -1 || tPower > g.getPower()) {
 				tPower = g.getPower();
 				grade = g;
			}
		}
		return grade;
	}

	public static GradeUnit getBeforeGrade(GradeUnit role) {
		List<Integer> powers = new ArrayList<Integer>();

		for(GradeUnit g : values()) {
			powers.add(g.getPower());
		}

		Collections.sort(powers);

		int i = powers.indexOf(role.getPower());

		return (i == powers.size() - 1) ? role : GradeUnit.getByPower(i + 1);
	}

	public static GradeUnit getAfterGrade(GradeUnit grade) {
		List<Integer> powers = new ArrayList<Integer>();

		for(GradeUnit g : values()) {
			powers.add(g.getPower());
		}

		Collections.sort(powers);

		int i = powers.indexOf(grade.getPower());

		return (i == 0) ? grade : GradeUnit.getByPower(i - 1);
	}
}
