package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandType;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

class Interact {

    private boolean enabled;
    private Component coloredTitle;
    private static final LegacyComponentSerializer LEGACY_TEXT = LegacyComponentSerializer.legacyAmpersand();
    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

    void load() {
        String pre = "Settings.";
        FileOther.FILETYPE file = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.SIGNS);
        enabled = file.getBoolean(pre + "Enabled");
        String title = file.getString(pre + "Title");
        coloredTitle = LEGACY_TEXT.deserialize(title == null ? "[RTP]" : title);
    }

    void event(PlayerInteractEvent e) {
        if (enabled && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && isSign(e.getClickedBlock())) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            SignSide front = sign.getSide(Side.FRONT);
            if (front.line(0).equals(coloredTitle)) {
                String[] lines = plainLines(front.lines());
                String action = cmd(lines);
                String command = lines[1].split(" ")[0];
                if (action.isBlank() || action.split(" ")[0].equalsIgnoreCase("srtp")) {
                    action(e.getPlayer(), null);
                    return;
                } else
                    for (RTPCommandType cmd : RTPCommandType.values())
                        if (command.equalsIgnoreCase(cmd.name())) {
                            action(e.getPlayer(), action.split(" "));
                            return;
                        }
                Message_RTP.sms(e.getPlayer(), "&cError! &7Command &a"
                        + Arrays.toString(action.split(" ")) + "&7 does not exist! Defaulting command to /srtp!");
            }
        }
    }

    void createSign(SignChangeEvent e) {
        if (enabled && PermissionNode.SIGN_CREATE.check(e.getPlayer())) {
            String line = PLAIN_TEXT.serialize(e.line(0));
            String plainTitle = PLAIN_TEXT.serialize(coloredTitle);
            if (line.equalsIgnoreCase(plainTitle) || line.equalsIgnoreCase("[RTP]")) {
                e.line(0, coloredTitle);
                MessagesCore.SIGN.send(e.getPlayer(), cmd(plainLines(e.lines())));
            }
        }
    }

    private void action(Player p, String[] line) {
        BetterRTP.getInstance().getCmd().commandExecuted(p, "srtp", line);
    }

    private static String cmd(String[] signArray) {
        String actions = "";
        for (int i = 1; i < signArray.length; i++) {
            String line = signArray[i];
            if (line != null && !line.equals(""))
                if (actions.equals(""))
                    actions = line;
                else
                    actions = actions.concat(" " + line);
        }
        return actions;
    }

    private static String[] plainLines(java.util.List<Component> lines) {
        return lines.stream().map(PLAIN_TEXT::serialize).toArray(String[]::new);
    }

    private static boolean isSign(Block block) {
        return block.getState() instanceof Sign;
    }
}
