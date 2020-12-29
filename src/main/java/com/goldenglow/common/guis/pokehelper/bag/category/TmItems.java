package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.keyItems.OOItem;
import com.goldenglow.common.keyItems.OOTMDummy;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.List;

public class TmItems extends ItemCategoryBase {

    public TmItems(){
        super("TMs/HMs");
    }

    @Override
    public CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui){
        String texture="pixelmon:textures/items/tms/";
        EssentialsGuis essentialsGui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(essentialsGui instanceof BagMenu){
            IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
            List<OOTMDummy> tms=data.getTMs();
            OOTMDummy tm=tms.get(((BagMenu) essentialsGui).getIndex());
            String tmString=tm.isHm() ? "hm" : "tm";
            Attack move=new Attack(tm.getAttackName());
            tmString+=move.getType().getName();
            texture+=tmString+".png";
        }
        gui.addTexturedRect(101, texture, 48, 64, 256, 256).setScale(0.125f);
        return gui;
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<OOTMDummy> tms=data.getTMs();
        String[] tmNames=new String[tms.size()];
        for(int i=0;i<tms.size();i++){
            tmNames[i]=tms.get(i).getFullTMName();
        }
        return tmNames;
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        gui.addTexturedButton(509, "Teach", 0, 180, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        gui.addTexturedButton(511, "Cancel", 0, 200, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{101, 509, 510};
    }
}
