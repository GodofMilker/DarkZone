
package me.godofmilker.darkzone.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.godofmilker.darkzone.objects.DZPlayer;
import me.godofmilker.darkzone.objects.DarkZone;
import me.godofmilker.darkzone.objects.SafeArea;

/**
 * MyListener
 */
public class MyListener implements Listener {

    @EventHandler
    public void onTarget(EntityTargetEvent ev) {
        Entity en = ev.getEntity();
        if (!(en instanceof Monster))
            return;
        if (!(((Monster) en).getTarget() instanceof Player))
            return;
        Player p = (Player) ((Monster) en).getTarget();
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(p.getUniqueId());
        if (dzp == null)
            return;
        if (dzp.inSafe())
            ev.setCancelled(true);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {

    }

    @EventHandler
    public void onDeath(EntityDeathEvent ev) {
        if (!(ev.getEntity() instanceof Player))
            return;
        Player p = (Player) ev.getEntity();
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(ev.getEntity().getUniqueId());
        if (dzp == null || dzp.isGarbage())
            return;

    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent ev) {
        if (!(ev.getEntity() instanceof Player))
            return;
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(ev.getEntity().getUniqueId());
        if (dzp == null || dzp.isGarbage())
            return;
        if (dzp.authorized())
            return;
        ev.setCancelled(true);
    }

    public void onLeave(Player p) {
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(p.getUniqueId());
        if (dzp == null)
            return;
        DZPlayer.removeAllProtectings(p);
        dzp.garbage();
    }

    public void onQuit(Player p) {
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(p.getUniqueId());
        if (dzp == null || dzp.isGarbage())
            return;
        SafeArea sa = dzp.closestSafeArea();
        p.teleport(sa.center());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent ev) {
        Player p = ev.getPlayer();
        DZPlayer dzp = DZPlayer.getDirectDZPlayer(p.getUniqueId());
        if (dzp == null)
            return;
        DarkZone zone = dzp.zone();
        SafeArea sa = dzp.safeArea();
        if (sa != null) {
            if (sa.isInside(p))
                return;
            ev.setCancelled(!dzp.leaveSafeArea());
            p.teleport(sa.center());
            return;
        }
        for (SafeArea saa : zone.safeZones()) {
            if (saa.isInside(p)) {
                ev.setCancelled(!dzp.enterSafeArea(saa));
                return;
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player p = ev.getPlayer();
        DZPlayer dzp = DZPlayer.getDZPlayer(p.getUniqueId());
        if (dzp == null || dzp.isGarbage())
            return;
        ev.setRespawnLocation(dzp.closestSafeArea().center());
    }

}