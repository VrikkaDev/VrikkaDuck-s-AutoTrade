package net.VrikkaDuck.AutoTrade.mixin.client.networking;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.mixin.client.villager.VillagerEntityMixin;
import net.VrikkaDuck.AutoTrade.villager.VillagerBuyThread;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    private VillagerEntity vg;
    //How many times tried to trade with villager
    private int tries = 0;

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("RETURN"))
    private void send(Packet packet, CallbackInfo info){

        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }

        //When player opens villager gui sends SelectMerchantTrade packet
        if(packet instanceof PlayerInteractEntityC2SPacket){

            PlayerInteractEntityC2SPacket piePacket = (PlayerInteractEntityC2SPacket) packet;

            if(piePacket.getEntity(MinecraftClient.getInstance().getServer().getOverworld()) instanceof VillagerEntity){

                VillagerEntity villager = (VillagerEntity) piePacket.getEntity(MinecraftClient.getInstance().getServer().getOverworld());

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
            PlayerEntity player = MinecraftClient.getInstance().player;
            ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());

            if(VillagerUtils.currentScreen == null){
                return;
            }

            ClickSlotC2SPacket csPacket = new ClickSlotC2SPacket(VillagerUtils.currentScreen.getScreenHandler().syncId, 5, 2, 0, SlotActionType.QUICK_MOVE, vg.getOffers().get(smtPacket.getTradeId()).copySellItem(), map);
            ((ClientConnection) (Object) this).send(csPacket);
            //VillagerUtils.addVillagerToIgnoreList(villager);
        }else if(packet instanceof ClickSlotC2SPacket){

            if(this.vg == null || VillagerUtils.currentScreen == null || VillagerUtils.currentVillager == null){
                return;
            }

            TradeOfferList tradeOffers = vg.getOffers();
            int tradeID = VillagerUtils.getTradeId(tradeOffers);

            tries++;

            if(tries > 10){
                VillagerUtils.addVillagerToIgnoreList(vg);
                VillagerUtils.shouldClose = true;
                return;
            }
            if(tradeID == 69){
                VillagerUtils.shouldClose = true;
            }else{
                SelectMerchantTradeC2SPacket pk = new SelectMerchantTradeC2SPacket(tradeID);
                VillagerBuyThread thread = new VillagerBuyThread(pk, ((ClientConnection)(Object) this) );
                thread.start();
            }

        }
    }

}
