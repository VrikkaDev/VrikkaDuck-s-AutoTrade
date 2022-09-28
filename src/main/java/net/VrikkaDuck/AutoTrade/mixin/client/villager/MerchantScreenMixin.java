package net.VrikkaDuck.AutoTrade.mixin.client.villager;

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {

    @Shadow private final MerchantScreen.WidgetButtonPage[] offers;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo cbi){

        MerchantScreen cur = ((MerchantScreen)(Object)this);

        int i = (cur.width - cur.backgroundWidth) / 2;
        int j = (cur.height - cur.backgroundHeight) / 2;
        int k = j + 16 + 2;

        for(int l = 0; l < 7; ++l) {
            cur.offers[l] = (MerchantScreen.WidgetButtonPage)this.addDrawableChild(new MerchantScreen.WidgetButtonPage(i + 5, k, l, (button) -> {
                if (button instanceof MerchantScreen.WidgetButtonPage) {
                    cur.selectedIndex = ((MerchantScreen.WidgetButtonPage)button).getIndex() + cur.indexStartOffset;
                    cur.syncRecipeIndex();
                }

            }));
            k += 20;
        }
    }
}
