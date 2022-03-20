package fr.norehc.test.scoreboard;

import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
 
/**
 * Created by Alexis on 19/02/2017.
 */
public class ScoreboardTeam {
 
    private RankUnit rank;
    private GradeUnit grade;
    
    private int power;
    private String prefix;
 
    public ScoreboardTeam(RankUnit rank, GradeUnit grade) {
        this.rank = rank;
        this.grade = grade;
        
        power = grade.getPower() + rank.getPower();
        prefix = grade.getPrefix() + rank.getPrefix();
    }
 
    public RankUnit getRank() {
    	return rank;
    }
    
    public GradeUnit getGrade() {
    	return grade;
    }
    
    public String getPrefix() {
    	return prefix;
    }
    
    public int getPower() {
    	return power;
    }
}