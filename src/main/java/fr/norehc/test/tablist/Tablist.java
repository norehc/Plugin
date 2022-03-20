package fr.norehc.test.tablist;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.norehc.test.gestion.Account;
import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
import fr.norehc.test.main.Main;
import fr.norehc.test.scoreboard.ScoreboardTeam;

public class Tablist {
	
	public Tablist() {
		
	}
	
	public void refresh() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.setPlayerListHeaderFooter(String.format("§b§lBienvenue sur notre serveur\n", ""), String.format("\n§a%s§f/§a%s §fJoueurs connectés", Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));
		}
	}
	
	public void setTab(Player player) {		
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		
		Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
		
		for(ScoreboardTeam t : Main.getMain().getTeams()) {
			Team team = scoreboard.registerNewTeam(Integer.toString(t.getPower()));
			team.setPrefix(t.getPrefix());
		}
		
		GradeUnit grade = GradeUnit.getLowestGrade();
		RankUnit rank = RankUnit.getLowestRank();
		
		Optional<Account> account = Main.getMain().getAccount(player);
		if(account.isPresent()) {
			grade = account.get().getDataGrade().getGrade();
			rank = account.get().getDataRank().getRank();
		}
		
		Team team = scoreboard.getTeam(Integer.toString(grade.getPower() + rank.getPower()));
		
		team.addEntry(player.getName());
		
		for(Team t : scoreboard.getTeams()) {
			if(t.hasEntry(player.getName()) && !(t.getName() == team.getName())) {
				t.removeEntry(player.getName());
			}
		}
		
		Objective objective = scoreboard.registerNewObjective("general", "dummy", "general");
		
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
		Score score = objective.getScore(team.getPrefix() + player.getName());
		
		score.setScore(player.getPing());
		
		player.setScoreboard(scoreboard);
		
		refresh();
		
	}

}
