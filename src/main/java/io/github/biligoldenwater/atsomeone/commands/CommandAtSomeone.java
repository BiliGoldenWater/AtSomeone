package io.github.biligoldenwater.atsomeone.commands;

import io.github.biligoldenwater.atsomeone.AtSomeone;
import io.github.biligoldenwater.atsomeone.utils.HashMapLoadAndSave;
import io.github.biligoldenwater.atsomeone.utils.I18nManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckPermissions.hasPermissions_Tips;

public class CommandAtSomeone {
    AtSomeone plugin;

    public CommandAtSomeone(AtSomeone plugin) {
        this.plugin = plugin;
        plugin.getCommand("atsomeone").setExecutor(atsomeone_command);
    }

    CommandExecutor atsomeone_command = new CommandExecutor() {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            I18nManager i18n = plugin.getI18nManager();
            Configuration config = plugin.getConfig();
            String lang = config.getString("pluginLanguage");
            String noPerm = i18n.getL10n(lang, "warn_noPerm");
            switch (args.length) {
                case 1:
                    switch (args[0]) {
                        case "help":
                            if (!hasPermissions_Tips(sender, "atsomeone.command.help", noPerm)) return true;

                            sender.sendMessage("AtSomeone By.Golden_Water");
                            sender.sendMessage(i18n.getL10n(lang, "help_usage_help"));
                            sender.sendMessage(i18n.getL10n(lang, "help_usage_ignore"));
                            sender.sendMessage(i18n.getL10n(lang, "help_usage_resetLang"));
                            sender.sendMessage(i18n.getL10n(lang, "help_usage_reload"));

                            return true;
                        case "ignore":
                            if (!hasPermissions_Tips(sender, "atsomeone.command.ignore", noPerm)) return true;

                            sender.sendMessage(i18n.getL10n(lang, "help_usage_ignore"));
                            return true;
                        case "resetLang":
                            if (!hasPermissions_Tips(sender, "atsomeone.command.resetlang", noPerm)) return true;

                            i18n.releaseDefaultLangFile(plugin, "lang", "langList.json", true);

                            return true;
                        case "reload":
                            if (!hasPermissions_Tips(sender, "atsomeone.command.reload", noPerm)) {
                                return true;
                            }

                            plugin.reloadConfig();
                            i18n.reload();

                            sender.sendMessage(i18n.getL10n(lang, "cmd_reload_success"));
                            return true;
                    }
                case 2:
                    if ("ignore".equals(args[0])) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(i18n.getL10n(lang, "warn_onlyPlayerCanUse"));
                            return true;
                        }
                        if (!hasPermissions_Tips(sender, "atsomeone.command.ignore", noPerm)) {
                            return true;
                        }
                        FileConfiguration fileConfig = plugin.getConfig();
                        HashMapLoadAndSave hashConfig = new HashMapLoadAndSave();
                        HashMap<Object, Object> hm = hashConfig.loadHashMap(fileConfig, "playerIgnore");
                        List<String> ignoreList = (List<String>) hm.get(sender.getName());

                        if (ignoreList == null) ignoreList = new ArrayList<>();

                        for (String i : ignoreList) {
                            if (i.equals(args[1])) {
                                ignoreList.remove(args[1]);

                                sender.sendMessage(i18n.getL10n(lang, "cmd_ignore_removed")
                                        .replace("{{playerName}}", args[1]));

                                hm.put(sender.getName(), ignoreList);
                                hashConfig.saveHashMap(fileConfig, plugin, "playerIgnore", hm);

                                return true;
                            }
                        }

                        ignoreList.add(args[1]);

                        sender.sendMessage(i18n.getL10n(lang, "cmd_ignore_added")
                                .replace("{{playerName}}", args[1]));

                        hm.put(sender.getName(), ignoreList);
                        hashConfig.saveHashMap(fileConfig, plugin, "playerIgnore", hm);
                        return true;
                    }
                default:
                    return false;
            }
        }
    };


}
