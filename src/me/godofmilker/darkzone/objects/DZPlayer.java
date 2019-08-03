
package me.godofmilker.darkzone.objects;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.godofmilker.darkzone.utils.ZSIGN;

/**
 * DZPlayer
 */
public class DZPlayer {

    public static HashMap<UUID, DZPlayer> dzPlayers = new HashMap<UUID, DZPlayer>();
    public static final double yDifferenceThreshold = 10;
    public static final int protectingItemCount = 6;
    public static final int holdingItemCount = 6;

    public static DZPlayer getDZPlayer(UUID id) {
        Player p = Bukkit.getPlayer(id);
        DarkZone zone = DarkZone.getDarkZone(p.getLocation());
        if (zone == null) {
            if (dzPlayers.containsKey(id))
                dzPlayers.remove(id);
            return null;
        }

        if (dzPlayers.containsKey(id)) {
            DZPlayer dzp = dzPlayers.get(id);
            if (zone != dzp.zone) {
                dzPlayers.remove(id);
            }
            return dzp;
        }

        DZPlayer dzp = new DZPlayer(id, zone);
        dzPlayers.put(id, dzp);
        return dzp;
    }

    public static DZPlayer getDirectDZPlayer(UUID id) {
        if (dzPlayers.containsKey(id))
            return dzPlayers.get(id);
        return null;
    }

    private Inventory inv;
    private UUID id;
    private boolean authorized;
    private DarkZone zone;
    private SafeArea sa;
    private boolean garbage;

    public DZPlayer(UUID id, DarkZone zone) {
        this.id = id;
        inv = Bukkit.createInventory(Bukkit.getPlayer(id), 2);
        this.zone = zone;
    }

    public UUID id() {
        return id;
    }

    public Inventory inventory() {
        return inv;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    public void showInventory() {
        Bukkit.getPlayer(id).openInventory(inv);
    }

    public boolean isGarbage() {
        Player p = Bukkit.getPlayer(id);
        return garbage || p == null || !p.isOnline() || zone() == null || !zone().isInside(p);
    }

    public void garbage() {
        this.garbage = true;
    }

    public boolean authorized() {
        return authorized;
    }

    public DarkZone zone() {
        return zone;
    }

    public boolean isInventoryEmpty() {
        for (ItemStack is : inv.getContents())
            if (is != null && is.getType() != Material.AIR)
                return true;
        return false;
    }

    public enum InventorySuitableState {
        SUITABLE, OVERTOTAL, OVERPROTECT, LOWERPROTECT, LOWERTOTAL;
    }

    public InventorySuitableState getInventorySuitableState() {
        Player p = Bukkit.getPlayer(id);
        Inventory invv = p.getInventory();
        int tot = 0;
        int ptot = 0;
        for (ItemStack is : invv.getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                tot++;
                if (ZSIGN.sorImzaZ(is, "dropProtect")) {
                    String imza = ZSIGN.alImzaZ(is, "dropProtect");
                    if (imza != p.getName())
                        ZSIGN.silImza(is, "dropProtect");
                    else
                        ptot++;
                    if (ptot >= protectingItemCount)
                        return InventorySuitableState.OVERPROTECT;
                }
                if (tot >= holdingItemCount) {
                    return InventorySuitableState.OVERTOTAL;
                }
            }
        }

        if (tot < holdingItemCount)
            return InventorySuitableState.LOWERTOTAL;
        if (ptot < protectingItemCount)
            return InventorySuitableState.LOWERPROTECT;
        return InventorySuitableState.SUITABLE;
    }

    public boolean inSafe() {
        return sa != null;
    }

    public boolean enterSafeArea(SafeArea sa) {
        Player p = Bukkit.getPlayer(id);
        if (isInventoryEmpty() && !p.isSneaking()) {
            p.sendMessage(
                    "İlk olarak ganimet envanterini boşalt veya shift'e basılı tutarak gir.(envanterini boşaltır)");
            return false;
        }
        inv.clear();
        this.sa = sa;
        p.sendMessage("Güvenli alana girdin");

        return true;
    }

    public boolean leaveSafeArea() {
        Player p = Bukkit.getPlayer(id);
        InventorySuitableState state = getInventorySuitableState();
        if (state == InventorySuitableState.OVERPROTECT) {
            p.sendMessage("Üzerinde barındırabileceğinden fazla korumalı eşya barındırıyorsun. En fazla "
                    + protectingItemCount + " eşya koruyabilirsin.");
            return false;
        }
        if (state == InventorySuitableState.OVERTOTAL) {
            p.sendMessage("Üzerinde barındırabileceğinden fazla eşya barındırıyorsun. En fazla " + holdingItemCount
                    + " eşya barındırabilirsin.");
            return false;
        }
        boolean sneaking = p.isSneaking();
        if (state == InventorySuitableState.LOWERPROTECT && !sneaking) {
            p.sendMessage(
                    "Daha fazla eşyayı düşmeye korumaya alabilirsin, eğer yine de çıkmak istiyorsan eğilerek çık. Max: "
                            + protectingItemCount);
            return false;
        }
        if (state == InventorySuitableState.LOWERTOTAL && !sneaking) {
            p.sendMessage("Daha fazla eşyayı yanına alabilirsin, eğer yine de çıkmak istiyorsan eğilerek çık. Max: "
                    + holdingItemCount);
            return false;
        }
        if (sneaking) {
            p.sendMessage(state == InventorySuitableState.LOWERTOTAL ? "Üzerine daha fazla eşya alabilirsin."
                    : "Daha fazla eşyayı korumaya alabilirsin.");
        }
        this.sa = null;
        p.sendMessage("iyi şanslar");
        return true;
    }

    public SafeArea safeArea() {
        return sa;
    }

    public SafeArea closestSafeArea() {
        if (inSafe())
            return sa;
        DarkZone zone = zone();
        SafeArea bsa = null;
        double dist = -1;
        for (SafeArea sa : zone.safeZones()) {
            Location loc = sa.center();
            Location ploc = Bukkit.getPlayer(id).getLocation();
            if (ploc.getY() - loc.getY() > yDifferenceThreshold)
                continue;
            double d = ploc.distance(loc);
            boolean change = dist == -1 ? true : (dist > d ? true : false);
            dist = change ? d : dist;
            if (change)
                bsa = sa;
        }
        return bsa;
    }

    public static ItemStack addProtecting(Player p, ItemStack is) {
        return ZSIGN.imzalaZ("dropProtect", p.getName(), is);
    }

    public static void removeProtecting(ItemStack is) {
        ZSIGN.silImza(is, "dropProtect");
    }

    public static void removeAllProtectings(Player p) {
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                removeProtecting(is);
            }
        }
    }

}