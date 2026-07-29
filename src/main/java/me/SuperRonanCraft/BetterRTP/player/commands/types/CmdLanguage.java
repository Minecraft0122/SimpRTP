package me.SuperRonanCraft.BetterRTP.player.commands.types;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.file.FileLanguage;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesUsage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CmdLanguage implements RTPCommand, RTPCommandHelpable {

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            send(sender, "Messages.Language.PlayersOnly", null, null);
            return;
        }

        FileLanguage languages = languages();
        if (args.length == 1) {
            sendCurrent(player);
            return;
        }
        if (args.length != 2) {
            MessagesUsage.LANGUAGE.send(sender, label);
            return;
        }

        String requested = args[1].toLowerCase(Locale.ROOT);
        if (requested.equals("default") || requested.equals("reset") || requested.equals("server")) {
            languages.resetPlayerLanguage(player);
            send(player, "Messages.Language.Reset", "%language%", languages.getLanguageCode(player));
            return;
        }

        if (!languages.setPlayerLanguage(player, requested)) {
            send(player, "Messages.Language.Invalid", "%language%", requested);
            send(player, "Messages.Language.Available", "%languages%", availableLanguages());
            return;
        }

        send(player, "Messages.Language.Changed", "%language%", languages.getLanguageCode(player));
    }

    private void sendCurrent(Player player) {
        FileLanguage languages = languages();
        String message = Message_RTP.getLang(player).getString("Messages.Language.Current")
                .replace("%language%", languages.getLanguageCode(player))
                .replace("%preference%", languages.getPlayerPreference(player));
        Message_RTP.sms(player, message);
        send(player, "Messages.Language.Available", "%languages%", availableLanguages());
    }

    private void send(CommandSender sender, String path, String placeholder, String value) {
        String message = Message_RTP.getLang(sender).getString(path);
        if (placeholder != null)
            message = message.replace(placeholder, value);
        Message_RTP.sms(sender, message);
    }

    private String availableLanguages() {
        return String.join(", ", languages().getAvailableLanguageCodes());
    }

    private FileLanguage languages() {
        return BetterRTP.getInstance().getFiles().getLang();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 2)
            return null;

        String input = args[1].toLowerCase(Locale.ROOT);
        List<String> choices = new ArrayList<>(languages().getAvailableLanguageCodes());
        choices.add("auto");
        choices.add("default");
        choices.removeIf(choice -> !choice.startsWith(input));
        return choices;
    }

    @Override
    public @NotNull PermissionNode permission() {
        return PermissionNode.LANGUAGE;
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getHelp(CommandSender sender) {
        return MessagesHelp.LANGUAGE.get(sender);
    }
}
