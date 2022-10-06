package net.VrikkaDuck.AutoTrade.villager;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class VillagerTrade{
    private final ItemStack firstBuyItem, secondBuyItem, sellItem;
    private final VillagerProfession profession;

    public VillagerTrade(TradeOffer offer, VillagerProfession profession){
        this.firstBuyItem = offer.getAdjustedFirstBuyItem();
        this.secondBuyItem = offer.getSecondBuyItem();
        this.sellItem = offer.getSellItem();
        this.profession = profession;
    }

    public VillagerTrade(NbtCompound compound){

        if(compound.contains("buy")){
            NbtCompound buyCompound = (NbtCompound) compound.get("buy");
            this.firstBuyItem = ItemStack.fromNbt(buyCompound);
        }else{
            this.firstBuyItem = ItemStack.EMPTY;
        }
        if(compound.contains("buyB")){
            NbtCompound buyBCompound = (NbtCompound) compound.get("buyB");
            this.secondBuyItem = ItemStack.fromNbt(buyBCompound);
        }else{
            this.secondBuyItem = ItemStack.EMPTY;
        }
        if(compound.contains("sell")){
            NbtCompound sellCompound = (NbtCompound) compound.get("sell");
            this.sellItem = ItemStack.fromNbt(sellCompound);
        }else{
            this.sellItem = ItemStack.EMPTY;
        }
        if(compound.contains("profession")){
            NbtCompound professionCompound = (NbtCompound) compound.get("profession");
            this.profession = VillagerUtils.idToProfession(professionCompound.getString("id"));
        }else{
            this.profession = VillagerProfession.NONE;
        }
    }

    public ItemStack getFirstBuyItem() {
        return firstBuyItem;
    }

    public ItemStack getSecondBuyItem() {
        return secondBuyItem;
    }

    public ItemStack getSellItem() {
        return sellItem;
    }

    public VillagerProfession getProfession(){
        return this.profession;
    }

    public NbtCompound toNbt(){

        //FirstBuyItem
        NbtCompound buy = new NbtCompound();
        buy.put("Count", NbtByte.of((byte) this.getFirstBuyItem().getCount()));
        buy.put("id", NbtString.of(this.getFirstBuyItem().getItem().toString()));

        //SecondBuyItem
        NbtCompound buyB = new NbtCompound();
        buyB.put("Count", NbtByte.of((byte) (this.getSecondBuyItem() == null ? 1 : this.getSecondBuyItem().getCount())));
        buyB.put("id", NbtString.of(this.getSecondBuyItem() == null ? "" : this.getSecondBuyItem().getItem().toString()));

        //SellItem
        NbtCompound sell = new NbtCompound();
        sell.put("Count", NbtByte.of((byte) this.getSellItem().getCount()));
        sell.put("id", NbtString.of(this.getSellItem().getItem().toString()));

        //Profession
        NbtCompound profession = new NbtCompound();
        profession.put("id", NbtString.of(this.getProfession().id()));

        //Combine
        NbtCompound trade = new NbtCompound();
        trade.put("buy", buy);
        trade.put("buyB", buyB);
        trade.put("sell", sell);
        trade.put("profession", profession);

        return trade;
    }
    public boolean equals(VillagerTrade trade){
        if(trade.getProfession().equals(this.getProfession())){
            if(trade.getFirstBuyItem().getItem().equals(this.getFirstBuyItem().getItem())){
                if(trade.getSecondBuyItem().getItem().equals(this.getSecondBuyItem().getItem())){
                    if(trade.getSellItem().getItem().equals(this.getSellItem().getItem())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean equals(TradeOffer trade){
        if(trade.getAdjustedFirstBuyItem().getItem().equals(this.getFirstBuyItem().getItem())){
            if(trade.getSecondBuyItem().getItem().equals(this.getSecondBuyItem().getItem())){
                if(trade.getSellItem().getItem().equals(this.getSellItem().getItem())){
                    return true;
                }
            }
        }
        return false;
    }
    public TradeOffer toTradeOffer(){

        TradeOffer t = new TradeOffer(this.getFirstBuyItem(), this.getSecondBuyItem(),this.getSellItem(), 0,0,0,0);

        return t;
    }
}
