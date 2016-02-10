package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.gyms.Gym;
import com.goldenglow.common.routes.RouteManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;

public class GGTickHandler
{
    int ticksSinceLastStepCheck;
    Map<EntityPlayer, Integer> gymChallengers = new HashMap();

    @SubscribeEvent
    public void worldTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            GoldenGlow.instance.routeManager.update(event.player.field_70170_p);
            if (this.ticksSinceLastStepCheck == 20) {
                for (EntityPlayer player : this.gymChallengers.keySet())
                {
                    int i = ((Integer)this.gymChallengers.get(player)).intValue();
                    this.gymChallengers.put(player, Integer.valueOf(i + 1));
                }
            }
            step();
        }
    }

    public void registerChallenger(Gym gym, EntityPlayer player)
    {
        this.gymChallengers.put(player, Integer.valueOf(0));
    }

    private void step()
    {
        this.ticksSinceLastStepCheck += 1;
        if (this.ticksSinceLastStepCheck < 20) {
            return;
        }
        this.ticksSinceLastStepCheck = 0;
        GoldenGlow.instance.followerHandler.stepUpdate();
    }
}
