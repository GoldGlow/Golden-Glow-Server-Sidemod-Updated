package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.inventory.CustomInventory;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenInvAction implements Action {
    public final String name="OPEN_INV";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        CustomInventory.openInventory(value, player);
    }
}
