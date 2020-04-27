package com.goldenglow.common.util.requirements.types;

import com.pixelmonessentials.common.api.requirement.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;

public class TimeRequirement implements Requirement {
    private final String name="TIME";

    public String getName(){
        return this.name;
    }

    public boolean hasRequirement(String data, EntityPlayerMP player){
        if(data.equalsIgnoreCase("day")) {
            return player.getEntityWorld().isDaytime();
        }
        else if(data.equalsIgnoreCase("night")) {
            return player.getEntityWorld().isDaytime();
        }
        else
            return  false;
    }
}
