package fr.norehc.test.permission;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;

import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
import fr.norehc.test.main.Main;

public class PermissionManager {
    private Map<RankUnit, List<Permission>> rankPermission = new HashMap<>();
    private Map<GradeUnit, List<Permission>> gradePermission = new HashMap<>();

    public void setupPermission() {
        //Get permissions from SQL
        System.out.println("Debut de la recuperation des permissions");

        Arrays.stream(RankUnit.values()).forEach(rank -> {
            rankPermission.put(rank, new ArrayList<>());
        });

        Arrays.stream(GradeUnit.values()).forEach(grade -> {
            gradePermission.put(grade, new ArrayList<>());
        });

        Main.getMain().getMySQL().query(String.format("SELECT * FROM permissions"), rs -> {
            try {
                while(rs.next()) {
                    String role = rs.getString("role");
                    Permission perm = new Permission(rs.getString("permission"));
                    if(RankUnit.isARank(role)) {
                        rankPermission.get(RankUnit.getByName(role)).add(perm);
                    }else if(GradeUnit.isAGrade(role)) {
                        gradePermission.get(GradeUnit.getByName(role)).add(perm);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        /*System.out.println("Recuperation des permissions de chaque rank");
        for(RankUnit rankUnit : RankUnit.values()) {
            System.out.println(rankUnit.getName());
            List<Permission> permissions = new ArrayList<>();
            Main.getMain().getMySQL().query(String.format("SELECT * FROM permissions WHERE role='%s'", rankUnit.getName()), rs -> {
                System.out.println("here");
                try {
                    while(rs.next()) {
                        permissions.add(new Permission(rs.getString("permission")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            rankPermission.put(rankUnit, permissions);
        }
        System.out.println("Recuperation des permissions de chaque grade");
        for(GradeUnit gradeUnit : GradeUnit.values()) {
            List<Permission> permissions = new ArrayList<>();
            Main.getMain().getMySQL().query(String.format("SELECT * FROM permissions WHERE role='%s'", gradeUnit.getName()), rs -> {
                try {
                    while(rs.next()) {
                        permissions.add(new Permission(rs.getString("permission")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            gradePermission.put(gradeUnit, permissions);
        }*/
        System.out.println("Fin de la recuperation des permissions");
    }

    public void savePermission() {
        //Save permissions with SQL
        System.out.println("Debut de la sauvegarde des permissions");
        System.out.println("Sauvegarde des permissions de chaque rank");
        for(RankUnit rankUnit : RankUnit.values()) {
            rankPermission.get(rankUnit).stream().forEach(perm -> {
                if(perm.exist()) {
                    Main.getMain().getMySQL().update(String.format(
                            "IF NOT EXIST (SELECT * FROM permissions WHERE role='%s' AND permission ='%s'" +
                                    "BEGIN" +
                                    "INSERT INTO  permissions (role, permission) " +
                                    "VALUES ('%s', '%s')" +
                                    "END"
                            , rankUnit.getName(), perm.getPermission(), rankUnit.getName(), perm.getPermission()));
                }else {
                    Main.getMain().getMySQL().update(String.format("DELETE FROM permissions WHERE role='%s' AND permission='%s'", rankUnit.getName(), perm.getPermission()));
                }
            });
        }

        System.out.println("Sauvegarde des permissions de chaque grade");
        for(GradeUnit gradeUnit : GradeUnit.values()) {
            gradePermission.get(gradeUnit).stream().forEach(perm -> {
                if(perm.exist()) {
                    Main.getMain().getMySQL().update(String.format(
                            "IF NOT EXIST (SELECT * FROM permissions WHERE role='%s' AND permission ='%s'" +
                                    "BEGIN" +
                                    "INSERT INTO  permissions (role, permission) " +
                                    "VALUES ('%s', '%s')" +
                                    "END"
                            , gradeUnit.getName(), perm.getPermission(), gradeUnit.getName(), perm.getPermission()));
                }else {
                    Main.getMain().getMySQL().update(String.format("DELETE FROM permissions WHERE role='%s' AND permission='%s'", gradeUnit.getName(), perm.getPermission()));
                }
            });
        }
    }

    public void addPermission(Object role, String permission) {
        //Add permission to a specific role
        if(role instanceof RankUnit) {
            rankPermission.get((RankUnit) role).add(new Permission(permission));
            updatePermission(role);
        }else if(role instanceof GradeUnit) {
            gradePermission.get((GradeUnit) role).add(new Permission(permission));
            updatePermission(role);
        }
    }

    public void removePermission(Object role, String permission) {
        //Remove permission to a specific role
        if(role instanceof RankUnit) {
            rankPermission.get((RankUnit) role).stream().forEach(perm -> {
                if(perm.getPermission().equalsIgnoreCase(permission)) {
                    perm.delete();
                    updatePermission(role);
                }
            });
        }else if(role instanceof GradeUnit) {
            gradePermission.get((GradeUnit) role).stream().forEach(perm -> {
                if(perm.getPermission().equalsIgnoreCase(permission))  {
                    perm.delete();
                    updatePermission(role);
                }
            });
        }
    }

    private void updatePermission(Object role) {
        //Update permission after addPermission or removePermission
        Bukkit.getOnlinePlayers().stream().forEach(player -> {
            PermissionAttachment perms = player.addAttachment(Main.getMain());

            if(role instanceof GradeUnit) {
                if(Main.getMain().getAccount(player).get().getDataGrade().getGrade().getName().equalsIgnoreCase(((GradeUnit) role).getName()))
                    Main.getMain().getPermissionManager().getGradePermissions(Main.getMain().getAccount(player).get().getDataGrade().getGrade()).stream().forEach(perm -> {
                        if(!perm.exist()) {
                            perms.setPermission(perm.getPermission(), true);
                        }
                    });

            }else if(role instanceof  RankUnit) {
                if(Main.getMain().getAccount(player).get().getDataRank().getRank().getName().equalsIgnoreCase(((RankUnit) role).getName()))
                    Main.getMain().getPermissionManager().getRankPermissions(Main.getMain().getAccount(player).get().getDataRank().getRank()).stream().forEach(perm -> {
                        if(!perm.exist()) {
                            perms.setPermission(perm.getPermission(), true);
                        }
                    });
            }
        });
    }

    public Map<RankUnit, List<Permission>> getRanksPermissions() {
        return rankPermission;
    }

    public Map<GradeUnit, List<Permission>> getGradesPermissions() {
        return gradePermission;
    }

    public List<Permission> getRankPermissions(RankUnit rankUnit) {
        return rankPermission.get(rankUnit);
    }

    public List<Permission> getGradePermissions(GradeUnit gradeUnit) {
        return gradePermission.get(gradeUnit);
    }
}
