package com.goldenglow.common.util.actions.types.gyms;

import com.goldenglow.common.gyms.GymLeaderUtils;
import com.pixelmonessentials.common.api.action.Action;
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
