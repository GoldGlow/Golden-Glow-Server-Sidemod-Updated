package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class CategoryUseAction extends ActionBase {
    public CategoryUseAction(){
        super("CATEGORY_USE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof BagMenu){
            BagCategory category=((BagMenu) gui).getCategory();
        }
    }
}
