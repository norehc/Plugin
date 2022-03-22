package fr.norehc.test.npc;

import fr.norehc.test.main.Main;

import java.sql.SQLException;
import java.util.ArrayList;
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
        Main.getMain().getDataNPCs().entrySet().forEach(entry -> {
            if(entry.getKey().exist()) {
                System.out.println("Sauvegarde du NPC " + entry.getKey().getName() + " en cours");
                System.out.println(entry.getKey().getOldName() + " " + entry.getKey().isNew());
                if(entry.getKey().isNew()) {
                    Main.getMain().getMySQL().update(String.format("INSERT INTO npcs (posX, posY, posZ, name, world, skin, skinName, signature, `function`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s' )",
                            entry.getKey().getPosX(),
                            entry.getKey().getPosY(),
                            entry.getKey().getPosZ(),
                            entry.getKey().getName(),
                            entry.getKey().getWorld(),
                            entry.getKey().getSkin(),
                            entry.getKey().getSkinName(),
                            entry.getKey().getSignatures(),
                            entry.getKey().getFunction()));
                } else {
                    Main.getMain().getMySQL().update(String.format("UPDATE npcs SET posX='%s', posY='%s', posZ='%s', name='%s', world='%s', skin='%s', skinName='%s', signature='%s', `function`='%s' WHERE name='%s'",
                            entry.getKey().getPosX(),  //posX
                            entry.getKey().getPosY(), //posY
                            entry.getKey().getPosZ(), //posZ
                            entry.getKey().getName(), //name
                            entry.getKey().getWorld(), //world
                            entry.getKey().getSkin(), //skin
                            entry.getKey().getSkinName(), //skinName
                            entry.getKey().getSignatures(), //signature
                            entry.getKey().getFunction(), //function
                            entry.getKey().getOldName())); //name
                }
            }else {
                System.out.println("here");
                Main.getMain().getMySQL().update(String.format("DELETE FROM npcs WHERE name='%s'", entry.getKey().getOldName()));
            }
        });

        Main.getMain().getDataNPCs().clear();
    }

}
