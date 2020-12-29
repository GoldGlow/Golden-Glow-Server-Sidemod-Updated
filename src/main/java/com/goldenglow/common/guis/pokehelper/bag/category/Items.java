package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.List;

public class Items extends ItemCategoryBase {

    public Items(){
        super("Items");
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<OOItem> ooItems=data.getBagItems();
        String[] items=new String[ooItems.size()];
        for(int i=0;i<ooItems.size();i++){
            OOItem item=ooItems.get(i);
            items[i]=item.getQuantity()+"x "+ item.getDisplayName();
        }
        return items;
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        gui.addTexturedButton(509, "Use", 0, 175, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(510, "Withdraw", 0, 195, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(511, "Cancel", 0, 215, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{509, 510, 511};
    }

    @Override
    public void storeItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.removeFromInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.addBagItem(item);
    }

    @Override
    public void withdrawItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.addToInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.removeBagItem(item);
    }

    @Override
    public OOItem getItem(EntityPlayerMP player, int index){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        return data.getBagItems().get(index);
    }
}
