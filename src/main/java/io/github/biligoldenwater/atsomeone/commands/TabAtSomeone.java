package io.github.biligoldenwater.atsomeone.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckPermissions.hasPermissions;

public class TabAtSomeone {
    public TabAtSomeone(JavaPlugin plugin) {
        plugin.getCommand("atsomeone").setTabCompleter(atsomeone_tab);
    }

    TabCompleter atsomeone_tab = (sender, command, alias, args) -> { //CommandSender Command String String[]
        List<String> completions = new ArrayList<>();
        String[] subcommandList = new String[]{"help", "ignore", "lang", "resetLang", "reload"};

        switch (args.length) {
            case 1:
                args[0] = args[0].toLowerCase();
                for (String subcommand : subcommandList) {
                    if (hasPermissions(sender, "atsomeone.command." + subcommand) &&
                            subcommand.startsWith(args[0]))
                        completions.add(subcommand);
                }
                return completions;
            case 2:
                if ("ignore".equals(args[0])) {
                    if (!hasPermissions(sender, "atsomeone.command.ignore")) {
                        return completions;
                    }

                    args[1] = args[1].toLowerCase();

                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(args[1])) {
                            completions.add(player.getName());
                        }
                    }
                    return completions;
                }
        }
        return null;
    };
}
