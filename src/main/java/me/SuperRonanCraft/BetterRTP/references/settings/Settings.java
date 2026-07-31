package me.SuperRonanCraft.BetterRTP.references.settings;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;

public class Settings {

    @Getter private boolean debug;
    @Getter private boolean delayEnabled;
    @Getter private int delayTime;
    @Getter private boolean rtpOnFirstJoin_Enabled;
    @Getter private String rtpOnFirstJoin_World;
    @Getter private boolean rtpOnFirstJoin_SetAsRespawn;
    @Getter private boolean statusMessages; //Send more information about rtp
    @Getter private boolean locationEnabled;
    @Getter private boolean useLocationIfAvailable;
    @Getter private boolean locationNeedPermission;
    @Getter private boolean useLocationsInSameWorld;
    @Getter private boolean permissionGroupEnabled;
    //Placeholders
    @Getter private String placeholder_true;
    @Getter private String placeholder_nopermission;
    @Getter private String placeholder_cooldown;
    @Getter private String placeholder_balance;
    @Getter private String placeholder_hunger;
    @Getter private String placeholder_timeDays;
    @Getter private String placeholder_timeHours;
    @Getter private String placeholder_timeMinutes;
    @Getter private String placeholder_timeSeconds;
    @Getter private String placeholder_timeZero;
    @Getter private String placeholder_timeInf;
    @Getter private String placeholder_timeSeparator_middle;
    @Getter private String placeholder_timeSeparator_last;


    public void load() { //Load Settings
        FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
        debug = config.getBoolean("Settings.Debugger");
        delayEnabled = config.getBoolean("Settings.Delay.Enabled");
        delayTime = config.getInt("Settings.Delay.Time");
        rtpOnFirstJoin_Enabled = config.getBoolean("Settings.RtpOnFirstJoin.Enabled");
        rtpOnFirstJoin_World = config.getString("Settings.RtpOnFirstJoin.World");
        rtpOnFirstJoin_SetAsRespawn = config.getBoolean("Settings.RtpOnFirstJoin.SetAsRespawn");
        statusMessages = config.getBoolean("Settings.StatusMessages");
        permissionGroupEnabled = config.getBoolean("PermissionGroup.Enabled");
        locationEnabled = FileOther.FILETYPE.LOCATIONS.getBoolean("Enabled");
        useLocationIfAvailable = FileOther.FILETYPE.LOCATIONS.getBoolean("UseLocationIfAvailable");
        locationNeedPermission = FileOther.FILETYPE.LOCATIONS.getBoolean("RequirePermission");
        useLocationsInSameWorld = FileOther.FILETYPE.LOCATIONS.getBoolean("UseLocationsInSameWorld");
        //Placeholders
        placeholder_true = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Success");
        placeholder_nopermission = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.NoPermission");
        placeholder_cooldown = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Cooldown");
        placeholder_balance = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Price");
        placeholder_hunger = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Hunger");
        placeholder_timeDays = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Days");
        placeholder_timeHours = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Hours");
        placeholder_timeMinutes = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Minutes");
        placeholder_timeSeconds = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Seconds");
        placeholder_timeZero = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.ZeroAll");
        placeholder_timeInf = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Infinite");
        placeholder_timeSeparator_middle = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Middle");
        placeholder_timeSeparator_last = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Last");
    }
}
