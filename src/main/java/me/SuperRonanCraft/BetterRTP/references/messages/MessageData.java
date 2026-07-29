package me.SuperRonanCraft.BetterRTP.references.messages;

import me.SuperRonanCraft.BetterRTP.references.file.FileData;
import org.bukkit.command.CommandSender;

public interface MessageData {

    String section();

    String prefix();

    FileData file();

    default String get() {
        return file().getString(prefix() + section());
    }

    default FileData file(CommandSender sender) {
        return file();
    }

    default String get(CommandSender sender) {
        return file(sender).getString(prefix() + section());
    }
}
