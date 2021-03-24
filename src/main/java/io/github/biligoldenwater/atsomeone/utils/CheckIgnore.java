package io.github.biligoldenwater.atsomeone.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckPermissions.hasPermissions;

public class CheckIgnore {
    public static boolean isIgnore(HashMap<Object, Object> hm, Player playerBeAt, Player initiatedPlayer) {
        List<String> ignoreList = (List<String>) hm.get(playerBeAt.getName());
        boolean ignore = false;

        if (ignoreList == null) ignoreList = new ArrayList<>();

//        if (!ignoreList.isEmpty()) {
//
//        }

        for (String playerInIgnoreList : ignoreList) {
            boolean inIgnoreList = playerInIgnoreList.equalsIgnoreCase(initiatedPlayer.getName());
            boolean hasBypassPerm = hasPermissions(initiatedPlayer, "atsomeone.bypass.ignore");

            if (inIgnoreList && !hasBypassPerm) {
                ignore = true;
            }
        }


        return ignore;
    }
}
