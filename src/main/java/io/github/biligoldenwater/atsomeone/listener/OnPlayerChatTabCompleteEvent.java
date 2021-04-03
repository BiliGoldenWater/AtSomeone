package io.github.biligoldenwater.atsomeone.listener;


import io.github.biligoldenwater.atsomeone.utils.CheckPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import java.util.Collection;

public class OnPlayerChatTabCompleteEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
        String token = event.getLastToken().toLowerCase();
        if (token.startsWith("@") || token.startsWith("[@")) {
            Collection<String> completions = event.getTabCompletions();

            if (CheckPermissions.hasPermissions(event.getPlayer(), "atsomeone.atall") &&
                    ("[@all]".startsWith(token) ||
                            "@all]".startsWith(token))) {
                completions.add("[@All]");
            }

            for (Player i : Bukkit.getOnlinePlayers()) {
                String playerNameL = i.getName().toLowerCase();
                String completion = "[@" + playerNameL + "]".toLowerCase();
                if (completion.startsWith(token) || completion.substring(1).startsWith(token)) {
                    completions.add("[@" + i.getName() + "]");
                }
            }
        }
    }
}
