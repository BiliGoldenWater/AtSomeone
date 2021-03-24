package io.github.biligoldenwater.atsomeone.commands;

import io.github.biligoldenwater.atsomeone.AtSomeone;
import io.github.biligoldenwater.atsomeone.utils.I18nManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckPermissions.hasPermissions_Tips;

public class CommandAtSomeone {
    AtSomeone plugin;

    public CommandAtSomeone(AtSomeone plugin) {
        this.plugin = plugin;
        plugin.getCommand("atsomeone").setExecutor(atsomeone_command);
    }

    final CommandExecutor atsomeone_command = new CommandExecutor() {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            I18nManager i18n = plugin.getI18nManager();
            Configuration config = plugin.getConfig();
            String lang = config.getString("pluginLanguage");
            String noPerm = i18n.getL10n(lang, "error_noPerm");

            if (args.length > 0) {
                switch (args[0]) {
                    case "help":
                        return commandHelp(sender, i18n, lang, noPerm);
                    case "ignore":
                        return commandIgnore(sender, i18n, lang, noPerm, args, config);
                    case "resetLang":
                        return commandResetLang(sender, i18n, lang, noPerm);
                    case "reload":
                        return commandReload(sender, i18n, lang, noPerm);
                }
            } else {
                HelpMessage.sendHelpMessages(sender, i18n, lang, HelpMessage.HelpLvl.fullHelp);
            }
            return true;
        }
    };

    private boolean commandHelp(CommandSender sender, I18nManager i18n, String lang, String noPerm) {
        if (!hasPermissions_Tips(sender, "atsomeone.command.help", noPerm)) return true;

        HelpMessage.sendHelpMessages(sender, i18n, lang, HelpMessage.HelpLvl.fullHelp);
        return true;
    }

    private boolean commandIgnore(CommandSender sender, I18nManager i18n, String lang, String noPerm,
                                  String[] args, Configuration config) {
        if (!hasPermissions_Tips(sender, "atsomeone.command.ignore", noPerm)) return true;

        switch (args.length) {
            case 1:
                HelpMessage.sendHelpMessages(sender, i18n, lang, HelpMessage.HelpLvl.commandIgnore);
                return true;
            case 2:
                if (!(sender instanceof Player)) {
                    sender.sendMessage(i18n.getL10n(lang, "error_onlyPlayerCanUse"));
                    return true;
                }
                List<String> ignoreList = config.getStringList("playerIgnore." + sender.getName());

                if (ignoreList == null) ignoreList = new ArrayList<>();

                if (ignoreList.contains(args[1])) {
                    ignoreList.remove(args[1]);

                    sender.sendMessage(i18n.getL10n(lang, "cmd_ignore_removed")
                            .replace("{{playerName}}", args[1]));
                } else {
                    ignoreList.add(args[1]);

                    sender.sendMessage(i18n.getL10n(lang, "cmd_ignore_added")
                            .replace("{{playerName}}", args[1]));
                }

                config.set("playerIgnore." + sender.getName(), ignoreList);
                return true;
        }

        return true;
    }

    private boolean commandResetLang(CommandSender sender, I18nManager i18n, String lang, String noPerm) {
        if (!hasPermissions_Tips(sender, "atsomeone.command.resetlang", noPerm)) return true;

        i18n.releaseDefaultLangFile(plugin,
                "lang",
                "langList.json",
                true);

        sender.sendMessage(i18n.getL10n(lang, "cmd_resetLang_resetFinish"));
        return true;
    }

    private boolean commandReload(CommandSender sender, I18nManager i18n, String lang, String noPerm) {
        if (!hasPermissions_Tips(sender, "atsomeone.command.reload", noPerm)) return true;

        plugin.reloadConfig();
        i18n.reload();

        sender.sendMessage(i18n.getL10n(lang, "cmd_reload_success"));
        return true;
    }

    public static class HelpMessage {
        public static void sendHelpMessages(CommandSender sender, I18nManager i18n, String language, int helpLvl) {
            sender.sendMessage("AtSomeone By.Golden_Water");

            switch (helpLvl) {
                case HelpLvl.fullHelp:
                    sender.sendMessage(i18n.getL10n(language, "help_usage_help"));
                    sender.sendMessage(i18n.getL10n(language, "help_usage_ignore"));
                    sender.sendMessage(i18n.getL10n(language, "help_usage_resetLang"));
                    sender.sendMessage(i18n.getL10n(language, "help_usage_reload"));
                    break;
                case HelpLvl.commandHelp:
                    sender.sendMessage(i18n.getL10n(language, "help_usage_help"));
                    break;
                case HelpLvl.commandIgnore:
                    sender.sendMessage(i18n.getL10n(language, "help_usage_ignore"));
                    break;
                case HelpLvl.commandResetLang:
                    sender.sendMessage(i18n.getL10n(language, "help_usage_resetLang"));
                    break;
                case HelpLvl.commandReload:
                    sender.sendMessage(i18n.getL10n(language, "help_usage_reload"));
                    break;
            }
        }

        public static class HelpLvl {
            public static final int fullHelp = 0;
            public static final int commandHelp = 1;
            public static final int commandIgnore = 2;
            public static final int commandResetLang = 3;
            public static final int commandReload = 4;
        }
    }

}
