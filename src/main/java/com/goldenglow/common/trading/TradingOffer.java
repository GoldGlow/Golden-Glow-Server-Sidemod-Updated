package com.goldenglow.common.trading;

import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TradingOffer {
    EntityPlayerMP player;
    int money;
    List<Pokemon> pokemonList;
    List<OOItem> items;
    List<ItemStack> inventoryBackup;
    public boolean ready;

    public TradingOffer(EntityPlayerMP player){
        this.player=player;
        this.money=0;
        this.pokemonList=new ArrayList<Pokemon>();
        this.items=new ArrayList<OOItem>();
        this.ready=false;
        this.inventoryBackup=new ArrayList<ItemStack>();
    }

    public EntityPlayerMP getPlayer(){
        return this.player;
    }

    public List<Pokemon> getPokemon(){
        return pokemonList;
    }

    public int getMoney(){
        return money;
    }

    public List<OOItem> getItems(){
        return this.items;
    }

    public void addPokemon(int pokemonSlot){
        if(Pixelmon.storageManager.getParty(this.player).getTeam().size()>1) {
            this.pokemonList.add(Pixelmon.storageManager.getParty(this.player).get(pokemonSlot));
            Pixelmon.storageManager.getParty(this.player).set(pokemonSlot, null);
        }
    }

    public void removePokemon(int pokemonSlot){
        Pixelmon.storageManager.getParty(this.player).add(this.pokemonList.get(pokemonSlot));
        this.pokemonList.remove(pokemonSlot);
    }

    public void addMoney(int amount){
        if(Pixelmon.storageManager.getParty(this.player).getMoney()>=amount){
            Pixelmon.storageManager.getParty(this.player).changeMoney(-1*amount);
            this.money+=amount;
        }
    }

    public void removeMoney(int amount){
        if(Pixelmon.storageManager.getParty(this.player).getMoney()>=amount){
            Pixelmon.storageManager.getParty(this.player).changeMoney(-1*amount);
            this.money+=amount;
        }
    }

    public void addItem(OOItem offerItem){
        for(OOItem item:this.items){
            if(item.getItemId().equals(offerItem.getItemId())){
                item.changeQuantity(offerItem.getQuantity());
                return;
            }
        }
        this.items.add(offerItem);
    }

    public String[] getItemNames(){
        String[] itemNames=new String[this.items.size()];
        for(int i=0;i<itemNames.length;i++){
            itemNames[i]=this.items.get(i).getQuantity()+"x "+this.items.get(i).getDisplayName();
        }
        return itemNames;
    }
}
