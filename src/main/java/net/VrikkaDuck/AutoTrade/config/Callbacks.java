package net.VrikkaDuck.AutoTrade.config;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.VrikkaDuck.AutoTrade.gui.ConfigGui;
import net.minecraft.client.MinecraftClient;

public class Callbacks {
    public static void init(MinecraftClient mc){
        IHotkeyCallback callbackGeneric = new KeyCallbackHotkeysGeneric(mc);

        Hotkeys.OPEN_CONFIG_GUI.getKeybind().setCallback(callbackGeneric);
    }

    private static class KeyCallbackHotkeysGeneric implements IHotkeyCallback {
        private final MinecraftClient mc;

        public KeyCallbackHotkeysGeneric(MinecraftClient mc) {
            this.mc = mc;
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if(key == Hotkeys.OPEN_CONFIG_GUI.getKeybind()){
                GuiBase.openGui(new ConfigGui());

                return true;
            }

            return false;
        }
    }
}
