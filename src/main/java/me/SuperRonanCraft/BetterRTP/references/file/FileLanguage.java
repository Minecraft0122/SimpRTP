package me.SuperRonanCraft.BetterRTP.references.file;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FileLanguage implements FileData {
    private static final String LANGUAGE_KEY = "language";
    private static final String AUTO_LANGUAGE = "auto";

    private volatile YamlConfiguration config = new YamlConfiguration();
    private volatile Map<String, FileData> languages = Collections.emptyMap();
    private volatile String defaultLanguage = "en";
    private final Map<UUID, String> playerLanguages = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerLocales = new ConcurrentHashMap<>();
    private NamespacedKey playerLanguageKey;

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public String fileName() {
        return null;
    }

    @Override
    public Plugin plugin() {
        return BetterRTP.getInstance();
    }

    @Override
    public void load() {
        generateDefaults();

        Map<String, FileData> loadedLanguages = new LinkedHashMap<>();
        for (String yaml : defaultLangs) {
            File file = new File(plugin().getDataFolder(), resourcePath(yaml));
            loadLanguage(loadedLanguages, file);
        }

        File languageDirectory = new File(plugin().getDataFolder(), "lang");
        File[] customLanguages = languageDirectory.listFiles((directory, name) ->
                name.toLowerCase(Locale.ROOT).endsWith(".yml"));
        if (customLanguages != null) {
            Arrays.sort(customLanguages, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
            for (File file : customLanguages)
                if (!loadedLanguages.containsKey(languageCode(file.getName())))
                    loadLanguage(loadedLanguages, file);
        }

        languages = Collections.unmodifiableMap(loadedLanguages);
        String configured = canonicalLanguage(FileOther.FILETYPE.CONFIG.getString("Language-File"));
        defaultLanguage = configured == null ? "en" : configured;
        FileData configuredLanguage = languages.get(defaultLanguage);
        if (configuredLanguage == null)
            configuredLanguage = languages.get("en");
        if (configuredLanguage != null)
            config = configuredLanguage.getConfig();

        for (Player player : Bukkit.getOnlinePlayers())
            loadPlayer(player);
    }

    public FileData getLanguage(@Nullable CommandSender sender) {
        FileData language = languages.get(getLanguageCode(sender));
        return language == null ? this : language;
    }

    public String getLanguageCode(@Nullable CommandSender sender) {
        if (!(sender instanceof Player player))
            return defaultLanguage;

        String preference = getPlayerPreference(player);
        if (AUTO_LANGUAGE.equals(preference))
            return languageFromLocale(playerLocales.get(player.getUniqueId()));
        return languages.containsKey(preference) ? preference : defaultLanguage;
    }

    public String getPlayerPreference(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId(), defaultLanguage);
    }

    public void loadPlayer(Player player) {
        String preference = player.getPersistentDataContainer().get(languageKey(), PersistentDataType.STRING);
        playerLocales.put(player.getUniqueId(), player.getLocale());
        if (preference == null)
            playerLanguages.remove(player.getUniqueId());
        else
            playerLanguages.put(player.getUniqueId(), preference);
    }

    public void unloadPlayer(Player player) {
        playerLanguages.remove(player.getUniqueId());
        playerLocales.remove(player.getUniqueId());
    }

    public void updatePlayerLocale(Player player, String locale) {
        playerLocales.put(player.getUniqueId(), locale);
    }

    public boolean setPlayerLanguage(Player player, String input) {
        String language = canonicalLanguage(input);
        if (language == null && !AUTO_LANGUAGE.equalsIgnoreCase(input))
            return false;
        player.getPersistentDataContainer().set(languageKey(), PersistentDataType.STRING,
                language == null ? AUTO_LANGUAGE : language);
        playerLanguages.put(player.getUniqueId(), language == null ? AUTO_LANGUAGE : language);
        return true;
    }

    public void resetPlayerLanguage(Player player) {
        player.getPersistentDataContainer().remove(languageKey());
        playerLanguages.remove(player.getUniqueId());
    }

    @Nullable
    public String canonicalLanguage(String input) {
        if (input == null)
            return null;
        String code = input.toLowerCase(Locale.ROOT).replace('-', '_');
        if (code.endsWith(".yml"))
            code = code.substring(0, code.length() - 4);
        if (languages.containsKey(code))
            return code;

        String alias = localeAliases.get(code);
        if (alias != null && languages.containsKey(alias))
            return alias;
        return null;
    }

    public List<String> getAvailableLanguageCodes() {
        return new ArrayList<>(languages.keySet());
    }

    public String getDefaultLanguageCode() {
        return defaultLanguage;
    }

    private String languageFromLocale(String locale) {
        if (locale == null || locale.isBlank())
            return defaultLanguage;
        String normalized = locale.toLowerCase(Locale.ROOT).replace('-', '_');
        String exact = canonicalLanguage(normalized);
        if (exact != null)
            return exact;

        int separator = normalized.indexOf('_');
        String base = separator < 0 ? normalized : normalized.substring(0, separator);
        String mapped = localeAliases.getOrDefault(base, base);
        return languages.containsKey(mapped) ? mapped : defaultLanguage;
    }

    private synchronized NamespacedKey languageKey() {
        if (playerLanguageKey == null)
            playerLanguageKey = new NamespacedKey(plugin(), LANGUAGE_KEY);
        return playerLanguageKey;
    }

    private final String[] defaultLangs = {
            "en.yml", // English - KEEP AS FIRST IN THE LIST
            "br.yml", // Portuguese
            "chs.yml", // Chinese Simplified (OasisAkari)
            "cht.yml", // Chinese Traditional (OasisAkari & kamiya10)
            "cs.yml", // Czech (Lewisparkle)
            "da.yml", // Danish (Janbchr)
            "de.yml", // German (IBimsDaNico#8690)
            "es.yml", // Spanish (emgv)
            "fr.yml", // French (At0micA55 & Mrflo67)
            "he.yml", // Hebrew (thefourcraft)
            "hu.yml", // Hungarian (Has-X)
            "it.yml", // Italian (iVillager)
            "ja.yml", // Japanese (ViaSnake)
            "nl.yml", // Dutch (QuestalNetwork) (GeleVla)
            "no.yml", // Norwegian (Fraithor & Janbchr)
            "pl.yml", // Polish (Farum & TeksuSiK)
            "ro.yml", // Romanian (GamingXBlood)
            "ru.yml", // Russian (Logan)
            "tr.yml", // Turkish (Erissos)
            "uk.yml", // Ukrainian
            "vi.yml", // Vietnamese (VoChiDanh#0862)
    };

    private static final Map<String, String> localeAliases = Map.ofEntries(
            Map.entry("pt", "br"),
            Map.entry("pt_br", "br"),
            Map.entry("zh", "chs"),
            Map.entry("zh_cn", "chs"),
            Map.entry("zh_sg", "chs"),
            Map.entry("zh_tw", "cht"),
            Map.entry("zh_hk", "cht"),
            Map.entry("zh_mo", "cht"),
            Map.entry("nb", "no"),
            Map.entry("nb_no", "no"),
            Map.entry("nn", "no")
    );

    private void generateDefaults() {
        for (String yaml : defaultLangs) {
            generateDefaultConfig(yaml, yaml);
            if (!yaml.equals(defaultLangs[0]))
                generateDefaultConfig(yaml, defaultLangs[0]);
        }
    }

    private void loadLanguage(Map<String, FileData> loadedLanguages, File file) {
        try {
            YamlConfiguration languageConfig = new YamlConfiguration();
            languageConfig.load(file);
            try (InputStream in = plugin().getResource(resourcePath(defaultLangs[0]))) {
                if (in != null) {
                    languageConfig.setDefaults(YamlConfiguration.loadConfiguration(
                            new InputStreamReader(in, StandardCharsets.UTF_8)));
                    languageConfig.options().copyDefaults(true);
                }
            }
            loadedLanguages.put(languageCode(file.getName()),
                    new LoadedLanguage(languageConfig, file, "lang/" + file.getName()));
        } catch (Exception e) {
            plugin().getLogger().warning("Unable to load language file " + file.getName());
            e.printStackTrace();
        }
    }

    private void generateDefaultConfig(String fileName, String defaultsFileName) {
        String resourcePath = resourcePath(fileName);
        File file = new File(plugin().getDataFolder(), resourcePath);
        if (!file.exists())
            plugin().saveResource(resourcePath, false);
        try {
            YamlConfiguration languageConfig = new YamlConfiguration();
            languageConfig.load(file);
            try (InputStream in = plugin().getResource(resourcePath(defaultsFileName))) {
                if (in != null) {
                    languageConfig.setDefaults(YamlConfiguration.loadConfiguration(
                            new InputStreamReader(in, StandardCharsets.UTF_8)));
                    languageConfig.options().copyDefaults(true);
                }
            }
            languageConfig.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String resourcePath(String fileName) {
        return "lang/" + fileName;
    }

    private static String languageCode(String fileName) {
        return fileName.substring(0, fileName.length() - 4).toLowerCase(Locale.ROOT);
    }

    private class LoadedLanguage implements FileData {
        private final YamlConfiguration languageConfig;
        private final File file;
        private final String fileName;

        private LoadedLanguage(YamlConfiguration languageConfig, File file, String fileName) {
            this.languageConfig = languageConfig;
            this.file = file;
            this.fileName = fileName;
        }

        @Override
        public YamlConfiguration getConfig() {
            return languageConfig;
        }

        @Override
        public File getFile() {
            return file;
        }

        @Override
        public String fileName() {
            return fileName;
        }

        @Override
        public Plugin plugin() {
            return FileLanguage.this.plugin();
        }
    }
}
