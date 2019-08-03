package me.godofmilker.darkzone.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Cuboid implements Serializable, Cloneable {
    private static final long serialVersionUID = 2680128633948213448L;
    protected SerLocation min, max;

    public Cuboid(SerLocation a, SerLocation b) {
        this(a.getLocation(), b.getLocation());
    }

    public Cuboid(Location a, Location b) {
        if (!a.getWorld().equals(b.getWorld()))
            throw new IllegalArgumentException("Locations are not in the same world !");
        updateLocations(a, b);
    }

    public void updateLocations(Location a, Location b) {
        int minX = (Math.min(a.getBlockX(), b.getBlockX()));
        int minY = (Math.min(a.getBlockY(), b.getBlockY()));
        int minZ = (Math.min(a.getBlockZ(), b.getBlockZ()));
        int maxX = (Math.max(a.getBlockX(), b.getBlockX()));
        int maxY = (Math.max(a.getBlockY(), b.getBlockY()));
        int maxZ = (Math.max(a.getBlockZ(), b.getBlockZ()));
        min = new SerLocation(new Location(a.getWorld(), minX, minY, minZ));
        max = new SerLocation(new Location(a.getWorld(), maxX, maxY, maxZ));
    }

    public ArrayList<SerLocation> getOutlinedFill() {
        int x1 = min.getLocation().getBlockX();
        int y1 = min.getLocation().getBlockY();
        int z1 = min.getLocation().getBlockZ();
        int x2 = max.getLocation().getBlockX();
        int y2 = max.getLocation().getBlockY();
        int z2 = max.getLocation().getBlockZ();
        ArrayList<SerLocation> locs = new ArrayList<SerLocation>();
        World w = min.getLocation().getWorld();
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    locs.add(new SerLocation(new Location(w, x, y, z)));
                }

            }
        }
        x1++;
        y1++;
        z1++;
        x2--;
        y2--;
        z2--;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    locs.remove(new SerLocation(new Location(w, x, y, z)));
                }

            }
        }
        return locs;

    }

    public ArrayList<SerLocation> getLinedFill() {
        int x1 = min.getLocation().getBlockX();
        int y1 = min.getLocation().getBlockY();
        int z1 = min.getLocation().getBlockZ();
        int x2 = max.getLocation().getBlockX();
        int y2 = max.getLocation().getBlockY();
        int z2 = max.getLocation().getBlockZ();

        ArrayList<SerLocation> locs = new ArrayList<SerLocation>();
        World w = min.getLocation().getWorld();
        Location upperSW = new Location(w, x1, y2, z1);
        Location upperSE = new Location(w, x2, y2, z1);
        Location upperNW = new Location(w, x1, y2, z2);
        Location upperNE = new Location(w, x2, y2, z2);
        Location lowerSW = new Location(w, x1, y1, z1);
        Location lowerSE = new Location(w, x2, y1, z1);
        Location lowerNW = new Location(w, x1, y1, z2);
        Location lowerNE = new Location(w, x2, y1, z2);
        ArrayList<Location> fromList = new ArrayList<Location>(Arrays.asList(upperSW, upperSE, upperNE, upperNW,
                upperSW, upperSE, upperNE, upperNW, lowerSW, lowerSE, lowerNE, lowerNW));
        ArrayList<Location> toList = new ArrayList<Location>(Arrays.asList(upperSE, upperNE, upperNW, upperSW, lowerSW,
                lowerSE, lowerNE, lowerNW, lowerSE, lowerNE, lowerNW, lowerSW));
        for (int i = 0; i < fromList.size(); i++) {

            Location from = fromList.get(i);
            Location to = toList.get(i);

            for (SerLocation loc : getLine(from, to, w)) {
                locs.add(loc.clone());
            }
        }

        return locs;

    }

    private ArrayList<SerLocation> getLine(Location a, Location b, World w) {
        ArrayList<SerLocation> locs = new ArrayList<SerLocation>();
        int x1 = (Math.min(a.getBlockX(), b.getBlockX()));
        int y1 = (Math.min(a.getBlockY(), b.getBlockY()));
        int z1 = (Math.min(a.getBlockZ(), b.getBlockZ()));
        int x2 = (Math.max(a.getBlockX(), b.getBlockX()));
        int y2 = (Math.max(a.getBlockY(), b.getBlockY()));
        int z2 = (Math.max(a.getBlockZ(), b.getBlockZ()));
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {

                for (int y = y1; y <= y2; y++) {
                    locs.add(new SerLocation(new Location(w, x, y, z)));

                }

            }
        }
        return locs;
    }

    public Location getMiddleLocation() {
        int x1 = min.getLocation().getBlockX();
        int y1 = min.getLocation().getBlockY();
        int z1 = min.getLocation().getBlockZ();
        int x2 = max.getLocation().getBlockX();
        int y2 = max.getLocation().getBlockY();
        int z2 = max.getLocation().getBlockZ();
        return new Location(min.getLocation().getWorld(), ((x1 + x2) / 2), ((y1 + y2) / 2), ((z1 + z2) / 2));
    }

    public boolean isInside(LivingEntity en) {
        return isInside(en.getLocation());
    }

    public boolean isInside(Location l) {
        Location l2 = min.getLocation();
        Location l3 = max.getLocation();
        if (l.getX() > l2.getX() && l.getY() > l2.getY() && l.getZ() > l2.getZ() && l.getX() < l3.getX()
                && l.getY() < l3.getY() && l.getZ() < l3.getZ())
            return true;
        return false;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getLocation().getWorld().getName().equals(min.getLocation().getWorld().getName()))
                continue;
            if (isInside(p))
                list.add(p);
        }
        return list;
    }

    @Override
    public Cuboid clone() {
        try {
            Cuboid cb = (Cuboid) super.clone();
            cb.max = max.clone();
            cb.min = min.clone();
            return cb;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final SerLocation getMin() {
        return min;
    }

    public final SerLocation getMax() {
        return max;
    }
}
