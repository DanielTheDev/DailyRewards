package com.dailyrewards.config;

import com.dailyrewards.PluginClass;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PlayerDataManager {

    public PlayerData getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

    public PlayerData getPlayer(UUID uuid) {
        ConfigFile file = getFile(uuid);
        if (!exists(uuid)) {
            file.load();
            file.setValue(uuid.toString(), new PlayerData(uuid));
        } else {
            file.load();
            if (file.getValue(uuid.toString()) == null || !(file.getValue(uuid.toString()) instanceof PlayerData)) {
                file.clear();
                file.setValue(uuid.toString(), new PlayerData(uuid));
            }
        }
        return (PlayerData) file.getValue(uuid.toString());
    }

    public boolean delete(UUID uuid) {
        if (exists(uuid)) {
            return getFile(uuid).delete();
        } else return false;
    }

    public boolean delete(PlayerData data) {
        return delete(data.getUuid());
    }

    public boolean delete(Player player) {
        return delete(player.getUniqueId());
    }

    public void update(PlayerData data) {
        ConfigFile file = getFile(data.getUuid());
        file.load();
        if (!exists(data.getUuid())) {
            file.setValue(data.getUuid().toString(), new PlayerData(data.getUuid()));
        } else {
            file.setValue(data.getUuid().toString(), data);
        }
    }

    public boolean exists(UUID uuid) {
        return new File(PluginClass.getPlugin().getPluginLib().getConfigClass().getPlayerdataFolder(), uuid.toString() + ".yml").exists();
    }

    private ConfigFile getFile(UUID uuid) {
        return new ConfigFile(PluginClass.getPlugin(), new File(PluginClass.getPlugin().getPluginLib().getConfigClass().getPlayerdataFolder(), uuid.toString() + ".yml"));
    }

}
