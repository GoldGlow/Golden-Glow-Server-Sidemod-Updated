package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InventoryItems extends ItemCategoryBase {
    public InventoryItems(){
        super("Inventory");
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof BagMenu){
            ArrayList<OOItem> inventory=((BagMenu) gui).getInventory();
            String[] itemNames=new String[inventory.size()];
            for(int i=0;i<inventory.size();i++){
                itemNames[i]=inventory.get(i).getQuantity()+"x "+inventory.get(i).getDisplayName();
            }
            return itemNames;
        }
        return new String[0];
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        gui.addTexturedButton(509, "Use", 0, 175, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(510, "Store", 0, 195, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(511, "Cancel", 0, 215, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{509, 510, 511};
    }
}
