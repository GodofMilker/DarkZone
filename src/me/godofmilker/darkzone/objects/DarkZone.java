package me.godofmilker.darkzone.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;

import me.godofmilker.darkzone.utils.Cuboid;
import me.godofmilker.darkzone.utils.Gerekli;

/**
 * Zone
 */
public class DarkZone extends Cuboid {

    private static final long serialVersionUID = 1L;

    public static final List<DarkZone> darkZones = new ArrayList<DarkZone>();

    public static DarkZone getDarkZone(Location loc) {
        for (DarkZone zone : darkZones) {
            if (zone.isInside(loc))
                return zone;
        }
        return null;
    }

    private boolean active;
    private List<SafeArea> safeZones;
    private List<MonsterArea> monsterAreas;

    public DarkZone(Location loc1, Location loc2) {
        super(loc1, loc2);
        safeZones = new ArrayList<>();
        monsterAreas = new ArrayList<>();
    }

    public void update() {
        for (MonsterArea ma : monsterAreas) {
            ma.update();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deActivate() {
        this.active = false;
    }

    public List<SafeArea> safeZones() {
        return safeZones;
    }

    public List<MonsterArea> monsterAreas() {
        return monsterAreas;
    }

    public SafeArea randomSuitableSafeArea() {
        return Gerekli.randomFromList(new ArrayList<SafeArea>(safeZones).stream().filter(sa -> {
            return !sa.isDangerous();
        }).collect(Collectors.toList()));
    }
}