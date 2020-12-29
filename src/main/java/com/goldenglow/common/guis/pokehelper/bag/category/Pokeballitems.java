package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.List;

public class Pokeballitems extends ItemCategoryBase {
    public Pokeballitems(){
        super("Pokeballs");
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<OOItem> pokeballs=data.getPokeballs();
        String[] pokeballNames=new String[pokeballs.size()];
        for(int i=0;i<pokeballs.size();i++){
            pokeballNames[i]=pokeballs.get(i).getQuantity()+"x "+ pokeballs.get(i).getDisplayName();
        }
        return pokeballNames;
    }

    @Override
    public CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui){
        String texture="pixelmon:textures/items/pokeballs/";
        EssentialsGuis essentialsGui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(essentialsGui instanceof BagMenu){
            OOItem item=((BagMenu) essentialsGui).getCategory().getItem(player, ((BagMenu) essentialsGui).getIndex());
            String itemId=item.getItemId();
            itemId=itemId.replace("pixelmon:", "").replace("_", "");
            texture+=itemId+".png";
        }
        gui.addTexturedRect(101, texture, 48, 64, 256, 256).setScale(0.125f);
        return gui;
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        gui.addTexturedButton(510, "Withdraw", 0, 180, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(511, "Cancel", 0, 200, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{101, 510, 511};
    }

    @Override
    public void storeItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.removeFromInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.addPokeball(item);
    }

    @Override
    public void withdrawItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.addToInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.removePokeball(item);
    }

    @Override
    public OOItem getItem(EntityPlayerMP player, int index){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        return data.getPokeballs().get(index);
    }
}
