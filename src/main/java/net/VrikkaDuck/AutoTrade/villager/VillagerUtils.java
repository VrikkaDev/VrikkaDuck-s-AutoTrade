package net.VrikkaDuck.AutoTrade.villager;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.render.MessageRenderer;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.Constants;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.NBTUtils;
import net.VrikkaDuck.AutoTrade.Variables;
import net.VrikkaDuck.AutoTrade.config.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.List;

public class VillagerUtils {

    public static VillagerEntity currentVillager = null;
    public static MerchantScreen currentScreen = null;
    public static boolean shouldClose = false;
    private static MessageRenderer mr = new MessageRenderer(0xDD000000, 0xFF999999).setCentered(false, false).setMessageBoxWidth(100).setExpandUp(true);

    //Why dont i just have villagers list here that updates as the professions list updates:/
    private static List<VillagerProfession> professions = new ArrayList<>();

    //This is to put cooldown to every villager after trade
    public static List<VillagerEntity> ignoreList = new ArrayList<>();

    public static void AddTradeToList(VillagerTrade trade){
        List<String> stringslist = Lists.TRADES_LIST.getStrings();

        if(tradeListContains(trade)) {
            setOverlayText("Removed selected trade from the list");
            String string = String.valueOf(trade.toNbt());
            stringslist.remove(String.valueOf(string));
            Lists.TRADES_LIST.setStrings(stringslist);
            return;
        }else{
            setOverlayText("Added trade to the list");
        }

        String string = String.valueOf(trade.toNbt());
        stringslist.add(String.valueOf(string));
        Lists.TRADES_LIST.setStrings(stringslist);

        updateLocalValues();
    }
    public static boolean tradeListContains(VillagerTrade trade){
        List<VillagerTrade> tradeOffers = new ArrayList<>();
        boolean r = false;
        try {
            for (String s : Lists.TRADES_LIST.getStrings()) {
                tradeOffers.add(new VillagerTrade(NbtHelper.fromNbtProviderString(s)));
            }
            for (VillagerTrade to : tradeOffers) {
                if(trade.equals(to)){
                    r = true;
                }
            }
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return r;
    }
    public static boolean tradeListContains(TradeOffer trade){
        List<VillagerTrade> offers = new ArrayList<>();
        try {
            for (String s : Lists.TRADES_LIST.getStrings()) {
                offers.add(new VillagerTrade(NbtHelper.fromNbtProviderString(s)));
            }
            for (VillagerTrade to : offers) {
                if(to.equals(trade)){
                    return true;
                }
            }
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static boolean tradeListContains(VillagerProfession profession){
        return professions.contains(profession);
    }

    //Updates local villager values so dont need to get them always from the string list
    public static void updateLocalValues(){
        List<VillagerTrade> trades = new ArrayList<>();
        try {
            professions = new ArrayList<>();
            for (String s : Lists.TRADES_LIST.getStrings()) {
                trades.add(new VillagerTrade(NbtHelper.fromNbtProviderString(s)));
            }
            for (VillagerTrade to : trades) {
                if(!professions.contains(to.getProfession())){
                    professions.add(to.getProfession());
                }
            }
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setOverlayText(String string){
        mr.addMessage(1000, string);
    }

    //Called in InGameHudMixin
    public static void drawMessages(DrawContext context){
        mr.drawMessages(MinecraftClient.getInstance().getWindow().getScaledWidth()/2-50,
                (int) (MinecraftClient.getInstance().getWindow().getScaledHeight()/1.25f), context);
    }

    public static VillagerProfession idToProfession(String id){
        switch (id){
            case "armorer":
                return VillagerProfession.ARMORER;
            case "butcher":
                return VillagerProfession.BUTCHER;
            case "cartographer":
                return VillagerProfession.CARTOGRAPHER;
            case "cleric":
                return VillagerProfession.CLERIC;
            case "farmer":
                return VillagerProfession.FARMER;
            case "fisherman":
                return VillagerProfession.FISHERMAN;
            case "fletcher":
                return VillagerProfession.FLETCHER;
            case "leatherworker":
                return VillagerProfession.LEATHERWORKER;
            case "librarian":
                return VillagerProfession.LIBRARIAN;
            case "mason":
                return VillagerProfession.MASON;
            case "nitwit":
                return VillagerProfession.NITWIT;
            case "shepherd":
                return VillagerProfession.SHEPHERD;
            case "toolsmith":
                return VillagerProfession.TOOLSMITH;
            case "weaponsmith":
                return VillagerProfession.WEAPONSMITH;
            default:
                return VillagerProfession.NONE;
        }
    }
    public static void addVillagerToIgnoreList(VillagerEntity villager){
        VillagerListThread thread = new VillagerListThread(villager);
        thread.start();
    }

    public static int getTradeId(TradeOfferList offerList){

        for(int i = 0; i < offerList.size(); i++){
            if(tradeListContains(offerList.get(i)) && !offerList.get(i).isDisabled()){
                return i;
            }
        }
        //funny
        return 69;
    }
}
