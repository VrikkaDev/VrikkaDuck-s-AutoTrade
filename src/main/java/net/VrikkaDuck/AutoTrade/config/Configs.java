package net.VrikkaDuck.AutoTrade.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.VrikkaDuck.AutoTrade.Variables;

import java.io.File;

public class Configs implements IConfigHandler {
    private static final String CONFIG_FILE_NAME = Variables.MODID + ".json";

    public static class Generic {
        public static final ConfigDouble MAX_VILLAGER_DISTANCE = new ConfigDouble("maxVillagerDistance", 3, 0, 5, "Max distance autotrade detects villager");
        public static final ConfigDouble TIME_BETWEEN_TRIES = new ConfigDouble("timeBetweenTries", 5000, 100, 99999, "Time in MS between trade tries");
        public static final ConfigDouble TIME_TO_TRADE = new ConfigDouble("timeToTrade", 150, 10, 10000, "Time in MS between opening merchant screen \n and trading \n (bigger number = more reliable in servers)");
        public static final ConfigInteger TRADE_TRIES = new ConfigInteger("tradeTries", 10, 1, 100, "How many times it tries to trade \n before moving on");

        public static ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                MAX_VILLAGER_DISTANCE,
                TIME_BETWEEN_TRIES,
                TIME_TO_TRADE,
                TRADE_TRIES
        );
    }

    public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Configs.Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Lists", Lists.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            }
        }
    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Lists", Lists.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
            ConfigUtils.writeHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
