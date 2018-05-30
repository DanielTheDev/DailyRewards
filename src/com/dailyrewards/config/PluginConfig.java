package com.dailyrewards.config;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.Initializer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PluginConfig implements Initializer<PluginConfig> {

    private int delay;
    private int resetStreak;
    private List<String> rewardMessage;
    private ConfigFile file;
    private LinkedHashMap<String, Object> defaultValues;
    private int checkInterval;

    public PluginConfig() {
        this.defaultValues = new LinkedHashMap<>();
        this.defaultValues.put("claim-delay-seconds", 86400);
        this.defaultValues.put("streak-reset-after-seconds", 129600);
        this.defaultValues.put("daily-reward-available-message", Arrays.asList("&aHey &e%player%&a, Your daily reward is available now.","&aType &b/dailybonus &ato collect your bonus."));
        this.defaultValues.put("reward-available-check-interval-seconds", 500);
    }

    public int getDelay() {
        return this.delay;
    }

    public int getResetStreak() {
        return resetStreak;
    }

    public List<String> getRewardMessage() {
        return rewardMessage;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public Object getValue(String path, Class<?> c) {
        Object value = this.file.getValue(path);
        if(value == null) {
            this.file.setValue(path, this.defaultValues.get(path));
            return this.defaultValues.get(path);
        } else {
            return value;
        }
    }

    public PluginConfig onEnable() {
        this.file = PluginClass.getPlugin().getPluginLib().getConfigClass().getConfig("config");
        this.init();
        return this;
    }

    private void init() {
        this.delay = (int) getValue("claim-delay-seconds", Integer.class);
        this.resetStreak = (int) getValue("streak-reset-after-seconds", Integer.class);
        this.rewardMessage = (List<String>) getValue("daily-reward-available-message", List.class);
        this.checkInterval = (int) getValue("reward-available-check-interval-seconds", Integer.class);
    }

    public void onDisable() {
        Initializer.unload(this);
    }

    public void onReload() {
        this.onDisable();
        this.onEnable();
    }
}
