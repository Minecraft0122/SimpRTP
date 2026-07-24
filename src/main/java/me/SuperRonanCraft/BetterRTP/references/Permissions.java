package me.SuperRonanCraft.BetterRTP.references;

import me.SuperRonanCraft.BetterRTP.references.depends.DepPerms;
import org.bukkit.command.CommandSender;

public class Permissions {

    private final DepPerms depPerms = new DepPerms();

    public void register() {
        depPerms.register();
    }

    public boolean checkPerm(String str, CommandSender sendi) {
        if (depPerms.hasPerm(str, sendi))
            return true;

        String prefix = PermissionCheck.getPrefix();
        if (str.startsWith(prefix)) {
            String legacy = PermissionCheck.getLegacyPrefix() + str.substring(prefix.length());
            return depPerms.hasPerm(legacy, sendi);
        }
        return false;
    }

}
