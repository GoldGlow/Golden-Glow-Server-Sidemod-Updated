package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.gyms.GymLeaderUtils;
import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.util.actions.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class StopChallengersAction implements Action {
    public final String name="STOP_CHALLENGERS";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        GymLeaderUtils.stopTakingChallengers(value, player);
    }
}
