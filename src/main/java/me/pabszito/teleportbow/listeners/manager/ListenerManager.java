package me.pabszito.teleportbow.listeners.manager;

import com.google.inject.Inject;
import me.pabszito.teleportbow.TeleportBow;
import me.pabszito.teleportbow.common.Manager;
import me.pabszito.teleportbow.listeners.EntityShootBowListener;
import me.pabszito.teleportbow.listeners.ProjectileHitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager implements Manager {

    @Inject
    private EntityShootBowListener entityShootBowListener;

    @Inject
    private ProjectileHitListener projectileHitListener;

    @Inject
    private TeleportBow plugin;

    @Override
    public void load() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(entityShootBowListener, plugin);
        pluginManager.registerEvents(projectileHitListener, plugin);
    }
}
