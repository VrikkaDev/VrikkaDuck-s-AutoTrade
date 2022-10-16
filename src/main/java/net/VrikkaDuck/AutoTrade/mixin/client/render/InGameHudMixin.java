package net.VrikkaDuck.AutoTrade.mixin.client.render;

import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.villager.VillagerUtils;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo info){
        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }
        if(!(VillagerUtils.currentScreen == null)){
            VillagerUtils.drawMessages(matrices);
        }
    }
}
