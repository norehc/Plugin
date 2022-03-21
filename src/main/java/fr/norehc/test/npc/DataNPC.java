package fr.norehc.test.npc;

import fr.norehc.test.main.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataNPC {

    public static void setupNPCs() {

        System.out.println("Chargement des NPCs sauvegardes");

        List<Double> posX = new ArrayList<>();
        List<Double> posY = new ArrayList<>();
        List<Double> posZ = new ArrayList<>();
        List<String> world = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<String> skin = new ArrayList<>();
        List<String> skinName = new ArrayList<>();
        List<String> signature = new ArrayList<>();
        List<String> function = new ArrayList<>();

        Main.getMain().getMySQL().query(String.format("SELECT * FROM npcs"), rs -> {
            try {
                while (rs.next()) {
                    posX.add(rs.getDouble("posX"));
                    posY.add(rs.getDouble("posY"));
                    posZ.add(rs.getDouble("posZ"));
                    name.add(rs.getString("name"));
                    world.add(rs.getString("world"));
                    skin.add(rs.getString("skin"));
                    skinName.add(rs.getString("skinName"));
                    signature.add(rs.getString("signature"));
                    function.add(rs.getString("function"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if(posX.isEmpty()) return;

        for(int i = 0; i < posX.size(); i++) {
            NPCManager.createNPC(name.get(i), world.get(i), posX.get(i), posY.get(i), posZ.get(i), skin.get(i), skinName.get(i), signature.get(i), false);
        }

        System.out.println("Fin du chargement des NPCs");
    }

    public static void saveNPCs() {
        System.out.println("Sauvegarde des NPCs");
        Main.getMain().getDataNPC().stream().forEach(npc -> {
            if(npc.exist()) {
                System.out.println("Sauvegarde du NPC " + npc.getName() + " en cours");
                if(npc.isNew()) {
                    Main.getMain().getMySQL().update(String.format("INSERT INTO npcs (posX, posY, posZ, name, world, skin, skinName, signature, function) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s' )", npc.getPosX(), npc.getPosY(), npc.getPosZ(), npc.getName(), npc.getWorld(), npc.getSkin(), npc.getSkinName(), npc.getSignatures(), npc.getFunction()));
                } else {
                    Main.getMain().getMySQL().update(String.format("UPDATE npcs SET posX='%s', posY='%s', posZ='%s', name='%s', world='%s', skin='%s', skinName='%s' signature='%s', function='%s' WHERE name='%s'", npc.getPosX(), npc.getPosY(), npc.getPosZ(), npc.getName(), npc.getWorld(), npc.getSkin(), npc.getSkinName(), npc.getSignatures(), npc.getOldName(), npc.getFunction()));
                }
            }else {
                Main.getMain().getMySQL().update(String.format("DELETE FROM npcs WHERE name='%s'", npc.getOldName()));
            }

        });
    }

}
