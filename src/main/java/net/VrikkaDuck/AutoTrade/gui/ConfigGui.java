package net.VrikkaDuck.AutoTrade.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.BooleanHotkeyGuiWrapper;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.VrikkaDuck.AutoTrade.Variables;
import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import com.google.common.collect.ImmutableList;
import net.VrikkaDuck.AutoTrade.config.Hotkeys;

import java.util.Collections;
import java.util.List;

public class ConfigGui extends GuiConfigsBase {

    public static ImmutableList<FeatureToggle> TWEAK_LIST = FeatureToggle.VALUES;

    private static ConfigGuiTab tab = ConfigGuiTab.GENERIC;

    public ConfigGui()
    {
        super(10, 50, Variables.MODID, null, "vrikkaducks-autotrade.gui.title.configs", String.format("%s", Variables.MODVERSION));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values())
        {
            x += this.createButton(x, y, -1, tab);
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(ConfigGui.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth()
    {
        ConfigGuiTab tab = ConfigGui.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            return 120;
        }
       /* else if (tab == ConfigGuiTab.FIXES)
        {
            return 60;
        }
        else if (tab == ConfigGuiTab.LISTS)
        {
            return 200;
        }*/

        return 260;
    }

    @Override
    protected boolean useKeybindSearch()
    {

        return false;
        /*return ConfigGui.tab == ConfigGuiTab.TWEAKS ||
                GuiConfigs.tab == ConfigGuiTab.GENERIC_HOTKEYS ||
                GuiConfigs.tab == ConfigGuiTab.DISABLES;*/
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs()
    {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = ConfigGui.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            configs = Configs.Generic.OPTIONS;
        }
        /*else if (tab == ConfigGuiTab.FIXES)
        {
            configs = Configs.Fixes.OPTIONS;
        }*/
        /*else if (tab == ConfigGuiTab.LISTS)
        {
            configs = Configs.Lists.OPTIONS;
        }*/
        else if (tab == ConfigGuiTab.TWEAKS)
        {
            return ConfigOptionWrapper.createFor(TWEAK_LIST.stream().map(this::wrapConfig).toList());
        }
       /* else if (tab == ConfigGuiTab.GENERIC_HOTKEYS)
        {
            configs = Hotkeys.HOTKEY_LIST;
        }*/
        else
        {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    protected BooleanHotkeyGuiWrapper wrapConfig(FeatureToggle config)
    {
        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
    }

    private static class ButtonListener implements IButtonActionListener
    {
        private final ConfigGui parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, ConfigGui parent)
        {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            ConfigGui.tab = this.tab;
            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    public enum ConfigGuiTab
    {
        GENERIC         ("vrikkaducks-autotrade.gui.button.config_gui.generic"),
       // FIXES           ("tweakeroo.gui.button.config_gui.fixes"),
       // LISTS           ("tweakeroo.gui.button.config_gui.lists"),
        TWEAKS          ("vrikkaducks-autotrade.gui.button.config_gui.tweaks");
        //GENERIC_HOTKEYS ("tweakeroo.gui.button.config_gui.generic_hotkeys"),
        //DISABLES        ("tweakeroo.gui.button.config_gui.disables");

        private final String translationKey;

        ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }

}
