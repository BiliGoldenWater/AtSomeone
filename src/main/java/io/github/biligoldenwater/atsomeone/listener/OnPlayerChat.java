package io.github.biligoldenwater.atsomeone.listener;

import io.github.biligoldenwater.atsomeone.utils.HashMapLoadAndSave;
import io.github.biligoldenwater.atsomeone.utils.CheckIgnore;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getPluginManager;

public class OnPlayerChat implements org.bukkit.event.Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        //Start variable initialize
        Plugin plugin = getPluginManager().getPlugin("AtSomeone");

        FileConfiguration config = plugin.getConfig();
        HashMapLoadAndSave hashConfig = new HashMapLoadAndSave();
        HashMap<Object, Object> hm = hashConfig.loadHashMap(config, "playerIgnore");

        String message = e.getMessage();
        StringBuilder finalMessage = new StringBuilder("\0");

        List<Player> atPlayer = new ArrayList<>();
        Collection<? extends Player> onlinePlayers = getOnlinePlayers();
        String[] strs = message.split("[\\[\\]]");

        boolean isAtAll = false;
        boolean hasAt = false;
        //Finish variable initialize

        for (int i = 0; i < strs.length; ++i) {
            if (strs[i].equalsIgnoreCase("@All") && e.getPlayer().hasPermission("atsomeone.atall")) {
                isAtAll = true;
                strs[i] = "§d§l" + strs[i] + "§r";
                for (Player player : onlinePlayers) {
                    if (!CheckIgnore.isIgnore(hm, player, e.getPlayer())) {
                        atPlayer.add(player);
                        strs[i] = "§b§l" + strs[i] + "§r";
                        hasAt = true;
                    }
                }
            }
        }

        if (!isAtAll) {
            for (int i = 0; i < strs.length; ++i) {
                for (Player player : onlinePlayers) {
                    if (strs[i].equalsIgnoreCase("@" + player.getName())) {
                        if (!CheckIgnore.isIgnore(hm, player, e.getPlayer())) {
                            atPlayer.add(player);
                            strs[i] = "§b§l" + strs[i] + "§r";
                        } else {
                            strs[i] = "§c§m" + strs[i] + "§r";
                        }
                        hasAt = true;
                    }
                }
            }
        }

        for (String str : strs) {
            if (finalMessage.toString().equals("\0")) {
                finalMessage = new StringBuilder(str);
            } else {
                finalMessage.append(str);
            }
        }

        if (hasAt) {
            e.setMessage(finalMessage.toString());
            for (Player atPlayer2 : atPlayer) {
                if (strs.length > 1) {
                    atPlayer2.sendTitle("§b你被§d" + e.getPlayer().getName() + "§b@了",
                            "§d" + e.getPlayer().getName() + "§b@了你并对你说: " + finalMessage,
                            10, 80, 10);
                } else {
                    atPlayer2.sendTitle("§b你被§d" + e.getPlayer().getName() + "§b@了",
                            "§d" + e.getPlayer().getName() + "§b@了你.",
                            10, 80, 10);
                }

                atPlayer2.sendMessage("§b你被§d" + e.getPlayer().getName() + "§b@了. 打开聊天栏输入@按Tab即可使用@.");
                new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        atPlayer2.playNote(atPlayer2.getLocation(), Instrument.BELL, new Note(6));
                        count++;
                        if (count > 2) cancel();
                    }
                }.runTaskTimerAsynchronously(
                        Objects.requireNonNull(getPluginManager().getPlugin("AtSomeone")),
                        0,
                        2);
            }
        }
    }
}
