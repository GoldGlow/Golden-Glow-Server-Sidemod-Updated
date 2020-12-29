package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.inventory.BagInventories;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class TMPartyAction extends ActionBase {
    public TMPartyAction(){
        super("TM_PARTY");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            BagInventories.openTMMenu(player, ((ActionStringData) data).getValue());
        }
    }
}
