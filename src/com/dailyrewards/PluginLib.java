package com.dailyrewards;

import com.dailyrewards.command.CommandManager;
import com.dailyrewards.config.ConfigFile;
import com.dailyrewards.config.PlayerDataManager;
import com.dailyrewards.config.PluginConfig;
import com.dailyrewards.config.RewardConfig;
import com.dailyrewards.extentions.Initializer;
import com.dailyrewards.menu.MenuManager;
import com.dailyrewards.timers.NotifyTimer;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;

public class PluginLib implements Initializer<PluginLib> {

    @LoadPriority(priority = LoadPriority.Priority.HIGH)
    private Config subclass_Config;
    private PlayerDataManager playerDataManager;
    private MenuManager menuManager;
    private CommandManager commandManager;
    private PluginConfig pluginConfig;
    private RewardConfig rewardConfig;
    private NotifyTimer notifyTimer;

    public MenuManager getMenuManager() {
        return menuManager;
    }


    public Config getConfigClass() {
        return this.subclass_Config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public NotifyTimer getNotifyTimer() {
        return notifyTimer;
    }

    public PluginLib onEnable() {
        Initializer.init(Action.ENABLE, Initializer.getInitializerFields(this));
        return this;
    }

    public PluginLib init() {
        this.playerDataManager = new PlayerDataManager();
        this.commandManager = new CommandManager();
        this.menuManager = new MenuManager();
        this.subclass_Config = new Config();
        this.pluginConfig = new PluginConfig();
        this.rewardConfig = new RewardConfig();
        this.notifyTimer = new NotifyTimer();
        return this;
    }

    public void onDisable() {
        Initializer.init(Action.DISABLE, Initializer.getInitializerFields(this));
        Initializer.unload(this);
    }

    public void onReload() {
        Initializer.init(Action.RELOAD, Initializer.getInitializerFields(this));
    }

    public RewardConfig getRewardConfig() {
        return this.rewardConfig;
    }

    public void restartTimers() {
        this.notifyTimer.onDisable();
        this.notifyTimer = new NotifyTimer();
        this.notifyTimer.onEnable();
    }

    public class Config implements Initializer<Config> {

        private LinkedHashMap<String, ConfigFile> files;
        private File playerdataFolder;

        public ConfigFile getConfig(String name) {
            return files.get(name);
        }

        public File getPlayerdataFolder() {
            return playerdataFolder;
        }

        public void registerFile(Plugin plugin, String name) {
            if (this.files.containsKey(name)) {
                try {
                    throw new IOException("ConfigFile with that name already exists '" + name + "'");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ConfigFile file = new ConfigFile(plugin, name, true);
                this.files.put(name, file.load());
            }
        }

        public void reloadFiles() {
            for (ConfigFile file : files.values()) {
                file.reload();
            }
        }

        public void unregisterFile(String name) {
            this.files.remove(name);
        }

        public void unregisterFile(ConfigFile file) {
            this.files.remove(file.getName());
        }

        public Config onEnable() {
            this.files = new LinkedHashMap<>();
            this.registerFiles();
            return this;
        }

        public void registerFiles() {
            this.setFolders();
            this.registerFile(PluginClass.getPlugin(), "config");
            this.registerFile(PluginClass.getPlugin(), "rewards");
        }

        public void setFolders() {
            this.playerdataFolder = new File(PluginClass.getPlugin().getDataFolder(), "playerdata");
            this.playerdataFolder.mkdirs();
        }


        public void onDisable() {
            this.reloadFiles();
            if (this.files != null) this.files.clear();
            this.files = null;
        }

        public void onReload() {
            if (files != null) this.files.clear();

            this.onEnable();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface LoadPriority {

        enum Priority {
            LOW(1), MEDIUM(2), HIGH(3);

            private final int type;

            Priority(int type) {
                this.type = type;
            }

            public int getType() {
                return type;
            }
        }

        Priority priority() default Priority.LOW;

    }

}
