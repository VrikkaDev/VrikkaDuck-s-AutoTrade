package net.VrikkaDuck.AutoTrade.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

public class Hotkeys {

    public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui","Z,C",  "The key open the in-game config GUI");
    public static final ConfigHotkey ADD_TRADE = new ConfigHotkey("addTrade", "LEFT_CONTROL", KeybindSettings.MODIFIER_GUI,"Press this and click trade to add it to Trades list");
    public static final ConfigHotkey TRADE = new ConfigHotkey("trade", "G",
            KeybindSettings.create(KeybindSettings.Context.ANY, KeyAction.PRESS, true, false, false, true),
            "Trades with villagers");
    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
            ADD_TRADE,
            TRADE
    );
}
