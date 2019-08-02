package me.godofmilker.darkzone.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.godofmilker.darkzone.utils.Gerekli;
import me.godofmilker.darkzone.utils.SerLocation;

/**
 * SpawnPoint
 */
public class SpawnPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    private SerLocation loc;
    private double chance;
    private int cooldown;
    private transient int ccooldown;
    private List<Entity> entities;

    public SpawnPoint(Location loc, double chance, int cooldown) {
        this.loc = new SerLocation(loc);
        this.chance = chance;
        this.cooldown = cooldown;
        this.entities = new ArrayList<Entity>();
    }

    public Entity randomEntity() {
        if (entities.isEmpty())
            return null;
        int i = Gerekli.getInt(entities.size());
        return entities.get(i);
    }

    public int curCooldown() {
        return ccooldown;
    }

    public int cooldown() {
        return cooldown;
    }

    public double chance() {
        return chance;
    }

    public boolean inCooldown() {
        return ccooldown <= 0;
    }

    public boolean tryChance() {
        return Gerekli.chanceof(chance / 100);
    }

    public Location location() {
        return loc.getLocation();
    }

    public List<Entity> entities() {
        return entities;
    }

    @Override
    public String toString() {
        String s = "";
        for (Entity en : entities) {
            s += en.toString() + ",";
        }
        return "Location: " + loc.toString() + ",        Entities: " + s + "         Cooldown: " + cooldown()
                + ",     Chance: " + chance;
    }
}