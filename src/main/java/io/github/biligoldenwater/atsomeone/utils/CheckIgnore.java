package io.github.biligoldenwater.atsomeone.utils;

import org.bukkit.entity.Player;

import java.util.List;

import static io.github.biligoldenwater.atsomeone.utils.CheckPermissions.hasPermissions;

public class CheckIgnore {
    public static boolean isIgnore(List<String> atTosIgnoreList, Player atTo, Player atFrom) {
        if (hasPermissions(atFrom, "atsomeone.bypass.ignore")) {
            return false;
        }
        
        return atTosIgnoreList.contains(atTo.getName());
    }
}
