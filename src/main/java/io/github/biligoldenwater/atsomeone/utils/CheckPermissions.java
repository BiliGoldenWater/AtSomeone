package io.github.biligoldenwater.atsomeone.utils;

import org.bukkit.command.CommandSender;

public final class CheckPermissions {
    public static boolean hasPermissions(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) return true;

        String[] nodes = SplitString.splitString(".", perm);
        int perm_long = nodes.length;

        while (perm_long > 1) {
            String perm2 = nodes[0];
            for (int i = 1; i < perm_long; ++i) {
                if (i == perm_long - 1) {
                    perm2 = perm2 + ".*";
                } else {
                    perm2 = perm2 + "." + nodes[i];
                }
            }
            if (sender.hasPermission(perm2)) return true;
            perm_long--;
        }

        return false;
    }
}
