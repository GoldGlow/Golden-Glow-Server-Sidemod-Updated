package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.guis.BagMenu;
import com.goldenglow.common.guis.GuiHandler;
import com.goldenglow.common.inventory.BagInventories;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class UpdateBagAction implements Action {
    public final String name="UPDATE_BAG";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof BagMenu){
            if(value.equals("ITEMS")){
                ((BagMenu) gui).setCategory(BagMenu.EnumCategory.ITEMS);
                ((BagMenu) gui).update(player);
            }
            else if(value.equals("TM_HM")){
                ((BagMenu) gui).setCategory(BagMenu.EnumCategory.TM_HM);
                ((BagMenu) gui).update(player);
            }
            else if(value.equals("KEY_ITEMS")){
                ((BagMenu) gui).setCategory(BagMenu.EnumCategory.KEY_ITEMS);
                ((BagMenu) gui).update(player);
            }
            else if(value.equals("BADGES")){
                ((BagMenu) gui).setCategory(BagMenu.EnumCategory.BADGES);
                ((BagMenu) gui).update(player);
            }
        }
    }
}
