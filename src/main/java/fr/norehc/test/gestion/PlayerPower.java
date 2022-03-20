package fr.norehc.test.gestion;

import org.bukkit.entity.Player;

import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;

public class PlayerPower {
	
	private Player player;
	private RankUnit rank;
	private GradeUnit grade;
	private int powerRank;
	private int powerGrade;
	
	public PlayerPower(Player player, RankUnit rank, GradeUnit grade) {
		this.player = player;
		this.rank = rank;
		this.grade = grade;
		powerRank = rank.getPower();
		powerGrade = grade.getPower();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public RankUnit getRank() {
		return rank;
	}
	
	public GradeUnit getGrade() {
		return grade;
	}
	
	public int getPowerRank() {
		return powerRank;
	}
	
	public int getPowerGrade() {
		return powerGrade;
	}

}
