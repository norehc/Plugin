package fr.norehc.test.gestion;

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

    public static ItemStack newSkullItem(String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        item.setItemMeta(meta);
        return item;
    }

}
