package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.gyms.Gym;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class GGTickHandler {

    int ticksSinceLastStepCheck;
    Map<EntityPlayer, Integer> gymChallengers = new HashMap<EntityPlayer, Integer>();

    @SubscribeEvent
    public void worldTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GoldenGlow.instance.routeManager.update(event.player.worldObj);
            if(ticksSinceLastStepCheck==20)
                for(EntityPlayer player : gymChallengers.keySet()) {
                    int i = gymChallengers.get(player);
                    gymChallengers.put(player, i+1);
                }
            step();
        }
    }

    public void registerChallenger(Gym gym, EntityPlayer player) {
        gymChallengers.put(player, 0);
    }

    private void step() {
        this.ticksSinceLastStepCheck += 1;
        if (this.ticksSinceLastStepCheck < 20)
            return;
        this.ticksSinceLastStepCheck = 0;
        //GoldenGlow.instance.followerHandler.stepUpdate();
    }
}
