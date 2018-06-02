package com.dailyrewards.command;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.Gui;
import com.dailyrewards.menu.MainGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] a) {
        if (sender.hasPermission("dailyrewards.command.dailyrewards")) {
            if (a.length == 0) {
                if (sender instanceof Player) {
                    PluginClass.getPlugin().getPluginLib().getMenuManager().open(new MainGui((Player) sender));
                } else {
                    Chat.sendToConsole("You cannot do that, you must be in game to perform that.");
                }
            } else if (a.length > 0 && a[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("dailyrewards.command.reload")) {
                    if (a.length == 1) {

                        List<Gui> guis = PluginClass.getPlugin().getPluginLib().getMenuManager().open_guis;
                        int haveOpen = 0;

                        for (int i = 0; i < guis.size(); i++) {
                            if (guis.get(i).getClass().getSimpleName().equals("RewardGui")) haveOpen++;
                        }
                        if (haveOpen > 0) {
                            Chat.sendMessage(sender, "&cWarning: &e" + haveOpen + "&7 player(s) are currently opening their rewards, to proceed you must type &6/dailyrewards reload override");
                        } else {
                            if (PluginClass.getPlugin().reload()) {
                                Chat.sendMessage(sender, "&asuccessfully reloaded the plugin.");
                            } else {
                                Chat.sendMessage(sender, "&cerror while reloading the plugin.");
                            }
                        }
                    } else if (a.length == 2 && a[1].equalsIgnoreCase("override")) {
                        if (PluginClass.getPlugin().reload()) {
                            Chat.sendMessage(sender, "&asuccessfully override reloaded the plugin.");
                        } else {
                            Chat.sendMessage(sender, "&cerror while override reloading the plugin.");
                        }
                    }
                } else {
                    Chat.sendMessage(sender, "&cYou have to no permissions to perform that.");
                }
            } else {
                Chat.sendMessage(sender, "&7reload the plugin by typing &6/dailyrewards reload");
            }
        } else Chat.sendMessage(sender, "&cYou have to no permissions to perform that.");
        return true;
    }

}
