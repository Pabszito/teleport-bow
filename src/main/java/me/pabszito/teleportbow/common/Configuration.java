package me.pabszito.teleportbow.common;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final JavaPlugin plugin;
    private final File folder;

    public Configuration(JavaPlugin plugin, String filename, String fileExtension, File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = filename + (filename.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public Configuration(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public Configuration(JavaPlugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    @Override
    public String getString(String path) {
        if (super.getString(path) == null) {
            return "Unknown message";
        } else {
            return ChatColor.translateAlternateColorCodes('&', super.getString(path));
        }
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        if (!this.contains(path)) return def;
        return super.getItemStack(path);
    }

    public List<String> getStringList(String path, List<String> def) {
        if (!super.contains(path)) return def;
        return super.getStringList(path);
    }

    public List<String> getColoredStringList(String path) {
        List<String> coloredStringList = super.getStringList(path);
        for (int i = 0; i < coloredStringList.size(); i++) {
            coloredStringList.set(i, ChatColor.translateAlternateColorCodes('&', coloredStringList.get(i)));
        }
        return coloredStringList;
    }

    public int getInt(String path) {
        int integer = super.getInt(path);
        return integer;
    }

    public int getInt(String path, int def) {
        int integer = super.getInt(path, def);
        return integer;
    }

    public <T> T get(Class<T> clazz, String path) {
        if (!super.contains(path)) return null;
        Object obj = super.get(path);
        return clazz.cast(obj);
    }

    private File createFile() {
        try {
            final File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }
                this.load(file);
            } else {
                this.load(file);
                this.save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void save() {
        final File folder = this.plugin.getDataFolder();
        try {
            this.save(new File(folder, this.fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated // this doesn't works at all lol
    public void reload() throws IOException, InvalidConfigurationException {
        File file = new File(folder, this.fileName);
        this.save();
        this.load(file);
    }
}