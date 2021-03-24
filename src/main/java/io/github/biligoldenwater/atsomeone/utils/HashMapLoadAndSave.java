package io.github.biligoldenwater.atsomeone.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class HashMapLoadAndSave {
    public void saveHashMap(FileConfiguration config, JavaPlugin plugin, String path, HashMap<Object, Object> hashMap) {
        for (Object key : hashMap.keySet()) {
            config.set(path + "." + key, hashMap.get(key));
        }
        plugin.saveConfig();
    }

    public HashMap<Object, Object> loadHashMap(FileConfiguration config, String path) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (config.getConfigurationSection(path).getKeys(false).isEmpty()) return hashMap;
        for (String key : config.getConfigurationSection(path).getKeys(false)) {
            hashMap.put(key, config.get(path + "." + key));
        }
        return hashMap;
    }
}
