package net.VrikkaDuck.AutoTrade;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.VrikkaDuck.AutoTrade.config.Callbacks;
import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.input.KeyboardHandler;
import net.minecraft.client.MinecraftClient;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers(){
        KeyboardHandler keyboardHandler = new KeyboardHandler();

        InputEventHandler.getKeybindManager().registerKeybindProvider(keyboardHandler);
        InputEventHandler.getInputManager().registerKeyboardInputHandler(keyboardHandler);

        ConfigManager.getInstance().registerConfigHandler(Variables.MODID, new Configs());

        Callbacks.init(MinecraftClient.getInstance());
    }
}
