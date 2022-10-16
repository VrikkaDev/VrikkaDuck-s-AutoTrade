package net.VrikkaDuck.AutoTrade.villager;

import net.VrikkaDuck.AutoTrade.config.Configs;
import net.VrikkaDuck.AutoTrade.config.FeatureToggle;
import net.VrikkaDuck.AutoTrade.config.Hotkeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;

import java.util.List;

public class VillagerTweaks {

    private PlayerEntity player;
    public VillagerTweaks(){
        this.player = MinecraftClient.getInstance().player;
    }

    public void tick(MinecraftClient mc){
        if(this.player == null){
            this.player = MinecraftClient.getInstance().player;
            return;
        }
        handleVillagers(mc);
    }

    private void handleVillagers(MinecraftClient mc){

        if(!FeatureToggle.TWEAK_AUTO_TRADE.getBooleanValue()){
            return;
        }

        if(Hotkeys.TRADE.getKeybind().isKeybindHeld()) {
            Box box = player.getBoundingBox().expand(Configs.Generic.MAX_VILLAGER_DISTANCE.getDoubleValue());

            World world = player.getWorld();

            if(world == null){return;}

            List<VillagerEntity> entities = world.getNonSpectatingEntities(VillagerEntity.class, box);
            if(entities.isEmpty()){return;}

            for(VillagerEntity villager : entities){
                VillagerData villagerData = villager.getVillagerData();

                if(VillagerUtils.tradeListContains(villagerData.getProfession())){
                    if(!VillagerUtils.ignoreList.contains(villager)){
                        if(MinecraftClient.getInstance().currentScreen == null){
                            MinecraftClient.getInstance().interactionManager.interactEntity(player, villager, Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }
}
