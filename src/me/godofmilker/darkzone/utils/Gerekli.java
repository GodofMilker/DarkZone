package me.godofmilker.darkzone.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gerekli {

    public static final Random random = new Random();
    public static final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    public static boolean chanceof(double chance) {
        boolean bool = random.nextDouble() <= chance;
        return bool;
    }

    public static int getInt(int size) {
        return random.nextInt(size);
    }

    public static boolean isDouble(String string) {

        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String string) {

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static ItemStack yapEsya(ItemStack is, String isim, ArrayList<String> lore, short altid) {
        ItemStack esya = is;
        ItemMeta esyam = esya.getItemMeta();
        if (isim != null) {
            esyam.setDisplayName(ChatColor.translateAlternateColorCodes('&', isim));
        }
        if (lore != null) {
            ArrayList<String> lored = new ArrayList<String>();
            for (String str : lore) {
                lored.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            esyam.setLore(lored);
        }
        if (altid > 0) {
            // esya.setDurability(altid);
        }
        esya.setItemMeta(esyam);
        return is;

    }

    public static String cevc(String a) {
        return ChatColor.translateAlternateColorCodes('&', a);
    }

    public static <T> T randomFromList(List<T> list) {
        if (list.isEmpty())
            return null;
        int i = Gerekli.getInt(list.size());
        return list.get(i);
    }

    public static Location getRandomLocation(Location loc, int range, double radius) {
        int i = Gerekli.getInt(range + 1);
        double a = 2 * Math.PI / range * i;
        double x = Math.cos(a) * radius + loc.getX();
        double z = Math.sin(a) * radius + loc.getZ();
        return new Location(loc.getWorld(), x, loc.getY(), z);
    }

    public static final void sendEquipmentPacket(int entityid, ItemSlot slot, ItemStack is, Player p) {
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, entityid);
        packet.getItemSlots().write(0, slot);
        packet.getItemModifier().write(0, is);
        try {
            pm.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
