package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.gyms.GymLeaderUtils;
import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.util.actions.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class StartBattleAction implements Action {
    public final String name="START_BATTLE";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        GymLeaderUtils.startGymBattle(value);
    }
}
