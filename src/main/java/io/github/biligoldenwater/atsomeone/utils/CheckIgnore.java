package io.github.biligoldenwater.atsomeone.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckIgnore {
    public static boolean isIgnore(HashMap<Object, Object> hm, Player playerBeAt, Player initiatedPlayer) {
        List<String> ignoreList = (List<String>) hm.get(playerBeAt.getName());
        boolean canAdd = false;

        if (ignoreList == null) ignoreList = new ArrayList<>();
        if (ignoreList.isEmpty()) {
            ignoreList.add("[holder]");
        }

        for (String playerInIgnoreList : ignoreList) {
            if (playerInIgnoreList.equalsIgnoreCase(initiatedPlayer.getName()) && !CheckPermissions.hasPermissions(initiatedPlayer, "atsomeone.bypass.ignore")) {
                canAdd = true;
            }
        }

        return canAdd;
    }
}
