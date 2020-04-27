package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.util.scripting.OtherFunctions;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class EquipArmorAction implements Action {
    public final String name="EQUIP_ARMOR";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OtherFunctions.equipArmor(player, Integer.parseInt(value.split("@")[0]), value.split("@")[1]);
    }
}
