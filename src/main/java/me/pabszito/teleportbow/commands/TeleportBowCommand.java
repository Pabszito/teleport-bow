package me.pabszito.teleportbow.commands;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.pabszito.teleportbow.common.Configuration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleportBowCommand implements CommandExecutor {

    @Inject @Named("messages")
    private Configuration messages;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player in order to do that!");
            return true;
        }

        Player player = (Player) sender;

        ItemStack item = new ItemStack(Material.BOW, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(messages.getString("messages.item.display_name"));
        meta.setLore(messages.getColoredStringList("messages.item.lore"));

        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        return true;
    }
}
