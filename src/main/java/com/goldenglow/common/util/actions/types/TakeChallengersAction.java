package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.gyms.GymLeaderUtils;
import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.util.actions.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class TakeChallengersAction implements Action {
    public final String name="TAKE_CHALLENGERS";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        GymLeaderUtils.takeChallengers(value, player);
    }
}
