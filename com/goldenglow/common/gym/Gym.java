package com.goldenglow.common.gyms;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.handlers.GGTickHandler;
import com.goldenglow.common.util.BlockPos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class Gym
{
    public Map<String, UUID> gymLeaders = new HashMap();
    public EntityPlayer currentChallenger;
    public List<EntityPlayer> challengerList = new ArrayList();
    public String name;
    public int levelCap;
    public BlockPos warpPoint;

    public Gym(String name)
    {
        this.name = name;
    }

    public Gym(String name, int levelCap)
    {
        this.name = name;
        this.levelCap = levelCap;
    }

    public Gym(String name, BlockPos warpPoint)
    {
        this.name = name;
        this.warpPoint = warpPoint;
    }

    public Gym(String name, int levelCap, BlockPos warpPoint)
    {
        this.name = name;
        this.levelCap = levelCap;
        this.warpPoint = warpPoint;
    }

    public void addGymChallenger(EntityPlayer player)
    {
        if (this.currentChallenger == null)
        {
            this.currentChallenger = player;
            GoldenGlow.instance.tickHandler.registerChallenger(this, player);
        }
    }

    public boolean playerJoin(EntityPlayer player)
    {
        if (player != null)
        {
            if (this.currentChallenger == null)
            {
                addGymChallenger(player);
                return true;
            }
            this.challengerList.add(player);
            return true;
        }
        return false;
    }
}
