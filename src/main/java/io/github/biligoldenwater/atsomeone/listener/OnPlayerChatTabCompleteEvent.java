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
        String token = event.getLastToken();
        if (token.startsWith("@") || token.startsWith("[@")) {
            Collection<String> completions = event.getTabCompletions();

            if (CheckPermissions.hasPermissions(event.getPlayer(), "atsomeone.atall") && ("[@All]".toLowerCase().startsWith(token.toLowerCase()) || "@All]".toLowerCase().startsWith(token.toLowerCase()))) {
                completions.add("[@All]");
            }
            for (Player i : Bukkit.getOnlinePlayers()) {
                String completion = "[@" + i.getName().toLowerCase() + "]";
                if (completion.startsWith(token.toLowerCase()) || completion.substring(1).startsWith(token.toLowerCase())) {
                    completions.add(completion);
                }
            }
        }
    }
}
