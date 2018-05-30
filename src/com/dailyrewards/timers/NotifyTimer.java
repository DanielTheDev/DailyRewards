package com.dailyrewards.timers;

import com.dailyrewards.PluginClass;
import com.dailyrewards.config.PlayerDataManager;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.Initializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NotifyTimer extends BukkitRunnable implements Initializer<NotifyTimer> {

    private BukkitTask task;
    private PlayerDataManager dataManager;

    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            this.check(player);
        }
    }

    public void check(Player player) {
        if(this.dataManager.getPlayer(player).canClaim()) {
            alert(player);
        }
    }

    public void alert(Player player) {
        Chat.sendMessage(player, PluginClass.getPluginConfig().getRewardMessage(), "%player%", player.getName());
    }

    @Override
    public NotifyTimer onEnable() {
        this.dataManager = PluginClass.getPlugin().getPluginLib().getPlayerDataManager();
        this.task = this.runTaskTimer(PluginClass.getPlugin(), 20*3, 20*PluginClass.getPluginConfig().getCheckInterval());
        return this;
    }


    @Override
    public void onDisable() {
        this.task.cancel();
        Initializer.unload(this);
    }

    @Override
    public void onReload() {
        this.onDisable();
        this.onEnable();
    }
}
