// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class Data
{
    private static FileConfiguration config;
    private static File file;
    
    public void loadYamlFile(final Plugin plugin) {
        Data.file = new File(plugin.getDataFolder(), "data.yml");
        if (!Data.file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("data.yml", true);
        }
        Data.config = (FileConfiguration)YamlConfiguration.loadConfiguration(Data.file);
    }
    
    public static void save() {
        try {
            Data.config.save(Data.file);
            Data.config.load(Data.file);
        }
        catch (IOException | InvalidConfigurationException ex2) {
            final Exception ex = new Exception();
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static FileConfiguration getConfig() {
        return Data.config;
    }
}
