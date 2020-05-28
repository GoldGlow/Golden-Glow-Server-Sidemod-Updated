package com.goldenglow.common.util.actions.types.oldActions;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomInventory;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class SealSetAction implements Action {
    private final String name="SEAL_SET";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        String[] equippedSeals = data.getEquippedSeals();
        equippedSeals[Integer.parseInt(value.split(":")[1])] = value.split(":")[0];
        data.setPlayerSeals(equippedSeals);
        player.closeScreen();
        CustomInventory.openInventory("seals", player);
    }
}
