package net.VrikkaDuck.AutoTrade.mixin.client.villager;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.render.RenderUtils;
import net.VrikkaDuck.AutoTrade.Variables;
import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.config.Hotkeys;
import net.VrikkaDuck.AutoTrade.villager.VillagerTrade;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MerchantScreen.class)

public abstract class MerchantScreenMixin extends HandledScreen<MerchantScreenHandler> {

    private static final Identifier DUCK_TEXTURE = new Identifier(Variables.MODID,"textures/duck.png");
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/villager2.png");

    public MerchantScreenMixin(MerchantScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir)
    {
        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }
        if(Hotkeys.ADD_TRADE.getKeybind().isKeybindHeld()){
            int visibleIndex = this.getHoveredTradeButtonIndex(mouseX, mouseY);
            TradeOffer offer = this.handler.getRecipes().get(visibleIndex);

            if(VillagerUtils.currentVillager == null){
                return;
            }

            VillagerTrade trade = new VillagerTrade(offer, VillagerUtils.currentVillager.getVillagerData().getProfession());

            VillagerUtils.AddTradeToList(trade);
        }
    }
    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, int x, int y, float d, CallbackInfo info){
        if(FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            VillagerUtils.drawMessages(matrices);
        }
        if(VillagerUtils.shouldClose){
            VillagerUtils.shouldClose = false;
            ((MerchantScreen) (Object) this).close();
        }
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo info){
        VillagerUtils.currentScreen = ((MerchantScreen) (Object)this);
    }
    private int getHoveredTradeButtonIndex(double mouseX, double mouseY)
    {
        int screenX = (this.width - this.backgroundWidth) / 2;
        int screenY = (this.height - this.backgroundHeight) / 2;
        int buttonsStartX = screenX + 5;
        int buttonsStartY = screenY + 16 + 2;
        int buttonWidth = 89;
        int buttonHeight = 20;

        if (mouseX >= buttonsStartX && mouseX <= buttonsStartX + buttonWidth &&
                mouseY >= buttonsStartY && mouseY <= buttonsStartY + 7 * buttonHeight)
        {
            return (((int) mouseY - buttonsStartY) / buttonHeight);
        }

        return -1;
    }

    @Inject(method = "renderArrow", at = @At("HEAD"), cancellable = true)
    private void renderArrow(MatrixStack matrices, TradeOffer offer, int x, int y, CallbackInfo info){

        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }

        if(!VillagerUtils.tradeListContains(offer)){
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DUCK_TEXTURE);
        if (offer.isDisabled()) {
            drawTexture(matrices, x + 3 + 35 + 20+2, y+2, this.getZOffset(), 12, 0, 12, 12, 24, 12);
        } else {
            drawTexture(matrices, x + 3 + 35 + 20+1, y+2, this.getZOffset(), 0, 0, 12, 12, 24, 12);
        }
        RenderSystem.setShaderTexture(0, TEXTURE);
        info.cancel();
    }
}
