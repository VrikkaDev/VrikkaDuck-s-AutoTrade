package net.VrikkaDuck.AutoTrade.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.VrikkaDuck.AutoTrade.gui.ConfigGui;

public class ModMenuImpl implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return (screen) -> {
            ConfigGui gui = new ConfigGui();
            gui.setParent(screen);
            return gui;
        };
    }
}
