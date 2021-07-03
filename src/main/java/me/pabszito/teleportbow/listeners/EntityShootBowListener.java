package me.pabszito.teleportbow.listeners;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.pabszito.teleportbow.TeleportBow;
import me.pabszito.teleportbow.common.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityShootBowListener implements Listener {

    @Inject
    private TeleportBow plugin;

    @Inject @Named("config")
    private Configuration config;

    @Inject @Named("messages")
    private Configuration messages;

    private final List<Player> onCooldown;

    public EntityShootBowListener() {
        this.onCooldown = new ArrayList<>();
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        ItemStack bow = event.getBow();
        String requiredDisplayName = messages.getString("messages.item.display_name");
        String itemDisplayName = bow.getItemMeta().getDisplayName();
        LivingEntity entity = event.getEntity();

        if(itemDisplayName.equals(requiredDisplayName)) {
            if(!(entity instanceof Player)) return;

            Player player = (Player) entity;
            if(onCooldown.contains(player)) {
                player.sendMessage(messages.getString("messages.on_cooldown"));
                event.setCancelled(true);
                return;
            }

            Arrow arrow = (Arrow) event.getProjectile();
            arrow.setCustomName(player.getName());
            arrow.setCustomNameVisible(false);
            arrow.setDamage(0);

            event.setProjectile(arrow);

            if(config.getBoolean("config.particle_effect.enabled")) {
                if (Bukkit.getVersion().contains("1.8")) {
                    plugin.getLogger().warning("Particle effects are unsupported under 1.9+. Please update to a newer version of Minecraft to use this feature.");
                } else {
                    int particleTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        Location location = event.getProjectile().getLocation();
                        String particleType = config.getString("config.particle_effect.type");

                        try {
                            player.spawnParticle(Particle.valueOf(particleType), location, 2);
                        } catch (Exception exception) {
                            plugin.getLogger().warning("Invalid particle effect provided: " + particleType);
                        }
                    }, 1, 1);

                    Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getScheduler().cancelTask(particleTask),
                            config.getLong("config.particle_effect.time_until_cancel"), 1);
                }
            }

            if(config.getBoolean("config.cooldown.enabled")) {
                long time = (long) (config.getDouble("config.cooldown.time") * 20);
                onCooldown.add(player);

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> onCooldown.remove(player), time);
            }
        }
    }
}
