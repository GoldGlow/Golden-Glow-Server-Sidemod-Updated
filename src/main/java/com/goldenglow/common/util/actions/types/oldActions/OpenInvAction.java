package com.goldenglow.common.util.actions.types.oldActions;

import com.goldenglow.common.inventory.CustomInventory;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenInvAction extends ActionBase {
    public OpenInvAction(){
        super("OPEN_INV");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            CustomInventory.openInventory(((ActionStringData) data).getValue(), player);
        }
    }
}
