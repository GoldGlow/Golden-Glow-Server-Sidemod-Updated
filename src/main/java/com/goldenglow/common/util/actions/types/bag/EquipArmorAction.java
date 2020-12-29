package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.util.scripting.OtherFunctions;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class EquipArmorAction extends ActionBase {
    public EquipArmorAction(){
        super("EQUIP_ARMOR");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData)
        OtherFunctions.equipArmor(player, Integer.parseInt(((ActionStringData) data).getValue().split("@")[0]), ((ActionStringData) data).getValue().split("@")[1]);
    }
}
