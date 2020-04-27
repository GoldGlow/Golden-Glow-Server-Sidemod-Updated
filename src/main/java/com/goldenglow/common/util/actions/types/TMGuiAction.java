package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.guis.TeachTMMenu;
import com.goldenglow.common.inventory.BagInventories;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class TMGuiAction implements Action {
    public final String name="TM_GUI";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        TeachTMMenu menu=new TeachTMMenu(value);
        menu.init(player, null);
    }
}