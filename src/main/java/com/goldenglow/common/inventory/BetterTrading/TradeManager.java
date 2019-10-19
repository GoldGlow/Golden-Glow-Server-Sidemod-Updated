package com.goldenglow.common.inventory.BetterTrading;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TradeManager {
    public List<Trade> activeTrades=new ArrayList<Trade>();

    public boolean alreadyTrading(EntityPlayerMP player){
        for(Trade trade: this.activeTrades){
            for(EntityPlayerMP playerMP:trade.getPlayers()){
                if(playerMP.getName().equals(player.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public Trade getPlayerTrade(EntityPlayerMP player){
        for(Trade trade: this.activeTrades){
            for(EntityPlayerMP playerMP:trade.getPlayers()){
                if(playerMP.getName().equals(player.getName())){
                    return trade;
                }
            }
        }
        return null;
    }

    public void cancelTrade(EntityPlayerMP player){
        Trade trade=this.getPlayerTrade(player);
        for(int i=0;i<trade.getPlayers().length;i++){
            EntityPlayerMP playerMP=trade.getPlayers()[i];
            TradingOffer offer=trade.getOffers()[i];
            PlayerPartyStorage storage= Pixelmon.storageManager.getParty(playerMP);
            for(ItemStack itemStack: offer.items){
                playerMP.inventory.addItemStackToInventory(itemStack);
            }
            storage.changeMoney(offer.money);
            for(Pokemon pokemon:offer.pokemonList){
                storage.add(pokemon);
            }
            playerMP.closeScreen();
        }
        this.activeTrades.remove(trade);
    }
}
