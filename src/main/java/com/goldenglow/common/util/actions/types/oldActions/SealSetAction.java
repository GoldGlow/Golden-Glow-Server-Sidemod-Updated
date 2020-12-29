package com.goldenglow.common.util.actions.types.oldActions;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomInventory;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class SealSetAction extends ActionBase {
    public SealSetAction(){
        super("SEAL_SET");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData actionData){
        if(actionData instanceof ActionStringData){
            OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
            String[] equippedSeals = data.getEquippedSeals();
            equippedSeals[Integer.parseInt(((ActionStringData) actionData).getValue().split(":")[1])] = ((ActionStringData) actionData).getValue().split(":")[0];
            data.setPlayerSeals(equippedSeals);
            player.closeScreen();
            CustomInventory.openInventory("seals", player);
        }
    }
}
