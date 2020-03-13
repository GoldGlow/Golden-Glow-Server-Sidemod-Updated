package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.util.actions.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class TMPartyAction implements Action {
    public final String name="TM_PARTY";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        BagInventories.openTMMenu(player, value);
    }
}
