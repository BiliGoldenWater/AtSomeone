package io.github.biligoldenwater.atsomeone.listener;

import io.github.biligoldenwater.atsomeone.AtSomeone;
import io.github.biligoldenwater.atsomeone.utils.I18nManager;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckIgnore.isIgnore;

public class OnPlayerChat implements org.bukkit.event.Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        AtSomeone plugin = AtSomeone.getInstance(); // 插件实例

        Configuration config = plugin.getConfig(); // 配置文件
        Server server = plugin.getServer(); // 服务器实例

        I18nManager i18n = plugin.getI18nManager(); // 国际化管理器实例
        String lang = config.getString("pluginLanguage"); // 获取插件语言

        Player player = e.getPlayer(); // 玩家实例

        String[] strings = e.getMessage().split("[\\[\\]]"); // 分割
        List<String> stringsLowerCase = Arrays.asList(
                e.getMessage().toLowerCase().split("[\\[\\]]")); // 分割并转为全小写

        if (stringsLowerCase.contains("@all") &&
                player.hasPermission("atsomeone.atall")) { // 如果包含有@all 并且有权限
            for (int i = 0; i < stringsLowerCase.size(); i++) { // 处理@all
                String str = stringsLowerCase.get(i);

                if (str.equalsIgnoreCase("@all")) { // 如果为@all
                    strings[i] = i18n.getL10n(lang, "msg_atAll"); // 替换为@all消息
                } else if (str.startsWith("@")) { // 否则 如果为@消息
                    strings[i] = invalidAt(config, str, i18n, lang); // 替换为无效@消息
                }
            }
            e.setMessage(atAlert(server.getOnlinePlayers(), player.getDisplayName(), strings, config, i18n, lang)); // 设置为替换后的消息并提醒
        } else if (player.hasPermission("atsomeone.at")) { // 如果有@的权限
            List<Player> atList = new ArrayList<>(); // 被@的玩家列表

            for (int i = 0; i < stringsLowerCase.size(); i++) { // 遍历所有被分割的字符串
                String str = stringsLowerCase.get(i);

                if (str.length() > 1) {
                    Player playerBeAt = server.getPlayer(str.substring(1)); // 尝试获取玩家实例

                    if (playerBeAt != null) { // 如果不为null
                        if (isIgnore(config.getStringList("playerIgnore." + playerBeAt.getName()), player) ||
                                atList.contains(playerBeAt)) { // 如果被忽略 或 已加入被@的列表
                            strings[i] = invalidAt(config, str, i18n, lang); // 替换为无效@消息
                        } else { // 否则
                            String atContent = "@" + playerBeAt.getDisplayName(); // 获取@内容
                            strings[i] = i18n.getL10n(lang, "msg_validAt")
                                    .replace("{{atContent}}", atContent); // 替换为有效@消息
                            atList.add(playerBeAt); // 添加至被@列表
                        }
                    } else {
                        strings[i] = invalidAt(config, str, i18n, lang); // 替换为无效@消息
                    }
                }
            }

            e.setMessage(atAlert(atList, player.getDisplayName(), strings, config, i18n, lang)); // 设置消息并提醒
        }


    }

    public String invalidAt(Configuration config, String atContent, I18nManager i18n, String lang) {
        if (config.getBoolean("deleteInvalidAtInMessage")) {
            return "";
        } else {
            return i18n.getL10n(lang, "msg_invalidAt")
                    .replace("{{atContent}}", atContent);
        }
    }

    private String atAlert(Collection<? extends Player> players,
                           String fromPlayerName,
                           String[] message,
                           Configuration config, I18nManager i18n,
                           String lang) {
        StringBuilder finalMessage = new StringBuilder();

        for (String str : message) {
            finalMessage.append(str);
        }

        for (Player player : players) {
            String msg_atAlert_title = i18n.getL10n(lang, "msg_atAlert_title")
                    .replace("{{fromPlayerName}}", fromPlayerName);
            String msg_atAlert_subtitle = i18n.getL10n(lang, "msg_atAlert_subtitle")
                    .replace("{{fromPlayerName}}", fromPlayerName);
            String msg_atAlert_chat = i18n.getL10n(lang, "msg_atAlert_chat")
                    .replace("{{fromPlayerName}}", fromPlayerName);
            int fadeIn = config.getInt("alertTitle.fadeIn");
            int stay = config.getInt("alertTitle.stay");
            int fadeOut = config.getInt("alertTitle.fadeOut");

            player.sendTitle(msg_atAlert_title,
                    msg_atAlert_subtitle,
                    fadeIn, stay, fadeOut);

            player.sendMessage(msg_atAlert_chat);
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    player.playNote(player.getLocation(), Instrument.BELL, new Note(6));
                    count++;
                    if (count > 2) cancel();
                }
            }.runTaskTimerAsynchronously(
                    AtSomeone.getInstance(),
                    0,
                    2);
        }

        return finalMessage.toString();
    }
}