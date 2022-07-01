package fr.norehc.test.gestion;

import fr.norehc.test.enums.InventoryNameEnums;
import fr.norehc.test.gestion.unit.RoleUnit;
import fr.norehc.test.main.Main;
import fr.norehc.test.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionInv {

    //Create an inventory with item in argument around the inventory.
    public static Inventory createInventory(int numberOfSlot, Inventory inventory, ItemStack itemStack) {
        for(int i = 0; i < numberOfSlot; i++) {
            if(i < 9 || i > numberOfSlot-9) {
                inventory.setItem(i, itemStack);
            }else if(i%9 == 0) {
                inventory.setItem(i, itemStack);
                inventory.setItem(i+8, itemStack);
            }
        }
        return inventory;
    }

    public static ItemStack newItem(Material material, int quantity, String name) {
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(Material material, int quantity, String name, ArrayList<String> lore) {
        List<String> loreL = lore;
        return newItem(material, quantity, name, loreL);
    }

    public static ItemStack newItem(Material material, int quantity, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(ItemStack itemStack, String name, ArrayList<String> lore) {
        List<String> loreL = lore;
        return newItem(itemStack, name, loreL);
    }

    public static ItemStack newItem(ItemStack itemStack, String name, List<String> lore) {
        ItemStack item = itemStack;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(Material material, int quantity, String name, Boolean ench, Boolean hide) {
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(ench == true) meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        if(hide == true) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(Material material, int quantity, String name, ArrayList<String> lore, Boolean ench, Boolean hide) {
        List<String> loreL = lore;
        return newItem(material, quantity, name, loreL, ench, hide);
    }

    public static ItemStack newItem(Material material, int quantity, String name, List<String> lore, Boolean ench, Boolean hide) {
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(ench == true) meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        if(hide == true) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(ItemStack itemStack, String name, ArrayList<String> lore, Boolean ench, Boolean hide) {
        List<String> loreL = lore;
        return newItem(itemStack, name, loreL, ench, hide);
    }

    public static ItemStack newItem(ItemStack itemStack, String name, List<String> lore, Boolean ench, Boolean hide) {
        ItemStack item = itemStack;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(ench == true) meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        if(hide == true) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    @SuppressWarnings("deprecation")
	public static ItemStack newSkullItem(String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        meta.setDisplayName("§e" + playerName);
        item.setItemMeta(meta);
        return item;
    }

    public static Inventory adminInventory(NPC npc, boolean canBack) {
        /*
        slot 10 à 16 libre
        Change name npc
        Change skin npc
        Change function npc
        Remove npc
        */
        Inventory inventory = Bukkit.createInventory(null, 27, "§7NPC : " + npc.getName() + " " + InventoryNameEnums.ADMIN);
        inventory = createInventory(27, inventory, newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

        inventory.setItem(10, newItem(Material.NAME_TAG, 1, "§8Changer le nom du NPC", Arrays.asList("§7Nom actuel : " + npc.getName())));
        inventory.setItem(12, newItem(newSkullItem(npc.getSkinName()), "§8Changer le skin du NPC", Arrays.asList("§7Skin actuel : " + npc.getSkinName())));
        inventory.setItem(14, newItem(Material.BOOK, 1, "§8Changer la fonction du NPC", Arrays.asList("§7Fonction actuel : " + npc.getFunction())));
        inventory.setItem(16, newItem(Material.BARRIER, 1, "§4Supprimer le NPC"));

        if(canBack) {
            inventory.setItem(inventory.getSize()-1, newItem(Material.PAPER, 1, "§7Retour"));
        }

        return inventory;
    }

    public static Inventory globalGuildInventory(Player player, NPC NPC) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§7-" + NPC.getName() + " : " + InventoryNameEnums.GLOBALGUILD.getTitle());
        inventory = GestionInv.createInventory(27, inventory, GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));

        if(player.hasPermission("succes.npcAdmin")) {
            inventory.setItem(inventory.getSize()-1, GestionInv.newItem(GestionInv.newSkullItem(NPC.getSkinName()), InventoryNameEnums.ACCESADMIN.getTitle(), Arrays.asList("")));
        }
        if(Main.getMain().getGuilds().isInGuild(player)) {
            if(Main.getMain().getGuilds().getPlayerGuild(player).getPlayerRole(player) == RoleUnit.getHighestRole()) {
                //Objet pour la gestion de guild
                inventory.setItem(16, GestionInv.newItem(Material.BARRIER, 1, "§4Supprimer la guilde", Arrays.asList("§6En cliquant dessus vous pourrez supprimer votre guilde")));
            }else {
                //Objet pour voir les parametres de la guilde
            }
        }else {
            //Objet pour créer une guilde
            inventory.setItem(10, GestionInv.newItem(Material.WHITE_BANNER, 1, "§6Créer une guilde", Arrays.asList("§6Créer une guilde pour vous et vos amis", "§6Pour réaliser cette action il vous faudra : §e1 000§6D")));
        }
        return inventory;
    }

    public static Inventory playerBank(Player player, NPC NPC) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§7-" + NPC.getName() + " : " + InventoryNameEnums.PLAYERBANK.getTitle());
        inventory = GestionInv.createInventory(27, inventory, GestionInv.newItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        if(player.hasPermission("succes.npcAdmin")) {
            inventory.setItem(inventory.getSize()-1, GestionInv.newItem(GestionInv.newSkullItem(NPC.getSkinName()), InventoryNameEnums.ACCESADMIN.getTitle(), Arrays.asList("")));
        }

        Account account = Main.getMain().getAccount(player).get();

        inventory.setItem(10, GestionInv.newItem(Material.GOLD_INGOT, 1, "§6Votre argent :", Arrays.asList("\t" + account.getDataMoney().getMoney() + " §6D")));
        inventory.setItem(11, GestionInv.newItem(Material.EMERALD, 1, "§2Donner de l'argent à une personne"));


        return inventory;
    }
}
