package io.github.biligoldenwater.atsomeone.listener;


import io.github.biligoldenwater.atsomeone.modules.CheckPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import java.util.Collection;

public class OnPlayerChatTabCompleteEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event){
        String token = event.getLastToken();
        if(token.startsWith("@") || token.startsWith("[@")){
            Collection<String> completions = event.getTabCompletions();;
            if(CheckPermissions.hasPermissions(event.getPlayer(),"atsomeone.atall") && ("[@All]".toLowerCase().startsWith(token.toLowerCase()) || "@All]".toLowerCase().startsWith(token.toLowerCase()))){
                completions.add("[@All]");
            }
            for(Player i: Bukkit.getOnlinePlayers()){
                String completion = "[@" + i.getName()+"]";
                if(completion.toLowerCase().startsWith(token.toLowerCase()) || completion.substring(1).toLowerCase().startsWith(token.toLowerCase())){
                    completions.add(completion);
                }
            }
        }
    }
}
