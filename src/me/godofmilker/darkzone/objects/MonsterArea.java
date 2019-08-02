package me.godofmilker.darkzone.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.godofmilker.darkzone.utils.Cuboid;
import me.godofmilker.darkzone.utils.Gerekli;
import me.godofmilker.darkzone.utils.SerLocation;

/**
 * MonsterArea
 */
public class MonsterArea extends Cuboid {

    private static final long serialVersionUID = 1L;

    private SerLocation center;

    private List<SpawnPoint> spawnPoints;

    public MonsterArea(Location loc1, Location loc2, Location center) {
        super(loc1, loc2);
        this.center = new SerLocation(center);
        spawnPoints = new ArrayList<SpawnPoint>();
    }

    public void update() {

    }

    public SpawnPoint randomSpawnPoint() {
        if (spawnPoints.isEmpty())
            return null;
        for (int tot = 0; tot <= 100; tot++) {

        }
        int i = Gerekli.getInt(spawnPoints.size());
        return spawnPoints.get(i);
    }

    public void randomSpawn() {

    }

    public Location center() {
        return center.getLocation();
    }

    public List<SpawnPoint> readySpawnPoints() {
        List<SpawnPoint> points = new ArrayList<>();
        for (SpawnPoint point : spawnPoints) {
            if (!point.inCooldown())
                points.add(point);
        }
        return points;
    }

    public List<SpawnPoint> spawnPoints() {
        return spawnPoints;
    }
}