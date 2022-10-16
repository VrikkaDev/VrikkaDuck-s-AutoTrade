package net.VrikkaDuck.AutoTrade.utils;

import net.VrikkaDuck.AutoTrade.mixin.client.networking.IPlayerInteractEntityC2SPacketMixin;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class AccessorUtils {
    public static int getEntityIdFromPacket(PlayerInteractEntityC2SPacket packet){
        return ((IPlayerInteractEntityC2SPacketMixin)packet).autotrade_getEntityId();
    }
}
