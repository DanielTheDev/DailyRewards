package com.dailyrewards.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ConfigFile {

    private YamlConfiguration config;
    private File file;
    private String name;
    private boolean fromJar;

    public static final char PATH_SEPARATOR = '.';

    private ConfigFile() {}

    public ConfigFile(Plugin plugin, String name, boolean fromJar) {
        if(name.endsWith(".yml")) try {
            throw new IOException("File cannot contain extention. (Only name required)");
        } catch (IOException e) {
            e.printStackTrace();
        } else {
            this.fromJar = fromJar;
            this.name = name;
            this.file = new File(plugin.getDataFolder(), name+".yml");
        }

    }

    public static ConfigFile getFile(Plugin plugin, String name) {
       return new ConfigFile(plugin, name, false);
    }

    public boolean isFromJar() {
        return fromJar;
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public ConfigFile load() {
        config = new YamlConfiguration();
        try {
            boolean safe = true;
            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();
                if(fromJar) {
                    safe = this.writeToFile(this.file.getName(), this.file);
                } else {
                   this.file.createNewFile();
                }
            }
            if(safe)this.loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public void createSection(String path) {
        this.config.createSection(path);
        this.save();
    }

    public Object getValue(String path) {
        return this.config.get(path);
    }

    public boolean exists(String path) {
        return this.getValue(path) == null;
    }

    public void setValue(String path, Object value) {
        this.config.set(path, value);
        this.save();
    }

    private void loadConfig() throws IOException, InvalidConfigurationException {
        if(this.config == null) {
            throw new NullPointerException("Error Could not load " + file.getAbsolutePath());
        } else {
            this.config.load(this.file);
        }
    }

    public void clear() {
        for(String key : this.config.getKeys(false)){
            getConfig().set(key,null);
        }
        this.save();
    }

    public void reload() {
        this.load();
        this.save();
    }

    public boolean delete() {
        try{
            this.file.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean writeToFile(String source, File destination) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = this.getResource(source);
            if(is == null) {
                throw new IOException("Cannot find " + name + ".yml inside the plugin jar.");
            }
            os = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];
            int i = 0;
            while((i = is.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {}
        }
        return true;
    }

    private InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = this.getClass().getClassLoader().getResource(filename);
                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException var4) {
                return null;
            }
        }
    }
}