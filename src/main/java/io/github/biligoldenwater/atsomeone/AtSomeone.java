package io.github.biligoldenwater.atsomeone;

import io.github.biligoldenwater.atsomeone.commands.CommandAtSomeone;
import io.github.biligoldenwater.atsomeone.commands.TabAtSomeone;
import io.github.biligoldenwater.atsomeone.listener.OnPlayerChat;
import io.github.biligoldenwater.atsomeone.listener.OnPlayerChatTabCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AtSomeone extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new OnPlayerChat(),this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerChatTabCompleteEvent(),this);
        new CommandAtSomeone(this);
        new TabAtSomeone(this);
        saveDefaultConfig();
        Bukkit.getLogger().info("AtSomeone enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        Bukkit.getLogger().info("AtSomeone disabled");
    }
}

