package com.goldenglow.common.inventory.BetterTrading;

import net.minecraft.entity.player.EntityPlayerMP;

public class Trade {
    private EntityPlayerMP[] players;
    private TradingOffer[] offers;
    public boolean[] ready;

    public Trade(EntityPlayerMP player1, EntityPlayerMP player2){
        this.players=new EntityPlayerMP[]{player1, player2};
        this.offers=new TradingOffer[]{new TradingOffer(player1), new TradingOffer(player2)};
        this.ready=new boolean[]{false, false};
    }

    public EntityPlayerMP[] getPlayers(){
        return this.players;
    }

    public TradingOffer[] getOffers(){
        return this.offers;
    }
}
