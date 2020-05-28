package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
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
            else if(value.equals("ARMOURERS_WORKSHOP")){
                ((BagMenu) gui).setCategory(BagMenu.EnumCategory.ARMOURERS_WORKSHOP);
                ((BagMenu) gui).update(player);
            }
        }
    }
}
