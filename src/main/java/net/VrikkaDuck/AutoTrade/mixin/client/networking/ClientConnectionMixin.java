package net.VrikkaDuck.AutoTrade.mixin.client.networking;

import io.netty.channel.Channel;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.VrikkaDuck.AutoTrade.Variables;
import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.config.Hotkeys;
import net.VrikkaDuck.AutoTrade.utils.AccessorUtils;
import net.VrikkaDuck.AutoTrade.villager.VillagerBuyThread;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.SocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Shadow private SocketAddress address;
    @Shadow private PacketListener packetListener;
    @Shadow private Channel channel;
    private VillagerEntity vg;
    //How many times tried to trade with villager
    private int tries = 0;

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void send(Packet<?> packet, CallbackInfo ci){

        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }

        if(packet instanceof PlayerInteractEntityC2SPacket) {

            PlayerInteractEntityC2SPacket piePacket = (PlayerInteractEntityC2SPacket) packet;

            if(MinecraftClient.getInstance().player == null){
                Variables.LOGGER.info("Player is null");
                return;
            }

            Entity entity = MinecraftClient.getInstance().world.getEntityById(AccessorUtils.getEntityIdFromPacket(piePacket));

            if(entity == null){
                Variables.LOGGER.info("Entity is null");
                return;
            }

            if (entity instanceof VillagerEntity) {

                VillagerEntity villager = (VillagerEntity) entity;

                VillagerUtils.currentVillager = villager;
                VillagerUtils.updateLocalValues();
            }
        }

        if(!Hotkeys.TRADE.getKeybind().isKeybindHeld()){
            return;
        }

        //When player opens villager gui sends SelectMerchantTrade packet
        if(packet instanceof PlayerInteractEntityC2SPacket){

            PlayerInteractEntityC2SPacket piePacket = (PlayerInteractEntityC2SPacket) packet;

            Entity entity = MinecraftClient.getInstance().world.getEntityById(AccessorUtils.getEntityIdFromPacket(piePacket));

            if(entity == null){
                Variables.LOGGER.info("Entity is null");
                return;
            }

            if(entity instanceof VillagerEntity){

                VillagerEntity villager = (VillagerEntity) entity;


                vg = villager;
                tries = 0;

                TradeOfferList tradeOffers = villager.getOffers();
                int tradeID = VillagerUtils.getTradeId(tradeOffers);

                    //VillagerUtils:public static int getTradeId
                    if(tradeID == 69){
                        VillagerUtils.addVillagerToIgnoreList(vg);
                        VillagerUtils.shouldClose = true;
                        return;
                    }

                SelectMerchantTradeC2SPacket pk = new SelectMerchantTradeC2SPacket(tradeID);
                VillagerBuyThread thread = new VillagerBuyThread(pk, ((ClientConnection)(Object) this) );
                thread.start();
            }

        }else if(packet instanceof SelectMerchantTradeC2SPacket){

            SelectMerchantTradeC2SPacket smtPacket = (SelectMerchantTradeC2SPacket) packet;

            Int2ObjectMap<ItemStack> map = new Int2ObjectArrayMap<ItemStack>();

            if(VillagerUtils.currentScreen == null){
                return;
            }

            ClickSlotC2SPacket csPacket = new ClickSlotC2SPacket(VillagerUtils.currentScreen.getScreenHandler().syncId, 5, 2, 0, SlotActionType.QUICK_MOVE, vg.getOffers().get(smtPacket.getTradeId()).copySellItem(), map);
            ((ClientConnection) (Object) this).send(csPacket);
        }else if(packet instanceof ClickSlotC2SPacket){

            if(this.vg == null || VillagerUtils.currentScreen == null || VillagerUtils.currentVillager == null){
                return;
            }

            TradeOfferList tradeOffers = vg.getOffers();
            int tradeID = VillagerUtils.getTradeId(tradeOffers);

            tries++;

            if(tries > Configs.Generic.TRADE_TRIES.getIntegerValue()){
                VillagerUtils.addVillagerToIgnoreList(vg);
                VillagerUtils.shouldClose = true;
                return;
            }
            if(tradeID == 69){//why am i so funny
                VillagerUtils.shouldClose = true;
            }else{
                SelectMerchantTradeC2SPacket pk = new SelectMerchantTradeC2SPacket(tradeID);
                VillagerBuyThread thread = new VillagerBuyThread(pk, ((ClientConnection)(Object) this) );
                thread.start();
            }

        }
    }

}
