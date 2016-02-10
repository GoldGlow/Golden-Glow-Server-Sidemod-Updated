package com.goldenglow.common.handlers;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.scripted.ScriptNpc;

public class FollowerHandler
{
    public Map<EntityPlayer, ScriptNpc> followMap = new HashMap();

    public void stepUpdate()
    {
        for (ScriptNpc npc : this.followMap.values()) {
            if ((!npc.getTempData("lastX").equals(Integer.valueOf(npc.getBlockX()))) || (!npc.getTempData("lastZ").equals(Integer.valueOf(npc.getBlockZ()))))
            {
                npc.setTempData("lastX", Integer.valueOf(npc.getBlockX()));
                npc.setTempData("lastZ", Integer.valueOf(npc.getBlockZ()));
                npc.setTempData("lastRand", Double.valueOf(Math.random()));
            }
        }
    }
}
