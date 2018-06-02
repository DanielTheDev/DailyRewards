package com.dailyrewards.config;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.CraftItem;
import com.dailyrewards.extentions.Initializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PluginConfig implements Initializer<PluginConfig> {

    private LinkedHashMap<String, Object> defaultValues;
    private int delay;
    private int resetStreak;
    private List<String> rewardMessage;
    private ConfigFile file;
    private int checkInterval;
    private boolean rewardAlert;
    private CraftItem presentTrail;
    private CraftItem presentWrapped;
    private String rewardFinished;

    public int getDelay() {
        return delay;
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

    public boolean isRewardAlert() {
        return rewardAlert;
    }

    public CraftItem getPresentTrail() {
        return presentTrail;
    }

    public CraftItem getPresentWrapped() {
        return presentWrapped;
    }

    public String getRewardFinished() {
        return rewardFinished;
    }

    public PluginConfig() {
        this.preinit();
    }

    private void preinit() {
        this.defaultValues = new LinkedHashMap<>();
        this.defaultValues.put("claim-delay-seconds", 86400);
        this.defaultValues.put("streak-reset-after-seconds", 129600);
        this.defaultValues.put("daily-reward-available-message", Arrays.asList("&aHey &e%player%&a, Your daily reward is available now.","&aType &b/dailybonus &ato collect your bonus."));
        this.defaultValues.put("reward-available-check-interval-seconds", 500);
        this.defaultValues.put("daily-reward-message-enabled", true);
        this.defaultValues.put("reward-finished-message", "&aCome back in &e24 hours &ato claim your next reward.");
    }

    private Object getValue(String path) {
        Object value = this.file.getValue(path);
        if(value == null) {

            this.file.setValue(path, this.defaultValues.get(path));
            return this.defaultValues.get(path);
        } else {
            return value;
        }
    }

    private ConfigurationSection getConfigurationSection(String path) {
        return this.file.getConfigurationSection(path);
    }

    public PluginConfig onEnable() {
        this.file = PluginClass.getPlugin().getPluginLib().getConfigClass().getConfig("config");
        this.init();
        return this;
    }

    private void init() {
        this.delay = (int) getValue("claim-delay-seconds");
        this.resetStreak = (int) getValue("streak-reset-after-seconds");
        this.rewardMessage = (List<String>) getValue("daily-reward-available-message");
        this.rewardFinished = (String) getValue("reward-finished-message");
        this.checkInterval = (int) getValue("reward-available-check-interval-seconds");
        this.rewardAlert = (boolean) getValue("daily-reward-message-enabled");
        try {
            this.presentTrail = CraftItem.fromConfiguration(getConfigurationSection("reward-animation-present-trail"));
        } catch (Exception e) {
            try {
                throw new IOException("Could not load item reward-animation-present-trail: " + e.getMessage());
            } catch (IOException e1) {
                this.presentTrail = new CraftItem(Material.NETHER_STAR).setDisplayName("&a");
                e1.printStackTrace();
            }
        }
        try {
            this.presentWrapped = CraftItem.fromConfiguration(getConfigurationSection("reward-animation-present-wrapped"));
        } catch (Exception e) {
            try {
                throw new IOException("Could not load item reward-animation-present-wrapped: " + e.getMessage());
            } catch (IOException e1) {
                this.presentWrapped = new CraftItem(Material.END_CRYSTAL).setDisplayName("&5?????").setGlow(true);
                e1.printStackTrace();
            }
        }

    }

    public void onDisable() {
        Initializer.unload(this);
    }

    public void onReload() {
        this.onDisable();
        this.onEnable();
    }
}
