package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Locale;

public class RTPEffect_Sounds {

    private boolean enabled;
    private Sound soundTeleport, soundDelay;

    void load() {
        FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
        enabled = config.getBoolean("Sounds.Enabled");
        if (enabled) {
            soundTeleport = parseSound(config.getString("Sounds.Success"));
            soundDelay = parseSound(config.getString("Sounds.Delay"));
        }
    }

    public void playTeleport(Player p) {
        if (!enabled)
            return;
        if (soundTeleport != null)
            p.playSound(p.getLocation(), soundTeleport, 1F, 1F);
    }

    public void playDelay(Player p) {
        if (!enabled) return;
        if (soundDelay != null)
            p.playSound(p.getLocation(), soundDelay, 1F, 1F);
    }

    private Sound parseSound(String sound) {
        if (sound == null) {
            org.bukkit.Bukkit.getLogger().warning("[SimpRTP] Invalid sound in effects.yml: " + sound);
            return null;
        }
        String normalized = sound.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(normalized);
        Registry<Sound> sounds = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT);
        Sound value = key == null ? null : sounds.get(key);
        if (value == null && !normalized.contains(":"))
            value = sounds.get(NamespacedKey.minecraft(normalized.replace('_', '.')));
        if (value == null)
            org.bukkit.Bukkit.getLogger().warning("[SimpRTP] Invalid sound in effects.yml: " + sound);
        return value;
    }
}
