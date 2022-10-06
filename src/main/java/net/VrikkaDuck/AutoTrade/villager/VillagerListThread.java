package net.VrikkaDuck.AutoTrade.villager;

import net.VrikkaDuck.AutoTrade.config.Configs;
import net.minecraft.entity.passive.VillagerEntity;

public class VillagerListThread extends Thread{
    private VillagerEntity villager;
    public VillagerListThread(VillagerEntity villagerEntity){
        this.villager = villagerEntity;
    }
    @Override
    public void run(){
        VillagerUtils.ignoreList.add(this.villager);
        try {
            Thread.sleep((long) Configs.Generic.TIME_BETWEEN_TRIES.getDoubleValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(VillagerUtils.ignoreList.contains(this.villager)) {
            VillagerUtils.ignoreList.remove(this.villager);
        }
    }
}
