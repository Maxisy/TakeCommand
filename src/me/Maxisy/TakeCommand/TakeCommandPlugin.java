package me.Maxisy.TakeCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TakeCommandPlugin extends JavaPlugin {

    String prefixTrue = ChatColor.AQUA + "[" + getConfig().getString("prefix") + "] " + ChatColor.GREEN;
    String prefixFalse = ChatColor.AQUA + "[" + getConfig().getString("prefix") + "] " + ChatColor.RED;

    @Override
    public void onEnable() {
        this.getLogger().info(prefixTrue + "Starting TakeCommand plugin...");
        this.getLogger().info(prefixTrue + "Plugin created by Maksymilian Sybicki");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        this.getLogger().info(prefixFalse + "Stopping TakeCommand plugin...");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("take")) {
            //args[0] = item; args[1] = amount; args[2] = player
            if (args.length == 3) {
                try {
                    Player target = Bukkit.getPlayerExact(args[2]);
                    if (target == null) {
                        sender.sendMessage(prefixFalse + "Player is not online!");
                    } else {
                        Material material = Material.matchMaterial(args[0]);
                        if (material == null) {
                            sender.sendMessage(prefixFalse + "Wrong argument: " + args[0]);
                        } else {
                            boolean done = removeItem(target, material, Integer.parseInt(args[1]));
                            if (done)
                                sender.sendMessage(prefixTrue + "Successfully removed " +
                                        material + " from " + target.getName() + "'s inventory!");
                            else
                                sender.sendMessage(prefixFalse + target.getName() + " hasn't got "
                                        + material + " in their inventory.");
                        }
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefixFalse + "Wrong argument: " + args[1]);
                }
            } else {
                sender.sendMessage(prefixFalse + "Arguments number incorrect!");
                return false;
            }
        }
        return true;
    }

    public boolean removeItem(Player player, Material material, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack != null && stack.getType().equals(material)) {
                int newStack = stack.getAmount() - amount;
                stack.setAmount(newStack);
                player.getInventory().setItem(i, newStack > 0 ? stack : null);
                player.updateInventory();
                return true;
            }
        }
        return false;
    }
}
