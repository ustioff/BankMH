// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.configs;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

public class Banks
{
    private static FileConfiguration config;
    
    public void loadYamlFile(final Plugin plugin) {
        final File file = new File(plugin.getDataFolder(), "banks.yml");
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("banks.yml", true);
        }
        Banks.config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
    }
    
    public static List<String> getBanks() {
        return new ArrayList<String>(Banks.config.getKeys(false));
    }
    
    public static FileConfiguration getConfig() {
        return Banks.config;
    }
}
