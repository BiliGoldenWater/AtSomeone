package io.github.biligoldenwater.atsomeone.commands;

import io.github.biligoldenwater.atsomeone.modules.CheckPermissions;
import io.github.biligoldenwater.atsomeone.config.HashMapLoadAndSave;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandAtSomeone {
    JavaPlugin plugin;
    public CommandAtSomeone(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("atsomeone").setExecutor(atsomeone_command);
    }

    CommandExecutor atsomeone_command = new CommandExecutor() {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            switch (args.length){
                case 1:
                    switch (args[0]){
                        case "help":
                            if(!CheckPermissions.hasPermissions(sender,"atsomeone.command.help")){
                                sender.sendMessage("§cYou don't have permission.");
                                return true;
                            }
                            sender.sendMessage("AtSomeone By.Golden_Water");
                            sender.sendMessage("Usage: /aso help (Show this message.)");
                            sender.sendMessage("Usage: /aso ignore <player> (Add or Remove someone in ignore list.)");
                            sender.sendMessage("Usage: /aso reload (Reload config.)");
                            return true;
                        case "ignore":
                            if(!CheckPermissions.hasPermissions(sender,"atsomeone.command.ignore")){
                                sender.sendMessage("§cYou don't have permission.");
                                return true;
                            }
                            sender.sendMessage("Usage: /aso ignore <player>");
                            return true;
                        case "reload":
                            if(!CheckPermissions.hasPermissions(sender,"atsomeone.command.reload")){
                                sender.sendMessage("§cYou don't have permission.");
                                return true;
                            }
                            plugin.reloadConfig();
                            sender.sendMessage("AtSomeone Reloaded");
                            return true;
                    }
                case 2:
                    switch (args[0]){
                        case "ignore":
                            if (!(sender instanceof Player)) {
                                sender.sendMessage("§cOnly player can use this command!");
                                return true;
                            }
                            if(!CheckPermissions.hasPermissions(sender,"atsomeone.command.ignore")){
                                sender.sendMessage("§cYou don't have permission.");
                                return true;
                            }
                            FileConfiguration config = plugin.getConfig();
                            HashMapLoadAndSave hashConfig = new HashMapLoadAndSave();
                            HashMap<Object, Object> hm = hashConfig.loadHashMap(config,"playerIgnore");
                            List<String> ignoreList = (List<String>) hm.get(sender.getName());

                            if(ignoreList==null)ignoreList = new ArrayList<>();
                            for (String i : ignoreList) {
                                if (i.equals(args[1])) {
                                    ignoreList.remove(args[1]);
                                    sender.sendMessage("Success to remove " + args[1] + " from you ignore list");
                                    hm.put(sender.getName(), ignoreList);
                                    hashConfig.saveHashMap(config,plugin,"playerIgnore",hm);
                                    return true;
                                }
                            }

                            ignoreList.add(args[1]);
                            sender.sendMessage("Success ignore "+args[1]+".");
                            hm.put(sender.getName(),ignoreList);
                            hashConfig.saveHashMap(config,plugin,"playerIgnore",hm);
                            return true;
                    }
                default:
                    return false;
            }
        }
    };


}
