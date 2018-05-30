package com.dailyrewards.command;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.Gui;
import com.dailyrewards.menu.MainGui;
import com.dailyrewards.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {

        Player player = (Player) sender;
        MenuManager manager = PluginClass.getPlugin().getPluginLib().getMenuManager();

        Gui gui = new MainGui(player);
        manager.register(gui);
        gui.open();
        return true;
    }
}
