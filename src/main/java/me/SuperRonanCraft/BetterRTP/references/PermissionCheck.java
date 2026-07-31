package me.SuperRonanCraft.BetterRTP.references;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface PermissionCheck {

    static String getPrefix() {
        return "simprtp.";
    }

    default boolean check(CommandSender sendi) {
        return BetterRTP.getInstance().getPerms().checkPerm(getNode(), sendi);
    }

    static boolean check(CommandSender sendi, String check) {
        return BetterRTP.getInstance().getPerms().checkPerm(check, sendi);
    }

    static boolean getAWorld(CommandSender sendi, String world) {
        return getAWorldText(sendi, world).passed;
    }

    static PermissionResult getAWorldText(CommandSender sendi, @NotNull String world) {
        String perm = getPrefix() + "world.*";
        if (check(sendi, perm)) {
            return new PermissionResult(perm, true);
        } else {
            perm = getPrefix() + "world." + world;
            if (check(sendi, perm))
                return new PermissionResult(perm, true);
        }
        return new PermissionResult(perm, false);
    }

    static boolean getLocation(CommandSender sendi, String location) {
        return check(sendi, getPrefix() + "location." + location);
    }

    static boolean getPermissionGroup(CommandSender sendi, String group) {
        return check(sendi, getPrefix() + "group." + group);
    }

    String getNode();

    @Getter
    class PermissionResult {
        private final boolean passed;
        private final String string;
        PermissionResult(String string, boolean passed) {
            this.passed = passed;
            this.string = string;
        }
    }
}
