package com.dailyrewards;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginClass extends JavaPlugin {

    private static PluginClass plugin;

    @Override
    public void onEnable() {
        this.setPlugin(true);
        super.onEnable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onDisable() {
        this.setPlugin(false);
        super.onDisable();
    }

    public void setPlugin(boolean load) {
        if(load) plugin = this;
        else plugin = null;
    }
}
