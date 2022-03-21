package fr.norehc.test.gestion;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
import fr.norehc.test.main.Main;
import fr.norehc.test.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class Account extends AbstractData {
	
	private static Main main = Main.getMain();
	
	private boolean newPlayer;
	
	private DataGrade dataGrade;
	private DataRank dataRank;
	private DataMoney dataMoney;
	
	public Account(UUID uuid) {
		this.uuid = uuid;
		this.newPlayer = false;
		this.dataGrade = new DataGrade(uuid);
		this.dataRank = new DataRank(uuid);
		this.dataMoney = new DataMoney(uuid);
	}
	
	private String[] getDataFromMySQL() {
		String[] data = new String[4];
		
		main.getMySQL().query(String.format("SELECT * FROM player WHERE uuid='%s'", getUUID()), rs-> {
			try {
				if(rs.next()) {
					data[0] = rs.getString("grade");
					data[1] = rs.getString("grade_end");
					data[2] = rs.getString("rank");
					data[3] = rs.getString("money");
				} else {
					newPlayer = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		return data;
	}
	
	private void sendDataToMySQL() {
		if(newPlayer) {
			main.getMySQL().update(String.format("INSERT INTO player (name, UUID, grade, grade_end, money, rank) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", getName(), getUUID(), dataGrade.getGrade().getName(), dataGrade.getEnd(), dataMoney.getMoney(), dataRank.getRank().getName()));
		} else {
			main.getMySQL().update(String.format("UPDATE player SET name='%s', grade='%s', grade_end='%s', money='%s', rank='%s' WHERE UUID='%s'", getName(), dataGrade.getGrade().getName(), dataGrade.getEnd(), dataMoney.getMoney(), dataRank.getRank().getName(), getUUID()));
		}
	}
	
	public void onLogin(Player player) {
		main.getLogger().info("Creation de l'account du joueur " + player.getName());
		main.getAccounts().add(this);
		String[] data = getDataFromMySQL();

		if(newPlayer) {
			dataGrade.setGrade(GradeUnit.getLowestGrade());
			dataRank.setRank(RankUnit.getLowestRank());
			dataMoney.setMoney(0);
		}else {
			dataGrade.setGrade(GradeUnit.getByName(data[0]), Long.parseLong(data[1]));
			dataRank.setRank(RankUnit.getByName(data[2]));
			dataMoney.setMoney(Long.parseLong(data[3]));
		}

		List<Permission> rankPermission = main.getPermissionManager().getRankPermissions(dataRank.getRank());
		List<Permission> gradePermission = main.getPermissionManager().getGradePermissions(dataGrade.getGrade());

		PermissionAttachment perms = player.addAttachment(main);

		if(!gradePermission.isEmpty()) {
			gradePermission.stream().forEach(perm -> {
				if (!perm.exist()) {
					perms.setPermission(perm.getPermission(), true);
				}
			});
		}

		if(!rankPermission.isEmpty()) {
			rankPermission.stream().forEach(perm -> {
				if (!perm.exist()) {
					perms.setPermission(perm.getPermission(), true);
				}
			});
		}
	}
	
	public void onLogout() {
		sendDataToMySQL();
		main.getAccounts().remove(this);
	}
	
	public DataGrade getDataGrade() {
		return dataGrade;
	}
	
	public DataMoney getDataMoney() {
		return dataMoney;
	}
	
	public DataRank getDataRank() {
		return dataRank;
	}
	
}
