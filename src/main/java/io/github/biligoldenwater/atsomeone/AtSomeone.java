package io.github.biligoldenwater.atsomeone;

import io.github.biligoldenwater.atsomeone.commands.CommandAtSomeone;
import io.github.biligoldenwater.atsomeone.commands.TabAtSomeone;
import io.github.biligoldenwater.atsomeone.listener.OnPlayerChat;
import io.github.biligoldenwater.atsomeone.listener.OnPlayerChatTabCompleteEvent;
import io.github.biligoldenwater.atsomeone.utils.I18nManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class AtSomeone extends JavaPlugin {
    private final AtSomeone instance = this;
    private I18nManager i18nManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new OnPlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerChatTabCompleteEvent(), this);
        new CommandAtSomeone(this);
        new TabAtSomeone(this);
        saveDefaultConfig();

        i18nManager = new I18nManager(getDataFolder(), "lang", "en_US");
        i18nManager.releaseDefaultLangFile(this, "lang", "langList.json", false);

        getLogger().info("AtSomeone enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        getLogger().info("AtSomeone disabled");
    }

    public AtSomeone getInstance() {
        return instance;
    }

    public I18nManager getI18nManager() {
        return i18nManager;
    }
}

