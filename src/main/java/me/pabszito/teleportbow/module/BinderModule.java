package me.pabszito.teleportbow.module;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import me.pabszito.teleportbow.TeleportBow;
import me.pabszito.teleportbow.common.Configuration;

public class BinderModule extends AbstractModule {

    private final TeleportBow plugin;

    public BinderModule(TeleportBow plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        bind(TeleportBow.class).toInstance(plugin);

        bind(Configuration.class).annotatedWith(Names.named("config"))
                .toInstance(new Configuration(plugin, "config"));
        bind(Configuration.class).annotatedWith(Names.named("messages"))
                .toInstance(new Configuration(plugin, "messages"));
    }
}
