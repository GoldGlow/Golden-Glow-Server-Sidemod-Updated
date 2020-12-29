package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class KeyItems extends ItemCategoryBase {

    public KeyItems(){
        super("Key Items");
    }

    @Override
    public CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui){
        EssentialsGuis essentialsGui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        String item="";
        if(essentialsGui instanceof BagMenu){
            IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
            item=data.getKeyItems().get(((BagMenu) essentialsGui).getIndex());
            gui.addTexturedRect(101, GoldenGlow.customItemManager.getKeyItem(item).getResourceLocation(), 48, 64, 256, 256).setScale(0.125f);
        }
        return gui;
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        return data.getKeyItems().toArray(new String[0]);
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        gui.addTexturedButton(511, "Cancel", 0, 200, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{510};
    }
}
