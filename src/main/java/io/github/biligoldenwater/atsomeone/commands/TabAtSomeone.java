package io.github.biligoldenwater.atsomeone.commands;

import io.github.biligoldenwater.atsomeone.utils.CheckPermissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TabAtSomeone {
    public TabAtSomeone(JavaPlugin plugin) {
        plugin.getCommand("atsomeone").setTabCompleter(atsomeone_tab);
    }

    TabCompleter atsomeone_tab = new TabCompleter() {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            switch (args.length) {
                case 1:
                    if (CheckPermissions.hasPermissions(sender, "atsomeone.command.help") && "help".startsWith(args[0]))
                        completions.add("help");
                    if (CheckPermissions.hasPermissions(sender, "atsomeone.command.ignore") && "ignore".startsWith(args[0]))
                        completions.add("ignore");
                    if (CheckPermissions.hasPermissions(sender, "atsomeone.command.reload") && "reload".startsWith(args[0]))
                        completions.add("reload");
                    return completions;
                case 2:
                    switch (args[0]) {
                        case "ignore":
                            if (!CheckPermissions.hasPermissions(sender, "atsomeone.command.ignore")) {
                                return completions;
                            }
                            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                if (player.getName().startsWith(args[1])) {
                                    completions.add(player.getName());
                                }
                            }
                            return completions;
                    }
            }
            return null;
        }
    };
}
