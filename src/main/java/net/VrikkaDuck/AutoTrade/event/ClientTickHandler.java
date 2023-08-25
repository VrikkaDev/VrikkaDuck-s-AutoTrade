package net.VrikkaDuck.AutoTrade.event;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.VrikkaDuck.AutoTrade.villager.VillagerTweaks;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.minecraft.client.MinecraftClient;

public class ClientTickHandler implements IClientTickHandler {

    private final VillagerTweaks villagerTweaks = new VillagerTweaks();
    @Override
    public void onClientTick(MinecraftClient mc)
    {
        villagerTweaks.tick(mc);
    }
}
