package me.pabszito.teleportbow.listeners;

import com.cryptomorin.xseries.XSound;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.pabszito.teleportbow.common.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    @Inject @Named("config")
    private Configuration config;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow) {
            Arrow arrow = (Arrow) projectile;
            if(arrow.getCustomName() != null && !arrow.getCustomName().isEmpty()) {
                Player player = Bukkit.getPlayer(arrow.getCustomName());
                if (player == null) return;

                Location arrowLocation = arrow.getLocation();
                arrowLocation.setYaw(player.getLocation().getYaw());
                player.teleport(arrowLocation);

                if(config.getBoolean("config.sound.enabled")) {
                    try {
                        XSound sound = XSound.valueOf(config.getString("config.sound.type"));
                        sound.play(player);
                    }catch(Exception ex) {
                        Bukkit.getLogger().warning("Invalid sound provided: " + config.getString("config.sound.type"));
                    }
                }

                arrow.remove(); // Delete the arrow after all that
            }
        }
    }
}
