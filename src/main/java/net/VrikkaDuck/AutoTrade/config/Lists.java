package net.VrikkaDuck.AutoTrade.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;

public class Lists {

    public static final ConfigStringList TRADES_LIST = new ConfigStringList("trades", ImmutableList.of(), "trades");

    public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(TRADES_LIST);

}
