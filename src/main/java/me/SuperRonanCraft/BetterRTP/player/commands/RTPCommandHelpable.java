package me.SuperRonanCraft.BetterRTP.player.commands;

import org.bukkit.command.CommandSender;

public interface RTPCommandHelpable {

    default String getHelp() {
        return getHelp(null);
    }

    default String getHelp(CommandSender sender) {
        return getHelp();
    }
}
