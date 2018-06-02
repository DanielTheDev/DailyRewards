package com.dailyrewards;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PluginClass.getPlugin().getPluginLib().getPlayerDataManager().getPlayer(event.getPlayer()).update();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PluginClass.getPlugin().getPluginLib().getPlayerDataManager().getPlayer(event.getPlayer()).update();
    }

}
