package net.VrikkaDuck.AutoTrade.mixin.client.villager;

import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.fabricmc.fabric.impl.screenhandler.Networking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @Inject(method = "beginTradeWith", at = @At("RETURN"))
    private void beginTradeWith(PlayerEntity customer, CallbackInfo info){
        VillagerUtils.currentVillager = ((VillagerEntity)(Object)this);
        VillagerUtils.updateLocalValues();
    }
}
