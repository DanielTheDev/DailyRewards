package com.dailyrewards.command;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.menu.MainGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] a) {
        if(a.length == 0) {
            if(sender instanceof Player) {
                PluginClass.getPlugin().getPluginLib().getMenuManager().open(new MainGui((Player) sender));
            } else {
                Chat.sendToConsole("You cannot do that, you must be in game to perform that.");
            }
        } else if(a.length == 1 && a[0].equalsIgnoreCase("reload")) {
            if(sender.hasPermission("dailyrewards.command.reload")) {
                Chat.sendMessage(sender, "&aReload completed.");
            } else {
                Chat.sendMessage(sender, "&cYou have to no permissions to perform that.");
            }
        } else {

        }
        return true;
    }

}
