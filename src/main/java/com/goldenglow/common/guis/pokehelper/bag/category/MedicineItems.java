package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.List;

public class MedicineItems extends ItemCategoryBase {
    public MedicineItems(){
        super("Medicine");
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<OOItem> medicine=data.getMedicine();
        String[] medicineNames=new String[medicine.size()];
        for(int i=0;i<medicine.size();i++){
            medicineNames[i]=medicine.get(i).getQuantity()+"x "+medicine.get(i).getDisplayName();
        }
        return medicineNames;
    }

    @Override
    public CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui){
        String texture="pixelmon:textures/items/healingitems/";
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
        gui.addTexturedButton(509, "Use", 0, 175, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(510, "Withdraw", 0, 195, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(511, "Cancel", 0, 215, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{101, 509, 510, 511};
    }

    @Override
    public void storeItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.removeFromInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.addMedicine(item);
    }

    @Override
    public void withdrawItem(EntityPlayerMP player, OOItem item){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        int amountChanged=super.addToInventory(player, item);
        item.changeQuantity(-1*amountChanged);
        data.removeMedicine(item);
    }

    @Override
    public OOItem getItem(EntityPlayerMP player, int index){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        return data.getMedicine().get(index);
    }
}
