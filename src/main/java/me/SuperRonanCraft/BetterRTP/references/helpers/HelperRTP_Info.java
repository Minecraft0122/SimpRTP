package me.SuperRonanCraft.BetterRTP.references.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;

public class HelperRTP_Info {

    //Custom biomes
    public static List<String> getBiomes(String[] args, int start, CommandSender sendi) {
        List<String> biomes = new ArrayList<>();
        boolean error_sent = false;
        if (PermissionNode.BIOME.check(sendi))
            for (int i = start; i < args.length; i++) {
                String str = args[i];
                String name = str.replace(",", "").toLowerCase(Locale.ROOT);
                NamespacedKey key = NamespacedKey.fromString(name);
                if (key == null && !name.contains(":"))
                    key = NamespacedKey.minecraft(name);
                Registry<Biome> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
                Biome biome = key == null ? null : registry.get(key);
                if (biome != null) {
                    biomes.add(biome.getKey().getKey());
                } else {
                    if (!error_sent) {
                        MessagesCore.OTHER_BIOME.send(sendi, str);
                        error_sent = true;
                    }
                }
            }
        return biomes;
    }

    public static void addBiomes(List<String> list, String[] args) {
        String prefix = args[args.length - 1].toLowerCase();
        RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).stream()
                .map(biome -> biome.getKey().getKey())
                .filter(name -> name.startsWith(prefix))
                .forEach(list::add);
    }

}
