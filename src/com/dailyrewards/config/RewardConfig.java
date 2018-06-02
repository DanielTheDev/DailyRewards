package com.dailyrewards.config;

import com.dailyrewards.PluginClass;
import com.dailyrewards.extentions.CraftItem;
import com.dailyrewards.extentions.Initializer;

import java.util.ArrayList;
import java.util.List;

public class RewardConfig implements Initializer<RewardConfig> {

    private final List<Present> presents;
    private ConfigFile file;

    public RewardConfig() {
        this.presents = new ArrayList<>();
    }

    public List<Present> getPresents() {
        return presents;
    }

    private void init() {
        String permission;
        List<String> rewards;
        CraftItem opened_item = null;
        CraftItem reward_item = null;
        for (String present : this.file.getConfigurationSection("rewards").getKeys(false)) {
            permission = (String) this.getValue("rewards." + present + ".permission");
            rewards = (List<String>) this.getValue("rewards." + present + ".reward-commands");
            try {
                reward_item = CraftItem.fromConfiguration(this.file.getConfigurationSection("rewards." + present + ".reward-item"));
                opened_item = CraftItem.fromConfiguration(this.file.getConfigurationSection("rewards." + present + ".opened-item"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.presents.add(new Present(reward_item, opened_item, permission, rewards));
        }
    }

    public Object getValue(String path) {
        return this.file.getValue(path);
    }

    public RewardConfig onEnable() {
        this.file = PluginClass.getPlugin().getPluginLib().getConfigClass().getConfig("rewards");
        this.init();
        return this;
    }

    @Override
    public void onDisable() {
        this.presents.clear();
    }

    @Override
    public void onReload() {

    }

    public class Present {

        private final CraftItem item;
        private final CraftItem opened_item;
        private final String permissions;
        private final List<String> rewards;

        public Present(CraftItem item, CraftItem opened_item, String permissions, List<String> rewards) {
            this.item = item;
            this.opened_item = opened_item;
            this.permissions = permissions;
            this.rewards = rewards;
        }


        public CraftItem getItem() {
            return item;
        }

        public CraftItem getOpened_item() {
            return opened_item;
        }

        public String getPermissions() {
            return permissions;
        }

        public List<String> getRewards() {
            return rewards;
        }


        public void destroy() {
            this.rewards.clear();
        }

        @Override
        public String toString() {
            return "Present{" +
                    "item=" + item +
                    ", opened_item=" + opened_item +
                    ", permissions='" + permissions + '\'' +
                    ", rewards=" + rewards +
                    '}';
        }


    }

}
