package net.VrikkaDuck.AutoTrade.villager;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;

public class VillagerBuyThread extends Thread{
    //This class is to simply wait for VillagerUtils.currentScreen update

    private Packet packet;
    private ClientConnection connection;
    public VillagerBuyThread(Packet pack, ClientConnection con){
        this.packet = pack;
        this.connection = con;
    }
    @Override
    public void run(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.connection.send(packet);
    }
}