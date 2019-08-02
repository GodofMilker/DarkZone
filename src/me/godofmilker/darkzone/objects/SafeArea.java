package me.godofmilker.darkzone.objects;

import org.bukkit.Location;

import me.godofmilker.darkzone.utils.Cuboid;
import me.godofmilker.darkzone.utils.SerLocation;

/**
 * SafeArea
 */
public class SafeArea extends Cuboid {

    private static final long serialVersionUID = 1L;
    private SerLocation center;

    public SafeArea(Location loc1, Location loc2, Location center) {
        super(loc1, loc2);
        this.center = new SerLocation(center);
    }

    public Location center() {
        return center.getLocation();
    }

    public boolean isDangerous() {
        return center.getLocation().getY() < dangerTreshold();
    }

    public static double dangerTreshold() {
        return 50;
    }

}