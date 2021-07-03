package me.pabszito.teleportbow;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.pabszito.teleportbow.commands.TeleportBowCommand;
import me.pabszito.teleportbow.listeners.manager.ListenerManager;
import me.pabszito.teleportbow.module.BinderModule;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportBow extends JavaPlugin {

    @Inject
    private ListenerManager listenerManager;

    @Inject
    private TeleportBowCommand mainCommand; // No need to create a Manager for now

    @Override
    public void onEnable() {
        setupInjection();

        listenerManager.load();
        this.getCommand("teleportbow").setExecutor(mainCommand);

        String version = this.getDescription().getVersion();
        this.getLogger().info("TeleportBow version " + version + " has been enabled.");
    }

    @Override
    public void onDisable() {
        String version = this.getDescription().getVersion();
        this.getLogger().info("TeleportBow version " + version + " has been disabled.");
    }

    private void setupInjection() {
        BinderModule module = new BinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }
}
