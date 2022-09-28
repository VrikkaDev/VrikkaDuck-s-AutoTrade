package net.VrikkaDuck.AutoTrade.input;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import net.VrikkaDuck.AutoTrade.Variables;
import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.config.Hotkeys;

public class KeyboardHandler implements IKeybindProvider, IKeyboardInputHandler {
    public KeyboardHandler(){
        super();
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (FeatureToggle toggle : FeatureToggle.values()) {
            manager.addKeybindToMap(toggle.getKeybind());
        }
        for (IHotkey hotkey : Hotkeys.HOTKEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
       manager.addHotkeysForCategory(Variables.MODNAME, "vrikkaducks-autotrade.hotkeys.category.generic_hotkeys", Hotkeys.HOTKEY_LIST);
        manager.addHotkeysForCategory(Variables.MODNAME, "vrikkaducks-autotrade.hotkeys.category.tweak_toggle_hotkeys", ImmutableList.copyOf(FeatureToggle.values()));
    }

    @Override
    public boolean onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState)
    {
        return false;
    }
}
