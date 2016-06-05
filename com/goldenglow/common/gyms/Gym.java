package com.goldenglow.common.gyms;

import com.goldenglow.GoldenGlow;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class Gym {
    public Map<String, UUID> gymLeaders = new HashMap<String, UUID>();
    public EntityPlayer currentChallenger;
    public List<EntityPlayer> challengerList = new ArrayList<EntityPlayer>();

    public String name;
    public int levelCap;
    public BlockPos warpPoint;

    public Gym(String name) {
        this.name=name;
    }

    public Gym(String name, int levelCap) {
        this.name=name;
        this.levelCap=levelCap;
    }

    public Gym(String name, BlockPos warpPoint) {
        this.name=name;
        this.warpPoint=warpPoint;
    }

    public Gym(String name, int levelCap, BlockPos warpPoint) {
        this.name=name;
        this.levelCap=levelCap;
        this.warpPoint=warpPoint;
    }

    public void addGymChallenger(EntityPlayer player) {
        if(currentChallenger==null) {
            currentChallenger = player;
            GoldenGlow.instance.tickHandler.registerChallenger(this, player);
        }
    }

    public boolean playerJoin(EntityPlayer player) {
        if(player!=null) {
            if(currentChallenger==null) {
                addGymChallenger(player);
                return true;
            } else {
                challengerList.add(player);
                return true;
            }
        }
        return false;
    }
}
