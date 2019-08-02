package me.godofmilker.darkzone.utils;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class ZSIGN {
    public ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    public static HashMap<String, ItemStack> list = new HashMap<String, ItemStack>();

    @Deprecated
    public static ItemStack imzala(String str, ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        tag.put("ZSignature", str);
        NbtFactory.setItemTag(ret, tag);
        list.put(str, ret);
        return ret;

    }

    public static ItemStack imzalaZ(String baslik, String str, ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        tag.put("ZSign_" + baslik, str);
        NbtFactory.setItemTag(ret, tag);
        return ret;

    }

    @Deprecated
    public static boolean sorImza(ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        if (tag.containsKey("ZSignature")) {
            return true;
        }
        return false;
    }

    public static boolean sorImzaZ(ItemStack is, String baslik) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        if (tag.containsKey("ZSign_" + baslik)) {
            return true;
        }
        return false;
    }

    @Deprecated
    public static String alImza(ItemStack is) {
        if (sorImza(is)) {
            ItemStack ret = is.clone();
            ret = MinecraftReflection.getBukkitItemStack(ret);
            NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
            return tag.getString("ZSignature");
        }
        return null;
    }

    public static String alImzaZ(ItemStack is, String baslik) {
        if (sorImzaZ(is, baslik)) {
            ItemStack ret = is.clone();
            ret = MinecraftReflection.getBukkitItemStack(ret);
            NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
            return tag.getString("ZSign_" + baslik);
        }
        return null;
    }

    public static boolean silImza(ItemStack is, String baslik) {
        if (sorImzaZ(is, baslik)) {
            ItemStack ret = is.clone();
            ret = MinecraftReflection.getBukkitItemStack(ret);
            NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
            tag.remove("ZSign_" + baslik);
            return true;
        }
        return false;
    }

}
