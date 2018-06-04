package com.dailyrewards;

import com.dailyrewards.config.PlayerData;
import com.dailyrewards.config.PluginConfig;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.Gui;
import com.dailyrewards.extentions.Initializer;
import com.dailyrewards.menu.MainGui;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PluginClass extends JavaPlugin {

    private static PluginClass plugin;
    private PluginLib pluginLib;
    private List<Initializer> initializers;

    public static PluginClass getPlugin() {
        return plugin;
    }

    public static String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    public PluginLib getPluginLib() {
        return this.pluginLib;
    }

    public static PluginConfig getPluginConfig() {
        return plugin.getPluginLib().getPluginConfig();
    }


    public void onEnable() {
        this.setPlugin(true);
        this.initializers = new ArrayList<>();
        this.registerInitializer(this.pluginLib = new PluginLib().init());
        this.init(Initializer.Action.ENABLE);
        this.registerListeners();
        this.registerSerializableObject();
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("dailyrewards").setExecutor(this.getPluginLib().getCommandManager());
    }


    public void init(Initializer.Action action) {
        Initializer.init(action, initializers);
    }

    public void onDisable() {
        this.init(Initializer.Action.DISABLE);
        this.initializers.clear();
        this.initializers = null;
        this.pluginLib = null;
        this.setPlugin(false);
    }

    public boolean reload() {
        try {
            List<Gui> guis = PluginClass.getPlugin().getPluginLib().getMenuManager().getOpen_guis();
            Player player;
            for (int i = 0; i < guis.size(); i++) {
                player = guis.get(i).getPlayer();
                player.closeInventory();
                Chat.sendMessage(player, "&7Sorry, your reward has canceled by the plugin reload.");
            }
            this.init(Initializer.Action.RELOAD);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void registerInitializer(Initializer listener) {
        if (this.initializers.contains(listener)) {
            try {
                throw new IOException("Initializer already exists.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.initializers.add(listener);
        }
    }

    public void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this.getPluginLib().getMenuManager(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
    }


    public void registerSerializableObject() {
        ConfigurationSerialization.registerClass(PlayerData.class);
    }

    public void setPlugin(boolean load) {
        if (load) plugin = this;
        else plugin = null;
    }
}
