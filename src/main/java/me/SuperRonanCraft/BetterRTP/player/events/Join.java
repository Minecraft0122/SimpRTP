package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join {

    static void event(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        getPl().getFiles().getLang().loadPlayer(p);
        getPl().getCooldowns().loadPlayer(p);
        rtpOnFirstJoin(p);
    }

    //RTP on first join
    private static void rtpOnFirstJoin(Player p) {
        if (getPl().getSettings().isRtpOnFirstJoin_Enabled() && !p.hasPlayedBefore())
            HelperRTP.tp(p, Bukkit.getConsoleSender(), Bukkit.getWorld(getPl().getSettings().getRtpOnFirstJoin_World()), null, RTP_TYPE.JOIN, true, true);
    }

    private static BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
